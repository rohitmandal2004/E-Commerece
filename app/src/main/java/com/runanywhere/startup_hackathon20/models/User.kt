package com.runanywhere.startup_hackathon20.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val profileImage: String = "",
    val addresses: MutableList<Address> = mutableListOf()
)

data class Address(
    val id: String,
    val label: String, // Home, Work, Other
    val fullName: String,
    val phoneNumber: String,
    val addressLine1: String,
    val addressLine2: String = "",
    val city: String,
    val state: String,
    val zipCode: String,
    val isDefault: Boolean = false
) {
    fun getFullAddress(): String {
        return buildString {
            append(addressLine1)
            if (addressLine2.isNotEmpty()) {
                append(", $addressLine2")
            }
            append(", $city")
            append(", $state - $zipCode")
        }
    }
}

data class Order(
    val id: String,
    val userId: String,
    val items: List<OrderItem>,
    val total: Double,
    val status: OrderStatus,
    val orderDate: Long,
    val deliveryAddress: String,
    val paymentMethod: String
)

data class OrderItem(
    val product: Product,
    val quantity: Int,
    val price: Double
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
