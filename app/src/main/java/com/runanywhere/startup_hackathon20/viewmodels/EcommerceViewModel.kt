package com.runanywhere.startup_hackathon20.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.startup_hackathon20.data.ProductRepository
import com.runanywhere.startup_hackathon20.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class EcommerceViewModel : ViewModel() {

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Selected category
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Price filter range
    private val _minPrice = MutableStateFlow(0.0)
    val minPrice: StateFlow<Double> = _minPrice.asStateFlow()

    private val _maxPrice = MutableStateFlow(Double.MAX_VALUE)
    val maxPrice: StateFlow<Double> = _maxPrice.asStateFlow()

    // Rating filter
    private val _minRating = MutableStateFlow(0f)
    val minRating: StateFlow<Float> = _minRating.asStateFlow()

    // Sort option
    private val _sortOption = MutableStateFlow(SortOption.RELEVANCE)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    // Products based on search and filter
    val displayedProducts: StateFlow<List<Product>> = combine(
        searchQuery,
        selectedCategory,
        minPrice,
        maxPrice,
        minRating,
        sortOption
    ) { flows ->
        FilterCriteria(
            query = flows[0] as String,
            category = flows[1] as String?,
            minPrice = flows[2] as Double,
            maxPrice = flows[3] as Double,
            minRating = flows[4] as Float,
            sort = flows[5] as SortOption
        )
    }.flatMapLatest { criteria ->
        when {
            criteria.category != null -> ProductRepository.getProductsByCategory(criteria.category)
            criteria.query.isNotBlank() -> ProductRepository.searchProducts(criteria.query)
            else -> ProductRepository.products
        }
    }.map { products ->
        // Apply filters
        var filtered = products.filter { product ->
            product.discountedPrice >= minPrice.value &&
                    product.discountedPrice <= maxPrice.value &&
                    product.rating >= minRating.value
        }

        // Apply sorting
        filtered = when (sortOption.value) {
            SortOption.PRICE_LOW_TO_HIGH -> filtered.sortedBy { it.discountedPrice }
            SortOption.PRICE_HIGH_TO_LOW -> filtered.sortedByDescending { it.discountedPrice }
            SortOption.RATING -> filtered.sortedByDescending { it.rating }
            SortOption.DISCOUNT -> filtered.sortedByDescending { it.discount }
            SortOption.NEWEST -> filtered.reversed()
            else -> filtered
        }

        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Featured products
    val featuredProducts: StateFlow<List<Product>> = ProductRepository.getFeaturedProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Top rated products
    val topRatedProducts: StateFlow<List<Product>> = ProductRepository.getTopRatedProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Notifications
    private val _notifications = MutableStateFlow(generateSampleNotifications())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    val unreadNotificationCount: StateFlow<Int> = notifications.map { list ->
        list.count { !it.isRead }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // Flash Sales
    private val _flashSales = MutableStateFlow(generateSampleFlashSales())
    val flashSales: StateFlow<List<FlashSale>> = _flashSales.asStateFlow()

    val activeFlashSales: StateFlow<List<FlashSale>> = flashSales.map { sales ->
        sales.filter { it.isActive }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Shopping cart
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Applied coupon
    private val _appliedCoupon = MutableStateFlow<Coupon?>(null)
    val appliedCoupon: StateFlow<Coupon?> = _appliedCoupon.asStateFlow()

    // Available coupons
    private val _availableCoupons = MutableStateFlow(getSampleCoupons())
    val availableCoupons: StateFlow<List<Coupon>> = _availableCoupons.asStateFlow()

    // Cart total (before discount)
    val cartSubtotal: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.subtotal }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Discount amount
    val discountAmount: StateFlow<Double> = combine(
        cartSubtotal,
        appliedCoupon
    ) { subtotal, coupon ->
        coupon?.calculateDiscount(subtotal) ?: 0.0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Cart total (after discount)
    val cartTotal: StateFlow<Double> = combine(
        cartSubtotal,
        discountAmount
    ) { subtotal, discount ->
        maxOf(0.0, subtotal - discount)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Cart item count
    val cartItemCount: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // Recently viewed products
    private val _recentlyViewedProducts = MutableStateFlow<List<Product>>(emptyList())
    val recentlyViewedProducts: StateFlow<List<Product>> = _recentlyViewedProducts.asStateFlow()

    // Wishlist products
    private val _wishlistProducts = MutableStateFlow<List<Product>>(emptyList())
    val wishlistProducts: StateFlow<List<Product>> = _wishlistProducts.asStateFlow()

    // Comparison list
    private val _comparisonProducts = MutableStateFlow<List<Product>>(emptyList())
    val comparisonProducts: StateFlow<List<Product>> = _comparisonProducts.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val existingItemIndex = currentItems.indexOfFirst { it.product.id == product.id }

            if (existingItemIndex >= 0) {
                // Increase quantity
                val existingItem = currentItems[existingItemIndex]
                currentItems[existingItemIndex] = existingItem.copy(
                    quantity = existingItem.quantity + 1
                )
            } else {
                // Add new item
                currentItems.add(CartItem(product, 1))
            }

            _cartItems.value = currentItems
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            _cartItems.value = _cartItems.value.filter { it.product.id != productId }
        }
    }

    fun updateCartItemQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            if (quantity <= 0) {
                removeFromCart(productId)
                return@launch
            }

            val currentItems = _cartItems.value.toMutableList()
            val itemIndex = currentItems.indexOfFirst { it.product.id == productId }

            if (itemIndex >= 0) {
                currentItems[itemIndex] = currentItems[itemIndex].copy(quantity = quantity)
                _cartItems.value = currentItems
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _appliedCoupon.value = null
    }

    // Coupon functions
    fun applyCoupon(couponCode: String): Boolean {
        val coupon = _availableCoupons.value.find {
            it.code.equals(couponCode, ignoreCase = true)
        }

        if (coupon != null && coupon.isValid() && cartSubtotal.value >= coupon.minOrderAmount) {
            _appliedCoupon.value = coupon
            return true
        }
        return false
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    // Comparison functions
    fun addToComparison(product: Product): Boolean {
        val currentList = _comparisonProducts.value
        if (currentList.size >= 4) return false // Max 4 products
        if (currentList.any { it.id == product.id }) return false // Already in list

        _comparisonProducts.value = currentList + product
        return true
    }

    fun removeFromComparison(productId: String) {
        _comparisonProducts.value = _comparisonProducts.value.filter { it.id != productId }
    }

    fun clearComparison() {
        _comparisonProducts.value = emptyList()
    }

    fun isInComparison(productId: String): Boolean {
        return _comparisonProducts.value.any { it.id == productId }
    }

    // Recently viewed
    fun trackProductView(product: Product) {
        viewModelScope.launch {
            val currentList = _recentlyViewedProducts.value.toMutableList()
            // Remove if already exists to avoid duplicates
            currentList.removeAll { it.id == product.id }
            // Add to beginning
            currentList.add(0, product)
            // Keep only last 10 viewed products
            if (currentList.size > 10) {
                currentList.removeAt(currentList.size - 1)
            }
            _recentlyViewedProducts.value = currentList
        }
    }

    fun getProductById(id: String): Product? {
        return ProductRepository.getProductById(id)
    }

    // Wishlist functions
    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val currentList = _wishlistProducts.value.toMutableList()
            if (currentList.any { it.id == product.id }) {
                currentList.removeAll { it.id == product.id }
            } else {
                currentList.add(product)
            }
            _wishlistProducts.value = currentList
        }
    }

    fun isInWishlist(productId: String): Boolean {
        return _wishlistProducts.value.any { it.id == productId }
    }

    fun removeFromWishlist(productId: String) {
        viewModelScope.launch {
            val currentList = _wishlistProducts.value.toMutableList()
            currentList.removeAll { it.id == productId }
            _wishlistProducts.value = currentList
        }
    }

    // Filter and Sort functions
    fun setPriceRange(min: Double, max: Double) {
        _minPrice.value = min
        _maxPrice.value = max
    }

    fun setMinRating(rating: Float) {
        _minRating.value = rating
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun clearFilters() {
        _minPrice.value = 0.0
        _maxPrice.value = Double.MAX_VALUE
        _minRating.value = 0f
        _sortOption.value = SortOption.RELEVANCE
    }

    // Notification functions
    fun markNotificationAsRead(notificationId: String) {
        val updatedList = _notifications.value.map { notification ->
            if (notification.id == notificationId) {
                notification.copy(isRead = true)
            } else {
                notification
            }
        }
        _notifications.value = updatedList
    }

    fun markAllNotificationsAsRead() {
        val updatedList = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = updatedList
    }

    fun deleteNotification(notificationId: String) {
        _notifications.value = _notifications.value.filter { it.id != notificationId }
    }

    // Flash Sale functions
    fun purchaseFlashSale(saleId: String): Boolean {
        val sale = _flashSales.value.find { it.id == saleId }
        if (sale != null && sale.isActive && sale.stockRemaining > 0) {
            val updatedSales = _flashSales.value.map { flashSale ->
                if (flashSale.id == saleId) {
                    flashSale.copy(stockRemaining = flashSale.stockRemaining - 1)
                } else {
                    flashSale
                }
            }
            _flashSales.value = updatedSales
            return true
        }
        return false
    }

    private fun generateSampleNotifications(): List<Notification> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            Notification(
                id = "notif1",
                title = "Order Shipped! 📦",
                message = "Your order #ORD001 has been shipped and will arrive in 2-3 days.",
                timestamp = currentTime - 2 * 60 * 60 * 1000, // 2 hours ago
                type = NotificationType.ORDER_UPDATE,
                isRead = false,
                orderId = "ORD001"
            ),
            Notification(
                id = "notif2",
                title = "Flash Sale Alert! ⚡",
                message = "50% OFF on Electronics! Limited time offer. Hurry!",
                timestamp = currentTime - 5 * 60 * 60 * 1000, // 5 hours ago
                type = NotificationType.PROMOTION,
                isRead = false
            ),
            Notification(
                id = "notif3",
                title = "Price Drop Alert 📉",
                message = "iPhone 15 Pro is now ₹10,000 cheaper!",
                timestamp = currentTime - 24 * 60 * 60 * 1000, // 1 day ago
                type = NotificationType.PRICE_DROP,
                isRead = true,
                productId = "phone1"
            ),
            Notification(
                id = "notif4",
                title = "Back in Stock! 🎉",
                message = "Samsung Galaxy S24 Ultra is now available.",
                timestamp = currentTime - 2 * 24 * 60 * 60 * 1000, // 2 days ago
                type = NotificationType.BACK_IN_STOCK,
                isRead = true,
                productId = "phone2"
            ),
            Notification(
                id = "notif5",
                title = "Delivery Update 🚚",
                message = "Your order is out for delivery today between 2-5 PM.",
                timestamp = currentTime - 3 * 24 * 60 * 60 * 1000, // 3 days ago
                type = NotificationType.DELIVERY,
                isRead = true,
                orderId = "ORD002"
            )
        )
    }

    private fun generateSampleFlashSales(): List<FlashSale> {
        val currentTime = System.currentTimeMillis()
        val oneHour = 60 * 60 * 1000L
        val twoHours = 2 * oneHour
        val sixHours = 6 * oneHour

        return listOf(
            FlashSale(
                id = "flash1",
                productId = "phone1",
                originalPrice = 119999.0,
                salePrice = 99999.0,
                discountPercentage = 17,
                startTime = currentTime - oneHour,
                endTime = currentTime + twoHours,
                stockLimit = 50,
                stockRemaining = 12
            ),
            FlashSale(
                id = "flash2",
                productId = "elec1",
                originalPrice = 39999.0,
                salePrice = 29999.0,
                discountPercentage = 25,
                startTime = currentTime - 30 * 60 * 1000,
                endTime = currentTime + oneHour,
                stockLimit = 30,
                stockRemaining = 8
            ),
            FlashSale(
                id = "flash3",
                productId = "fashion6",
                originalPrice = 2499.0,
                salePrice = 1249.0,
                discountPercentage = 50,
                startTime = currentTime - twoHours,
                endTime = currentTime + sixHours,
                stockLimit = 100,
                stockRemaining = 45
            )
        )
    }
}

data class FilterCriteria(
    val query: String,
    val category: String?,
    val minPrice: Double,
    val maxPrice: Double,
    val minRating: Float,
    val sort: SortOption
)

enum class SortOption {
    RELEVANCE,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    RATING,
    DISCOUNT,
    NEWEST
}
