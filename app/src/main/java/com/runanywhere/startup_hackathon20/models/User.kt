package com.runanywhere.startup_hackathon20.models

import androidx.compose.runtime.Immutable

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val addresses: MutableList<Address> = mutableListOf(),
    // NEW FLIPKART FEATURES
    val plusMembership: PlusMembership? = null,
    val superCoins: SuperCoin = SuperCoin(balance = 0, expiringCoins = 0, expiryDate = null),
    val wallet: Wallet = Wallet(balance = 0.0),
    val giftCards: List<GiftCard> = emptyList(),
    val referralProgram: ReferralProgram? = null,
    val savedPaymentMethods: List<PaymentOption> = emptyList(),
    val wishlist: List<String> = emptyList(), // product IDs
    val searchHistory: List<SearchHistory> = emptyList(),
    val budgetTracker: BudgetTracker? = null,
    val profileImage: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val alternatePhone: String? = null,
    val emailVerified: Boolean = false,
    val phoneVerified: Boolean = false,
    val accountCreatedDate: String = "",
    val lastLoginDate: String = ""
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
