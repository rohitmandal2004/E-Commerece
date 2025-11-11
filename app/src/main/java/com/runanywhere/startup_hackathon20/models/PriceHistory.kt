package com.runanywhere.startup_hackathon20.models

import androidx.compose.runtime.Immutable

@Immutable
data class PriceHistory(
    val productId: String,
    val price: Double,
    val timestamp: Long,
    val date: String
)

@Immutable
data class FlashSale(
    val id: String,
    val productId: String,
    val originalPrice: Double,
    val salePrice: Double,
    val discountPercentage: Int,
    val startTime: Long,
    val endTime: Long,
    val stockLimit: Int,
    val stockRemaining: Int
) {
    val isActive: Boolean
        get() {
            val currentTime = System.currentTimeMillis()
            return currentTime in startTime..endTime && stockRemaining > 0
        }

    val timeRemaining: Long
        get() = maxOf(0, endTime - System.currentTimeMillis())
}
