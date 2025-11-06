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
    val customerReviews: List<CustomerReview> = emptyList()
) {
    val discountedPrice: Double
        get() = if (discount > 0) {
            price * (1 - discount / 100.0)
        } else {
            price
        }
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
