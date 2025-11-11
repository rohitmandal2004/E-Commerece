package com.runanywhere.startup_hackathon20.models

data class Coupon(
    val code: String,
    val description: String,
    val discountType: DiscountType,
    val discountValue: Double,
    val minOrderAmount: Double = 0.0,
    val maxDiscount: Double = Double.MAX_VALUE,
    val validFrom: Long = System.currentTimeMillis(),
    val validUntil: Long = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000), // 30 days
    val usageLimit: Int = Int.MAX_VALUE,
    val usedCount: Int = 0,
    val isActive: Boolean = true,
    val applicableCategories: List<String> = emptyList()
) {
    fun isValid(): Boolean {
        val now = System.currentTimeMillis()
        return isActive &&
                now >= validFrom &&
                now <= validUntil &&
                usedCount < usageLimit
    }

    fun calculateDiscount(orderAmount: Double): Double {
        if (!isValid() || orderAmount < minOrderAmount) return 0.0

        return when (discountType) {
            DiscountType.PERCENTAGE -> {
                val discount = orderAmount * (discountValue / 100.0)
                minOf(discount, maxDiscount)
            }

            DiscountType.FIXED_AMOUNT -> {
                minOf(discountValue, orderAmount)
            }

            DiscountType.FREE_SHIPPING -> {
                0.0 // Shipping discount handled separately
            }

            DiscountType.BUY_ONE_GET_ONE -> {
                0.0 // BOGO handled separately
            }
        }
    }
}

enum class DiscountType {
    PERCENTAGE,
    FIXED_AMOUNT,
    FREE_SHIPPING,
    BUY_ONE_GET_ONE
}

// Sample coupons
fun getSampleCoupons() = listOf(
    Coupon(
        code = "WELCOME10",
        description = "10% off on first order",
        discountType = DiscountType.PERCENTAGE,
        discountValue = 10.0,
        minOrderAmount = 500.0,
        maxDiscount = 500.0
    ),
    Coupon(
        code = "SAVE100",
        description = "₹100 off on orders above ₹1000",
        discountType = DiscountType.FIXED_AMOUNT,
        discountValue = 100.0,
        minOrderAmount = 1000.0
    ),
    Coupon(
        code = "FREESHIP",
        description = "Free shipping on all orders",
        discountType = DiscountType.FREE_SHIPPING,
        discountValue = 0.0,
        minOrderAmount = 0.0
    ),
    Coupon(
        code = "MEGA50",
        description = "50% off - Mega Sale",
        discountType = DiscountType.PERCENTAGE,
        discountValue = 50.0,
        minOrderAmount = 1500.0,
        maxDiscount = 1000.0
    )
)
