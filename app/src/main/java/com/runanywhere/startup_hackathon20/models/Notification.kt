package com.runanywhere.startup_hackathon20.models

import androidx.compose.runtime.Immutable

@Immutable
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: NotificationType,
    val isRead: Boolean = false,
    val orderId: String? = null,
    val productId: String? = null
)

enum class NotificationType {
    ORDER_UPDATE,
    PROMOTION,
    PRICE_DROP,
    BACK_IN_STOCK,
    DELIVERY,
    GENERAL
}
