package com.runanywhere.startup_hackathon20.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.startup_hackathon20.data.ProductRepository
import com.runanywhere.startup_hackathon20.models.CartItem
import com.runanywhere.startup_hackathon20.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EcommerceViewModel : ViewModel() {

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Selected category
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Products based on search and filter
    val displayedProducts: StateFlow<List<Product>> = combine(
        searchQuery,
        selectedCategory
    ) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, category) ->
        when {
            category != null -> ProductRepository.getProductsByCategory(category)
            query.isNotBlank() -> ProductRepository.searchProducts(query)
            else -> ProductRepository.products
        }
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

    // Shopping cart
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Cart total
    val cartTotal: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.subtotal }
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
    }

    fun getProductById(id: String): Product? {
        return ProductRepository.getProductById(id)
    }
}
