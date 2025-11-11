package com.runanywhere.startup_hackathon20.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.startup_hackathon20.models.Address
import com.runanywhere.startup_hackathon20.models.Order
import com.runanywhere.startup_hackathon20.models.OrderItem
import com.runanywhere.startup_hackathon20.models.OrderStatus
import com.runanywhere.startup_hackathon20.models.User
import com.runanywhere.startup_hackathon20.data.ProductRepository
import com.runanywhere.startup_hackathon20.data.UserDao
import com.runanywhere.startup_hackathon20.data.UserEntity
import com.runanywhere.startup_hackathon20.data.Converters
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(private val userDao: UserDao? = null) : ViewModel() {

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

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val converters = Converters()

    // Mock user database (fallback when Room is not available)
    private val users = mutableMapOf<String, Pair<String, User>>() // email to (password, user)

    init {
        // Add demo user for testing
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

        // Insert demo user into database
        viewModelScope.launch {
            userDao?.let { dao ->
                try {
                    // Check if demo user exists
                    if (!dao.userExists("demo@techxon.com")) {
                        val demoEntity = UserEntity(
                            email = "demo@techxon.com",
                            id = "1",
                            name = "Demo User",
                            phone = "+1234567890",
                            password = "demo123",
                            addressesJson = converters.fromAddressList(demoUser.addresses)
                        )
                        dao.insertUser(demoEntity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Generate some sample orders for demo user
        generateSampleOrders()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginError.value = null

                // Try database first
                userDao?.let { dao ->
                    val userEntity = dao.getUserByEmail(email)
                    if (userEntity == null) {
                        _loginError.value = "User not registered. Please sign up first."
                        return@launch
                    }

                    if (userEntity.password != password) {
                        _loginError.value = "Invalid password"
                        return@launch
                    }

                    // Convert entity to User model
                    val addresses = try {
                        converters.toAddressList(userEntity.addressesJson).toMutableList()
                    } catch (e: Exception) {
                        mutableListOf()
                    }

                    val user = User(
                        id = userEntity.id,
                        name = userEntity.name,
                        email = userEntity.email,
                        phone = userEntity.phone,
                        addresses = addresses
                    )

                    _currentUser.value = user
                    _isLoggedIn.value = true
                    _selectedAddress.value = user.addresses.find { it.isDefault }
                    loadUserOrders()
                    return@launch
                }

                // Fallback to in-memory storage
                val userEntry = users[email]
                if (userEntry == null) {
                    _loginError.value = "User not registered. Please sign up first."
                    return@launch
                }

                if (userEntry.first != password) {
                    _loginError.value = "Invalid password"
                    return@launch
                }

                _currentUser.value = userEntry.second
                _isLoggedIn.value = true
                _selectedAddress.value = userEntry.second.addresses.find { it.isDefault }
                loadUserOrders()
                return@launch
            } catch (e: Exception) {
                _loginError.value = "Login failed: ${e.message}"
            }
        }
    }

    fun signup(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            try {
                _loginError.value = null

                // Validate inputs
                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    _loginError.value = "Please fill all required fields"
                    return@launch
                }

                // Try database first
                userDao?.let { dao ->
                    if (dao.userExists(email)) {
                        _loginError.value = "Email already registered. Please login."
                        return@launch
                    }

                    val newUser = User(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        email = email,
                        phone = phone
                    )

                    val userEntity = UserEntity(
                        email = email,
                        id = newUser.id,
                        name = name,
                        phone = phone,
                        password = password,
                        addressesJson = converters.fromAddressList(emptyList())
                    )

                    dao.insertUser(userEntity)
                    _currentUser.value = newUser
                    _isLoggedIn.value = true
                    _orders.value = emptyList()
                    return@launch
                }

                // Fallback to in-memory storage
                if (users.containsKey(email)) {
                    _loginError.value = "Email already registered. Please login."
                    return@launch
                }

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

            } catch (e: Exception) {
                _loginError.value = "Signup failed: ${e.message}"
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
        _orders.value = emptyList()
        _selectedAddress.value = null
        _loginError.value = null
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
        viewModelScope.launch {
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

                // Update database
                userDao?.let { dao ->
                    try {
                        val userEntity = dao.getUserByEmail(user.email)
                        userEntity?.let {
                            val updatedEntity = it.copy(
                                addressesJson = converters.fromAddressList(user.addresses)
                            )
                            dao.updateUser(updatedEntity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
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

                // Update database
                userDao?.let { dao ->
                    try {
                        val userEntity = dao.getUserByEmail(user.email)
                        userEntity?.let {
                            val updatedEntity = it.copy(
                                addressesJson = converters.fromAddressList(user.addresses)
                            )
                            dao.updateUser(updatedEntity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
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
