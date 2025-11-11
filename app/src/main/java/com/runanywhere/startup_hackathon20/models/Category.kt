package com.runanywhere.startup_hackathon20.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val subcategories: List<String> = emptyList()
)

fun getCategories() = listOf(
    // ELECTRONICS & GADGETS
    Category(
        "electronics",
        "Electronics",
        Icons.Default.Phone,
        Color(0xFF2979FF),
        listOf(
            "Mobiles",
            "Laptops",
            "Tablets",
            "Cameras",
            "Audio",
            "Wearables",
            "Gaming",
            "Accessories"
        )
    ),
    Category(
        "mobiles",
        "Mobiles & Accessories",
        Icons.Default.Phone,
        Color(0xFF1976D2),
        listOf(
            "Smartphones",
            "Feature Phones",
            "Cases & Covers",
            "Screen Protectors",
            "Chargers",
            "Power Banks"
        )
    ),
    Category(
        "laptops",
        "Laptops & Computers",
        Icons.Default.Settings,
        Color(0xFF0288D1),
        listOf("Laptops", "Desktops", "Monitors", "Keyboards", "Mouse", "Printers", "Storage")
    ),
    Category(
        "tablets",
        "Tablets & iPads",
        Icons.Default.Phone,
        Color(0xFF0277BD),
        listOf("Apple iPads", "Android Tablets", "Tablet Accessories", "Stylus Pens")
    ),
    Category(
        "cameras",
        "Cameras & Photography",
        Icons.Default.Star,
        Color(0xFF01579B),
        listOf("DSLR", "Mirrorless", "Action Cameras", "Lenses", "Tripods", "Camera Bags")
    ),
    Category(
        "audio",
        "Audio & Music",
        Icons.Default.Phone,
        Color(0xFF4A148C),
        listOf("Headphones", "Earbuds", "Speakers", "Soundbars", "Home Theater", "Microphones")
    ),
    Category(
        "wearables",
        "Smart Wearables",
        Icons.Default.Star,
        Color(0xFF6A1B9A),
        listOf("Smart Watches", "Fitness Bands", "Smart Glasses", "VR Headsets")
    ),
    Category(
        "gaming",
        "Gaming",
        Icons.Default.Menu,
        Color(0xFF7B1FA2),
        listOf("Consoles", "PC Gaming", "Gaming Accessories", "Games", "Gaming Chairs")
    ),

    // FASHION
    Category(
        "fashion",
        "Fashion",
        Icons.Default.ShoppingCart,
        Color(0xFFE91E63),
        listOf("Men's Wear", "Women's Wear", "Kids' Wear", "Footwear", "Accessories", "Watches")
    ),
    Category(
        "mens_fashion",
        "Men's Fashion",
        Icons.Default.Person,
        Color(0xFFC2185B),
        listOf("T-Shirts", "Shirts", "Jeans", "Trousers", "Suits", "Ethnic Wear", "Winter Wear")
    ),
    Category(
        "womens_fashion",
        "Women's Fashion",
        Icons.Default.Person,
        Color(0xFFAD1457),
        listOf("Sarees", "Kurtas", "Dresses", "Tops", "Jeans", "Leggings", "Ethnic Wear")
    ),
    Category(
        "kids_fashion",
        "Kids' Fashion",
        Icons.Default.Face,
        Color(0xFF880E4F),
        listOf("Boys Clothing", "Girls Clothing", "Baby Wear", "School Uniforms", "Party Wear")
    ),
    Category(
        "footwear",
        "Footwear",
        Icons.Default.Star,
        Color(0xFF4A148C),
        listOf(
            "Men's Shoes",
            "Women's Shoes",
            "Kids' Shoes",
            "Sports Shoes",
            "Formal Shoes",
            "Sandals"
        )
    ),
    Category(
        "watches",
        "Watches & Eyewear",
        Icons.Default.Favorite,
        Color(0xFF311B92),
        listOf("Smart Watches", "Luxury Watches", "Fashion Watches", "Sunglasses", "Eyeglasses")
    ),
    Category(
        "accessories",
        "Fashion Accessories",
        Icons.Default.ShoppingCart,
        Color(0xFF1A237E),
        listOf("Bags", "Wallets", "Belts", "Jewelry", "Scarves", "Hats", "Ties")
    ),

    // HOME & FURNITURE
    Category(
        "home",
        "Home & Living",
        Icons.Default.Home,
        Color(0xFF00BFA5),
        listOf("Furniture", "Home Decor", "Kitchen", "Bedding", "Bath", "Storage", "Lighting")
    ),
    Category(
        "furniture",
        "Furniture",
        Icons.Default.Home,
        Color(0xFF00897B),
        listOf(
            "Sofas",
            "Beds",
            "Dining Tables",
            "Wardrobes",
            "Office Furniture",
            "Outdoor Furniture"
        )
    ),
    Category(
        "kitchen",
        "Kitchen & Dining",
        Icons.Default.Menu,
        Color(0xFF00796B),
        listOf("Cookware", "Dinnerware", "Kitchen Tools", "Storage Containers", "Appliances")
    ),
    Category(
        "home_decor",
        "Home Decor",
        Icons.Default.Home,
        Color(0xFF00695C),
        listOf("Wall Art", "Curtains", "Cushions", "Rugs", "Mirrors", "Plants", "Candles")
    ),
    Category(
        "bedding",
        "Bedding & Bath",
        Icons.Default.Home,
        Color(0xFF004D40),
        listOf("Bed Sheets", "Comforters", "Pillows", "Towels", "Bath Mats", "Shower Curtains")
    ),
    Category(
        "lighting",
        "Lighting",
        Icons.Default.Star,
        Color(0xFF004D40),
        listOf(
            "Ceiling Lights",
            "Table Lamps",
            "Floor Lamps",
            "Wall Lights",
            "Smart Lights",
            "Bulbs"
        )
    ),

    // APPLIANCES
    Category(
        "appliances",
        "Home Appliances",
        Icons.Default.Settings,
        Color(0xFFFF6F00),
        listOf("Large Appliances", "Small Appliances", "Kitchen Appliances", "Air Quality")
    ),
    Category(
        "large_appliances",
        "Large Appliances",
        Icons.Default.Settings,
        Color(0xFFE65100),
        listOf("Refrigerators", "Washing Machines", "Air Conditioners", "Microwaves", "Dishwashers")
    ),
    Category(
        "small_appliances",
        "Small Appliances",
        Icons.Default.Settings,
        Color(0xFFBF360C),
        listOf("Vacuum Cleaners", "Irons", "Mixer Grinders", "Juicers", "Coffee Makers", "Toasters")
    ),

    // SPORTS & FITNESS
    Category(
        "sports",
        "Sports & Fitness",
        Icons.Default.Star,
        Color(0xFF43A047),
        listOf("Fitness Equipment", "Sports Gear", "Athleisure", "Outdoor Sports", "Indoor Sports")
    ),
    Category(
        "fitness",
        "Fitness Equipment",
        Icons.Default.Star,
        Color(0xFF388E3C),
        listOf(
            "Treadmills",
            "Cycles",
            "Dumbbells",
            "Yoga Mats",
            "Protein Supplements",
            "Fitness Trackers"
        )
    ),
    Category(
        "outdoor_sports",
        "Outdoor Sports",
        Icons.Default.LocationOn,
        Color(0xFF2E7D32),
        listOf("Cricket", "Football", "Badminton", "Tennis", "Cycling", "Camping", "Swimming")
    ),

    // BEAUTY & PERSONAL CARE
    Category(
        "beauty",
        "Beauty & Personal Care",
        Icons.Default.Face,
        Color(0xFFFF4081),
        listOf("Makeup", "Skincare", "Haircare", "Fragrances", "Bath & Body", "Personal Care")
    ),
    Category(
        "makeup",
        "Makeup",
        Icons.Default.Face,
        Color(0xFFF50057),
        listOf("Face", "Eyes", "Lips", "Nails", "Makeup Tools", "Makeup Remover")
    ),
    Category(
        "skincare",
        "Skincare",
        Icons.Default.Face,
        Color(0xFFC51162),
        listOf("Face Wash", "Moisturizers", "Serums", "Sunscreen", "Face Masks", "Toners")
    ),
    Category(
        "haircare",
        "Haircare",
        Icons.Default.Face,
        Color(0xFF880E4F),
        listOf("Shampoos", "Conditioners", "Hair Oil", "Hair Styling", "Hair Color", "Hair Tools")
    ),

    // BOOKS & MEDIA
    Category(
        "books",
        "Books & Media",
        Icons.Default.Menu,
        Color(0xFF7C4DFF),
        listOf("Fiction", "Non-Fiction", "Academic", "Comics", "Magazines", "Audiobooks")
    ),
    Category(
        "fiction",
        "Fiction Books",
        Icons.Default.Menu,
        Color(0xFF651FFF),
        listOf("Romance", "Thriller", "Mystery", "Sci-Fi", "Fantasy", "Historical Fiction")
    ),
    Category(
        "non_fiction",
        "Non-Fiction",
        Icons.Default.Menu,
        Color(0xFF6200EA),
        listOf("Biography", "Self-Help", "Business", "History", "Science", "Philosophy")
    ),

    // TOYS & BABY
    Category(
        "toys",
        "Toys & Baby Products",
        Icons.Default.Star,
        Color(0xFFFF5722),
        listOf("Toys", "Baby Care", "Baby Gear", "Nursery", "Maternity", "Kids' Books")
    ),
    Category(
        "baby_care",
        "Baby Care",
        Icons.Default.Favorite,
        Color(0xFFFF3D00),
        listOf("Diapers", "Baby Food", "Bath & Skin", "Baby Health", "Wipes", "Feeding")
    ),

    // GROCERY & FOOD
    Category(
        "grocery",
        "Grocery & Gourmet",
        Icons.Default.ShoppingCart,
        Color(0xFF4CAF50),
        listOf("Staples", "Snacks", "Beverages", "Personal Care", "Household", "Organic")
    ),
    Category(
        "food",
        "Food & Beverages",
        Icons.Default.ShoppingCart,
        Color(0xFF388E3C),
        listOf(
            "Snacks",
            "Chocolates",
            "Beverages",
            "Tea & Coffee",
            "Cooking Ingredients",
            "Organic Food"
        )
    ),

    // AUTOMOTIVE
    Category(
        "automotive",
        "Automotive",
        Icons.Default.LocationOn,
        Color(0xFF424242),
        listOf("Car Accessories", "Bike Accessories", "Car Electronics", "Helmets", "Car Care")
    ),

    // PET SUPPLIES
    Category(
        "pets",
        "Pet Supplies",
        Icons.Default.Favorite,
        Color(0xFF795548),
        listOf(
            "Dog Supplies",
            "Cat Supplies",
            "Fish & Aquatics",
            "Bird Supplies",
            "Pet Food",
            "Pet Care"
        )
    ),

    // OFFICE & STATIONERY
    Category(
        "office",
        "Office & Stationery",
        Icons.Default.Settings,
        Color(0xFF607D8B),
        listOf("Office Supplies", "Stationery", "Art Supplies", "School Supplies", "Organizers")
    ),

    // HEALTH & WELLNESS
    Category(
        "health",
        "Health & Wellness",
        Icons.Default.Favorite,
        Color(0xFFE53935),
        listOf("Health Devices", "Supplements", "Ayurveda", "Sexual Wellness", "Health Foods")
    ),

    // JEWELRY
    Category(
        "jewelry",
        "Jewelry & Precious Stones",
        Icons.Default.Star,
        Color(0xFFFFD700),
        listOf("Gold", "Silver", "Diamond", "Gemstones", "Fashion Jewelry", "Wedding Jewelry")
    ),

    // GIFTS
    Category(
        "gifts",
        "Gifts & Occasions",
        Icons.Default.Add,
        Color(0xFFFF6B6B),
        listOf(
            "Birthday Gifts",
            "Anniversary",
            "Wedding",
            "Corporate Gifts",
            "Personalized",
            "Gift Cards"
        )
    )
)

// Helper function to get category by ID
fun getCategoryById(id: String): Category? {
    return getCategories().find { it.id == id }
}

// Get all main categories (parent categories)
fun getMainCategories(): List<Category> {
    return listOf(
        getCategories().find { it.id == "electronics" }!!,
        getCategories().find { it.id == "fashion" }!!,
        getCategories().find { it.id == "home" }!!,
        getCategories().find { it.id == "appliances" }!!,
        getCategories().find { it.id == "sports" }!!,
        getCategories().find { it.id == "beauty" }!!,
        getCategories().find { it.id == "books" }!!,
        getCategories().find { it.id == "toys" }!!,
        getCategories().find { it.id == "grocery" }!!,
        getCategories().find { it.id == "automotive" }!!,
        getCategories().find { it.id == "pets" }!!,
        getCategories().find { it.id == "office" }!!,
        getCategories().find { it.id == "health" }!!,
        getCategories().find { it.id == "jewelry" }!!,
        getCategories().find { it.id == "gifts" }!!
    )
}
