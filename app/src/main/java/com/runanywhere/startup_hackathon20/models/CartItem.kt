package com.runanywhere.startup_hackathon20.models

data class CartItem(
    val product: Product,
    val quantity: Int = 1
) {
    val subtotal: Double
        get() = product.discountedPrice * quantity
}
