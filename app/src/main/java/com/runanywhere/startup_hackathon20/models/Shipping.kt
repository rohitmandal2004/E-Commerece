package com.runanywhere.startup_hackathon20.models

data class ShipmentTracking(
    val orderId: String,
    val trackingNumber: String,
    val courier: String,
    val status: ShipmentStatus,
    val currentLocation: String,
    val estimatedDelivery: Long,
    val trackingHistory: List<TrackingEvent>
)

enum class ShipmentStatus {
    ORDER_PLACED,
    PROCESSING,
    PACKED,
    SHIPPED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    RETURNED,
    CANCELLED;

    fun getDisplayName(): String = when (this) {
        ORDER_PLACED -> "Order Placed"
        PROCESSING -> "Processing"
        PACKED -> "Packed"
        SHIPPED -> "Shipped"
        IN_TRANSIT -> "In Transit"
        OUT_FOR_DELIVERY -> "Out for Delivery"
        DELIVERED -> "Delivered"
        RETURNED -> "Returned"
        CANCELLED -> "Cancelled"
    }

    fun getDescription(): String = when (this) {
        ORDER_PLACED -> "Your order has been placed successfully"
        PROCESSING -> "We're processing your order"
        PACKED -> "Your order has been packed"
        SHIPPED -> "Your order has been shipped"
        IN_TRANSIT -> "Your package is on its way"
        OUT_FOR_DELIVERY -> "Your package is out for delivery"
        DELIVERED -> "Your order has been delivered"
        RETURNED -> "Your order has been returned"
        CANCELLED -> "Your order has been cancelled"
    }
}

data class TrackingEvent(
    val status: String,
    val location: String,
    val timestamp: Long,
    val description: String
)

data class ShippingRate(
    val courier: String,
    val deliveryDays: Int,
    val cost: Double,
    val codAvailable: Boolean
)

// Sample tracking data
fun getSampleTracking(orderId: String): ShipmentTracking {
    val now = System.currentTimeMillis()
    return ShipmentTracking(
        orderId = orderId,
        trackingNumber = "TXN${orderId}",
        courier = "Blue Dart",
        status = ShipmentStatus.IN_TRANSIT,
        currentLocation = "Mumbai Hub",
        estimatedDelivery = now + (2L * 24 * 60 * 60 * 1000), // 2 days
        trackingHistory = listOf(
            TrackingEvent(
                status = "Order Placed",
                location = "New Delhi",
                timestamp = now - (2L * 24 * 60 * 60 * 1000),
                description = "Order has been placed"
            ),
            TrackingEvent(
                status = "Processing",
                location = "New Delhi Warehouse",
                timestamp = now - (1L * 24 * 60 * 60 * 1000 + 20 * 60 * 60 * 1000),
                description = "Order is being processed"
            ),
            TrackingEvent(
                status = "Packed",
                location = "New Delhi Warehouse",
                timestamp = now - (1L * 24 * 60 * 60 * 1000 + 12 * 60 * 60 * 1000),
                description = "Order has been packed"
            ),
            TrackingEvent(
                status = "Shipped",
                location = "New Delhi",
                timestamp = now - (1L * 24 * 60 * 60 * 1000),
                description = "Package shipped from warehouse"
            ),
            TrackingEvent(
                status = "In Transit",
                location = "Mumbai Hub",
                timestamp = now - (6 * 60 * 60 * 1000),
                description = "Package arrived at sorting facility"
            )
        )
    )
}
