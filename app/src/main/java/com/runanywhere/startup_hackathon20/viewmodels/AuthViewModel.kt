package com.runanywhere.startup_hackathon20.viewmodels

import androidx.lifecycle.ViewModel
import com.runanywhere.startup_hackathon20.models.Address
import com.runanywhere.startup_hackathon20.models.Order
import com.runanywhere.startup_hackathon20.models.OrderItem
import com.runanywhere.startup_hackathon20.models.OrderStatus
import com.runanywhere.startup_hackathon20.models.User
import com.runanywhere.startup_hackathon20.data.ProductRepository
import kotlinx.coroutines.flow.*
import java.util.UUID

class AuthViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    // Mock user database
    private val users = mutableMapOf<String, Pair<String, User>>() // email to (password, user)

    init {
        // Add some demo users with sample address
        val demoUser = User(
            id = "1",
            name = "Demo User",
            email = "demo@techxon.com",
            phone = "+1234567890",
            addresses = mutableListOf(
                Address(
                    id = UUID.randomUUID().toString(),
                    label = "Home",
                    fullName = "Demo User",
                    phoneNumber = "+1234567890",
                    addressLine1 = "123 Main Street",
                    addressLine2 = "Apt 4B",
                    city = "New York",
                    state = "NY",
                    zipCode = "10001",
                    isDefault = true
                )
            )
        )
        users["demo@techxon.com"] = "demo123" to demoUser

        // Generate some sample orders for demo user
        generateSampleOrders()
    }

    fun login(email: String, password: String): Boolean {
        val userEntry = users[email]
        return if (userEntry != null && userEntry.first == password) {
            _currentUser.value = userEntry.second
            _isLoggedIn.value = true
            _selectedAddress.value = userEntry.second.addresses.find { it.isDefault }
            loadUserOrders()
            true
        } else {
            false
        }
    }

    fun signup(name: String, email: String, phone: String, password: String): Boolean {
        return if (!users.containsKey(email)) {
            val newUser = User(
                id = UUID.randomUUID().toString(),
                name = name,
                email = email,
                phone = phone
            )
            users[email] = password to newUser
            _currentUser.value = newUser
            _isLoggedIn.value = true
            _orders.value = emptyList()
            true
        } else {
            false
        }
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
        _orders.value = emptyList()
        _selectedAddress.value = null
    }

    private fun loadUserOrders() {
        // Load orders for current user
        _orders.value = sampleOrders.filter { it.userId == _currentUser.value?.id }
    }

    fun addOrder(order: Order) {
        sampleOrders = sampleOrders + order
        loadUserOrders()
    }

    // Address Management Functions
    fun addAddress(address: Address) {
        _currentUser.value?.let { user ->
            // If this is the first address or marked as default, make it default
            val newAddress = if (user.addresses.isEmpty() || address.isDefault) {
                // Unset all other defaults
                user.addresses.forEach { addr ->
                    val index = user.addresses.indexOf(addr)
                    if (addr.isDefault && index >= 0) {
                        user.addresses[index] = addr.copy(isDefault = false)
                    }
                }
                address.copy(isDefault = true)
            } else {
                address
            }

            user.addresses.add(newAddress)
            _currentUser.value = user.copy()

            // If this is the default address, update selected address
            if (newAddress.isDefault) {
                _selectedAddress.value = newAddress
            }
        }
    }

    fun deleteAddress(addressId: String) {
        _currentUser.value?.let { user ->
            val addressToRemove = user.addresses.find { it.id == addressId }
            user.addresses.removeIf { it.id == addressId }

            // If deleted address was default and there are other addresses, make first one default
            if (addressToRemove?.isDefault == true && user.addresses.isNotEmpty()) {
                user.addresses[0] = user.addresses[0].copy(isDefault = true)
                _selectedAddress.value = user.addresses[0]
            } else if (user.addresses.isEmpty()) {
                _selectedAddress.value = null
            }

            _currentUser.value = user.copy()
        }
    }

    fun setDefaultAddress(addressId: String) {
        _currentUser.value?.let { user ->
            user.addresses.forEachIndexed { index, address ->
                user.addresses[index] = address.copy(isDefault = address.id == addressId)
                if (address.id == addressId) {
                    _selectedAddress.value = user.addresses[index]
                }
            }
            _currentUser.value = user.copy()
        }
    }

    fun selectAddressForOrder(address: Address) {
        _selectedAddress.value = address
    }

    fun getDefaultAddress(): Address? {
        return _currentUser.value?.addresses?.find { it.isDefault }
    }

    // Edit Profile Functions
    fun updateProfile(name: String, phone: String) {
        _currentUser.value?.let { user ->
            val updatedUser = user.copy(name = name, phone = phone)
            // Update in users map
            users[user.email] = users[user.email]!!.first to updatedUser
            _currentUser.value = updatedUser
        }
    }

    fun changeLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun createOrder(
        cartItems: List<com.runanywhere.startup_hackathon20.models.CartItem>,
        total: Double,
        paymentMethod: String,
        deliveryAddress: String
    ) {
        val currentUserId = _currentUser.value?.id ?: return

        val orderItems = cartItems.map { cartItem ->
            OrderItem(
                product = cartItem.product,
                quantity = cartItem.quantity,
                price = cartItem.product.discountedPrice
            )
        }

        val newOrder = Order(
            id = "ORD${String.format("%03d", (sampleOrders.size + 1))}",
            userId = currentUserId,
            items = orderItems,
            total = total,
            status = OrderStatus.PENDING,
            orderDate = System.currentTimeMillis(),
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod
        )

        addOrder(newOrder)
    }

    private fun generateSampleOrders() {
        // Get products from repository
        val phone1 = ProductRepository.getProductById("phone1")!!
        val phone2 = ProductRepository.getProductById("phone2")!!
        val fashion6 = ProductRepository.getProductById("fashion6")!!
        val elec1 = ProductRepository.getProductById("elec1")!!
        val home3 = ProductRepository.getProductById("home3")!!

        sampleOrders = listOf(
            Order(
                id = "ORD001",
                userId = "1",
                items = listOf(
                    OrderItem(phone1, 1, phone1.discountedPrice),
                    OrderItem(elec1, 2, elec1.discountedPrice)
                ),
                total = phone1.discountedPrice + (elec1.discountedPrice * 2),
                status = OrderStatus.DELIVERED,
                orderDate = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000, // 7 days ago
                deliveryAddress = "123 Main Street, Apt 4B, New York, NY - 10001",
                paymentMethod = "UPI"
            ),
            Order(
                id = "ORD002",
                userId = "1",
                items = listOf(
                    OrderItem(fashion6, 1, fashion6.discountedPrice)
                ),
                total = fashion6.discountedPrice,
                status = OrderStatus.SHIPPED,
                orderDate = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000, // 2 days ago
                deliveryAddress = "123 Main Street, Apt 4B, New York, NY - 10001",
                paymentMethod = "Card"
            ),
            Order(
                id = "ORD003",
                userId = "1",
                items = listOf(
                    OrderItem(phone2, 1, phone2.discountedPrice),
                    OrderItem(home3, 1, home3.discountedPrice)
                ),
                total = phone2.discountedPrice + home3.discountedPrice,
                status = OrderStatus.PENDING,
                orderDate = System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000, // 1 day ago
                deliveryAddress = "123 Main Street, Apt 4B, New York, NY - 10001",
                paymentMethod = "COD"
            )
        )
    }

    companion object {
        private var sampleOrders = emptyList<Order>()
    }
}
