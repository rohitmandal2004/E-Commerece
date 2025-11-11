package com.runanywhere.startup_hackathon20.models

import androidx.compose.runtime.Immutable

@Immutable
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val rating: Float = 4.5f,
    val reviews: Int = 0,
    val inStock: Boolean = true,
    val discount: Int = 0, // Percentage
    val additionalImages: List<String> = emptyList(),
    val customerReviews: List<CustomerReview> = emptyList(),
    // NEW FLIPKART FEATURES
    val seller: Seller? = null,
    val specifications: List<ProductSpecification> = emptyList(),
    val variants: List<ProductVariant> = emptyList(),
    val questions: List<ProductQuestion> = emptyList(),
    val returnPolicy: ReturnPolicy? = null,
    val deliveryOptions: List<DeliveryOption> = emptyList(),
    val emiAvailable: Boolean = false,
    val emiOptions: List<EMIOption> = emptyList(),
    val bankOffers: List<BankOffer> = emptyList(),
    val similarProducts: List<String> = emptyList(), // product IDs
    val frequentlyBoughtTogether: List<String> = emptyList(), // product IDs
    val brand: String = "",
    val warranty: String = "",
    val manufactureDate: String = "",
    val countryOfOrigin: String = "India",
    val hsnCode: String = "", // corrected property name
    val tags: List<String> = emptyList(),
    val isBestseller: Boolean = false,
    val isNewArrival: Boolean = false,
    val isPlusExclusive: Boolean = false,
    val stockQuantity: Int = 100,
    val minOrderQuantity: Int = 1,
    val maxOrderQuantity: Int = 10
) {
    val discountedPrice: Double
        get() = if (discount > 0) {
            price * (1 - discount / 100.0)
        } else {
            price
        }

    val savedAmount: Double
        get() = price - discountedPrice
}

@Immutable
data class CustomerReview(
    val id: String,
    val customerName: String,
    val rating: Float,
    val comment: String,
    val date: String,
    val verified: Boolean = true
)