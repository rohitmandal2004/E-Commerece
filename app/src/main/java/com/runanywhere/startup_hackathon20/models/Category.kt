package com.runanywhere.startup_hackathon20.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val color: Color
)

fun getCategories() = listOf(
    Category("electronics", "Electronics", Icons.Default.Phone, Color(0xFF2196F3)),
    Category("fashion", "Fashion", Icons.Default.ShoppingCart, Color(0xFFE91E63)),
    Category("home", "Home & Living", Icons.Default.Home, Color(0xFF4CAF50)),
    Category("sports", "Sports", Icons.Default.Star, Color(0xFFFF9800)),
    Category("books", "Books", Icons.Default.Menu, Color(0xFF9C27B0)),
    Category("beauty", "Beauty", Icons.Default.Face, Color(0xFFFF5722))
)
