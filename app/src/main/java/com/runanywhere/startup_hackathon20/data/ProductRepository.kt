package com.runanywhere.startup_hackathon20.data

import com.runanywhere.startup_hackathon20.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

object ProductRepository {

    private val allProducts = listOf(
        // ========== SMARTPHONES (Electronics Category) ==========
        Product(
            id = "phone1",
            name = "iPhone 15 Pro Max",
            description = "Latest flagship with A17 Pro chip, titanium design, 256GB storage, 6.7-inch display",
            price = 1199.99,
            imageUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400",
            category = "electronics",
            rating = 4.9f,
            reviews = 3200,
            discount = 5
        ),
        Product(
            id = "phone2",
            name = "Samsung Galaxy S24 Ultra",
            description = "200MP camera, S Pen included, 512GB storage, 6.8-inch AMOLED display",
            price = 1299.99,
            imageUrl = "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400",
            category = "electronics",
            rating = 4.8f,
            reviews = 2890,
            discount = 10
        ),
        Product(
            id = "phone3",
            name = "Google Pixel 8 Pro",
            description = "AI-powered photography, Tensor G3 chip, 256GB, Pure Android experience",
            price = 999.99,
            imageUrl = "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=400",
            category = "electronics",
            rating = 4.7f,
            reviews = 1560,
            discount = 15
        ),
        Product(
            id = "phone4",
            name = "OnePlus 12 Pro",
            description = "Hasselblad camera, 100W fast charging, 512GB, 120Hz display",
            price = 899.99,
            imageUrl = "https://images.unsplash.com/photo-1592286927505-02c0b7c5eac3?w=400",
            category = "electronics",
            rating = 4.6f,
            reviews = 980,
            discount = 20
        ),
        Product(
            id = "phone5",
            name = "Xiaomi 14 Ultra",
            description = "Leica optics, 5000mAh battery, 1TB storage, ceramic body",
            price = 1099.99,
            imageUrl = "https://images.unsplash.com/photo-1585060544812-6b45742d762f?w=400",
            category = "electronics",
            rating = 4.7f,
            reviews = 1240,
            discount = 12
        ),
        Product(
            id = "phone6",
            name = "iPhone 14 Pro",
            description = "A16 Bionic chip, Dynamic Island, 256GB, triple camera system",
            price = 999.99,
            imageUrl = "https://images.unsplash.com/photo-1678652197517-6c4062da08c3?w=400",
            category = "electronics",
            rating = 4.8f,
            reviews = 4200,
            discount = 18
        ),
        Product(
            id = "phone7",
            name = "Samsung Galaxy Z Fold 5",
            description = "Foldable display, multitasking powerhouse, S Pen compatible, 512GB",
            price = 1799.99,
            imageUrl = "https://images.unsplash.com/photo-1616348436168-de43ad0db179?w=400",
            category = "electronics",
            rating = 4.6f,
            reviews = 890,
            discount = 8
        ),
        Product(
            id = "phone8",
            name = "OPPO Find X6 Pro",
            description = "Periscope telephoto, 5000mAh battery, 256GB, flagship performance",
            price = 949.99,
            imageUrl = "https://images.unsplash.com/photo-1574944985070-8f3ebc6b79d2?w=400",
            category = "electronics",
            rating = 4.5f,
            reviews = 670,
            discount = 15
        ),
        Product(
            id = "phone9",
            name = "Nothing Phone 2",
            description = "Glyph Interface, clean Android, 256GB, unique design",
            price = 699.99,
            imageUrl = "https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=400",
            category = "electronics",
            rating = 4.4f,
            reviews = 1120,
            discount = 10
        ),
        Product(
            id = "phone10",
            name = "Asus ROG Phone 7",
            description = "Gaming beast, 165Hz display, 512GB, RGB lighting, cooling fan",
            price = 1099.99,
            imageUrl = "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=400",
            category = "electronics",
            rating = 4.8f,
            reviews = 1450,
            discount = 12
        ),
        Product(
            id = "phone11",
            name = "Motorola Edge 40 Pro",
            description = "Curved display, 512GB, 68W charging, premium mid-range",
            price = 799.99,
            imageUrl = "https://images.unsplash.com/photo-1605236453806-6ff36851218e?w=400",
            category = "electronics",
            rating = 4.5f,
            reviews = 780,
            discount = 20
        ),
        Product(
            id = "phone12",
            name = "Realme GT 5 Pro",
            description = "Snapdragon flagship, 240W charging, 1TB, affordable flagship",
            price = 749.99,
            imageUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400",
            category = "electronics",
            rating = 4.4f,
            reviews = 920,
            discount = 25
        ),
        Product(
            id = "phone13",
            name = "Vivo X100 Pro",
            description = "Zeiss optics, gimbal stabilization, 512GB, premium photography",
            price = 1149.99,
            imageUrl = "https://images.unsplash.com/photo-1580910051074-3eb694886505?w=400",
            category = "electronics",
            rating = 4.7f,
            reviews = 650,
            discount = 10
        ),
        Product(
            id = "phone14",
            name = "Sony Xperia 1 V",
            description = "4K OLED display, headphone jack, 512GB, content creator's choice",
            price = 1299.99,
            imageUrl = "https://images.unsplash.com/photo-1517765371796-5bd3a7d30a29?w=400",
            category = "electronics",
            rating = 4.6f,
            reviews = 540,
            discount = 15
        ),
        Product(
            id = "phone15",
            name = "Honor Magic 6 Pro",
            description = "Falcon camera system, 5600mAh battery, 512GB, AI features",
            price = 999.99,
            imageUrl = "https://images.unsplash.com/photo-1585060544812-6b45742d762f?w=400",
            category = "electronics",
            rating = 4.5f,
            reviews = 430,
            discount = 18
        ),

        // ========== CAMERAS (Electronics Category) ==========
        Product(
            id = "cam1",
            name = "Canon EOS R5",
            description = "45MP full-frame mirrorless, 8K video, IBIS, professional-grade",
            price = 3899.99,
            imageUrl = "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=400",
            category = "electronics",
            rating = 4.9f,
            reviews = 1200,
            discount = 10
        ),
        Product(
            id = "cam2",
            name = "Sony A7 IV",
            description = "33MP full-frame, 4K 60fps, hybrid autofocus, content creator's favorite",
            price = 2499.99,
            imageUrl = "https://images.unsplash.com/photo-1606980707103-0b1e3f6c6e5e?w=400",
            category = "electronics",
            rating = 4.8f,
            reviews = 1850,
            discount = 8
        ),
        Product(
            id = "cam3",
            name = "Nikon Z9",
            description = "45.7MP stacked sensor, 8K video, flagship mirrorless, no blackout",
            price = 5499.99,
            imageUrl = "https://images.unsplash.com/photo-1606800052052-1e1b0b6e0c50?w=400",
            category = "electronics",
            rating = 4.9f,
            reviews = 980,
            discount = 5
        ),
        Product(
            id = "cam4",
            name = "Fujifilm X-T5",
            description = "40MP APS-C, film simulations, retro design, 6.2K video",
            price = 1699.99,
            imageUrl = "https://images.unsplash.com/photo-1607462109225-6b64ae2dd3cb?w=400",
            category = "electronics",
            rating = 4.7f,
            reviews = 760,
            discount = 12
        ),
        Product(
            id = "cam5",
            name = "Panasonic Lumix GH6",
            description = "25.2MP M43 sensor, unlimited recording, V-Log, video powerhouse",
            price = 2199.99,
            imageUrl = "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=400",
            category = "electronics",
            rating = 4.8f,
            reviews = 650,
            discount = 15
        ),
        Product(
            id = "cam6",
            name = "GoPro Hero 12 Black",
            description = "5.3K video, HyperSmooth 6.0, waterproof, action camera leader",
            price = 399.99,
            imageUrl = "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=400",
            category = "electronics",
            rating = 4.7f,
            reviews = 2340,
            discount = 20
        ),
        Product(
            id = "cam7",
            name = "DJI Osmo Pocket 3",
            description = "Gimbal camera, 4K 120fps, AI tracking, pocket-sized",
            price = 519.99,
            imageUrl = "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=400",
            category = "electronics",
            rating = 4.6f,
            reviews = 1120,
            discount = 10
        ),
        Product(
            id = "cam8",
            name = "Leica Q3",
            description = "60MP full-frame, fixed 28mm f/1.7 lens, luxury compact",
            price = 5995.99,
            imageUrl = "https://images.unsplash.com/photo-1606800052052-1e1b0b6e0c50?w=400",
            category = "electronics",
            rating = 4.9f,
            reviews = 340,
            discount = 0
        ),
        Product(
            id = "cam9",
            name = "Canon PowerShot G7 X Mark III",
            description = "20.1MP 1-inch sensor, 4K video, vlogging camera, flip screen",
            price = 749.99,
            imageUrl = "https://images.unsplash.com/photo-1607462109225-6b64ae2dd3cb?w=400",
            category = "electronics",
            rating = 4.5f,
            reviews = 1560,
            discount = 18
        ),
        Product(
            id = "cam10",
            name = "Olympus OM-1",
            description = "20MP Micro Four Thirds, AI subject detection, weather-sealed",
            price = 2199.99,
            imageUrl = "https://images.unsplash.com/photo-1606980707103-0b1e3f6c6e5e?w=400",
            category = "electronics",
            rating = 4.6f,
            reviews = 450,
            discount = 12
        ),
        Product(
            id = "cam11",
            name = "Insta360 X3",
            description = "360° camera, 5.7K video, invisible selfie stick, waterproof",
            price = 449.99,
            imageUrl = "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=400",
            category = "electronics",
            rating = 4.7f,
            reviews = 890,
            discount = 15
        ),
        Product(
            id = "cam12",
            name = "Ricoh GR IIIx",
            description = "24MP APS-C, 40mm f/2.8, street photography compact",
            price = 1099.99,
            imageUrl = "https://images.unsplash.com/photo-1606800052052-1e1b0b6e0c50?w=400",
            category = "electronics",
            rating = 4.6f,
            reviews = 320,
            discount = 10
        ),

        // ========== FASHION - MEN'S CLOTHING ==========
        Product(
            id = "fashion1",
            name = "Premium Leather Jacket",
            description = "Genuine lambskin leather, quilted lining, YKK zippers, timeless design",
            price = 599.99,
            imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400",
            category = "fashion",
            rating = 4.8f,
            reviews = 890,
            discount = 25
        ),
        Product(
            id = "fashion2",
            name = "Classic Slim Fit Suit",
            description = "Wool blend, navy blue, modern fit, includes jacket and pants",
            price = 449.99,
            imageUrl = "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=400",
            category = "fashion",
            rating = 4.7f,
            reviews = 560,
            discount = 20
        ),
        Product(
            id = "fashion3",
            name = "Designer Polo Shirt Pack (3)",
            description = "100% cotton pique, assorted colors, breathable, perfect fit",
            price = 89.99,
            imageUrl = "https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?w=400",
            category = "fashion",
            rating = 4.5f,
            reviews = 1230,
            discount = 30
        ),
        Product(
            id = "fashion4",
            name = "Raw Denim Jeans",
            description = "Selvedge denim, straight fit, Japanese fabric, ages beautifully",
            price = 129.99,
            imageUrl = "https://images.unsplash.com/photo-1542272604-787c3835535d?w=400",
            category = "fashion",
            rating = 4.6f,
            reviews = 780,
            discount = 15
        ),
        Product(
            id = "fashion5",
            name = "Wool Overcoat",
            description = "Double-breasted, charcoal gray, Italian wool, winter essential",
            price = 379.99,
            imageUrl = "https://images.unsplash.com/photo-1539533113208-c47fa864d8bc?w=400",
            category = "fashion",
            rating = 4.8f,
            reviews = 340,
            discount = 18
        ),

        // ========== FASHION - WOMEN'S CLOTHING ==========
        Product(
            id = "fashion6",
            name = "Elegant Evening Dress",
            description = "Silk blend, A-line silhouette, midnight blue, perfect for formal events",
            price = 299.99,
            imageUrl = "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400",
            category = "fashion",
            rating = 4.9f,
            reviews = 670,
            discount = 22
        ),
        Product(
            id = "fashion7",
            name = "Cashmere Sweater",
            description = "100% pure cashmere, crew neck, ultra-soft, multiple colors",
            price = 189.99,
            imageUrl = "https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=400",
            category = "fashion",
            rating = 4.8f,
            reviews = 920,
            discount = 20
        ),
        Product(
            id = "fashion8",
            name = "High-Waisted Skinny Jeans",
            description = "Stretch denim, black, figure-flattering, comfortable all day",
            price = 79.99,
            imageUrl = "https://images.unsplash.com/photo-1587829741301-d7fe6bc767ec?w=400",
            category = "fashion",
            rating = 4.6f,
            reviews = 1450,
            discount = 25
        ),
        Product(
            id = "fashion9",
            name = "Silk Blouse",
            description = "100% silk, cream color, button-front, professional elegance",
            price = 129.99,
            imageUrl = "https://images.unsplash.com/photo-1618932260643-eee4a2f652a6?w=400",
            category = "fashion",
            rating = 4.7f,
            reviews = 540,
            discount = 15
        ),
        Product(
            id = "fashion10",
            name = "Maxi Floral Dress",
            description = "Flowing fabric, summer print, adjustable straps, beach-ready",
            price = 89.99,
            imageUrl = "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=400",
            category = "fashion",
            rating = 4.5f,
            reviews = 890,
            discount = 30
        ),

        // ========== FASHION - SHOES ==========
        Product(
            id = "fashion11",
            name = "Premium Running Shoes",
            description = "Nike Air cushioning, breathable mesh, perfect for marathons",
            price = 159.99,
            imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400",
            category = "fashion",
            rating = 4.8f,
            reviews = 2340,
            discount = 15
        ),
        Product(
            id = "fashion12",
            name = "Leather Chelsea Boots",
            description = "Goodyear welted, brown leather, Chelsea style, all-season",
            price = 249.99,
            imageUrl = "https://images.unsplash.com/photo-1608256246200-53e635b5b65f?w=400",
            category = "fashion",
            rating = 4.7f,
            reviews = 560,
            discount = 20
        ),
        Product(
            id = "fashion13",
            name = "White Sneakers Classic",
            description = "Minimalist design, leather upper, versatile, everyday wear",
            price = 99.99,
            imageUrl = "https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?w=400",
            category = "fashion",
            rating = 4.6f,
            reviews = 1670,
            discount = 25
        ),
        Product(
            id = "fashion14",
            name = "Heeled Ankle Boots",
            description = "Block heel, black suede, side zip, comfortable height",
            price = 139.99,
            imageUrl = "https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=400",
            category = "fashion",
            rating = 4.5f,
            reviews = 780,
            discount = 18
        ),
        Product(
            id = "fashion15",
            name = "Canvas Slip-On Sneakers",
            description = "Casual comfort, multiple colors, breathable, easy on/off",
            price = 49.99,
            imageUrl = "https://images.unsplash.com/photo-1525966222134-c4a3942d3acf?w=400",
            category = "fashion",
            rating = 4.4f,
            reviews = 1120,
            discount = 30
        ),

        // ========== FASHION - ACCESSORIES ==========
        Product(
            id = "fashion16",
            name = "Luxury Leather Wallet",
            description = "Full-grain leather, RFID blocking, 8 card slots, slim design",
            price = 79.99,
            imageUrl = "https://images.unsplash.com/photo-1627123424574-724758594e93?w=400",
            category = "fashion",
            rating = 4.7f,
            reviews = 890,
            discount = 20
        ),
        Product(
            id = "fashion17",
            name = "Designer Sunglasses",
            description = "Polarized lenses, UV400 protection, acetate frames, Italian design",
            price = 199.99,
            imageUrl = "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=400",
            category = "fashion",
            rating = 4.8f,
            reviews = 670,
            discount = 15
        ),
        Product(
            id = "fashion18",
            name = "Silk Tie Collection (3)",
            description = "100% silk, classic patterns, handmade, business essential",
            price = 89.99,
            imageUrl = "https://images.unsplash.com/photo-1617127365659-c47fa864d8bc?w=400",
            category = "fashion",
            rating = 4.6f,
            reviews = 450,
            discount = 25
        ),

        // ========== OTHER ELECTRONICS ==========
        Product(
            id = "elec1",
            name = "Premium Wireless Headphones",
            description = "ANC technology, 30-hour battery, premium audio quality",
            price = 299.99,
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400",
            category = "electronics",
            rating = 4.7f,
            reviews = 1250,
            discount = 15
        ),
        Product(
            id = "elec2",
            name = "Smart Watch Series 9",
            description = "Fitness tracking, ECG, always-on display, water-resistant",
            price = 399.99,
            imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400",
            category = "electronics",
            rating = 4.6f,
            reviews = 980,
            discount = 20
        ),
        Product(
            id = "elec3",
            name = "Mechanical Gaming Keyboard",
            description = "Cherry MX switches, RGB lighting, aluminum frame, wrist rest",
            price = 179.99,
            imageUrl = "https://images.unsplash.com/photo-1587829741301-d7fe6bc767ec?w=400",
            category = "electronics",
            rating = 4.8f,
            reviews = 1560,
            discount = 12
        ),
        Product(
            id = "elec4",
            name = "4K Gaming Monitor 32\"",
            description = "144Hz, HDR10, 1ms response time, adaptive sync",
            price = 549.99,
            imageUrl = "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=400",
            category = "electronics",
            rating = 4.7f,
            reviews = 890,
            discount = 18
        ),

        // ========== HOME & LIVING ==========
        Product(
            id = "home1",
            name = "Smart LED Bulbs (4-Pack)",
            description = "Color-changing, voice control, energy-efficient, app control",
            price = 49.99,
            imageUrl = "https://images.unsplash.com/photo-1550985616-10810253b84d?w=400",
            category = "home",
            rating = 4.3f,
            reviews = 720,
            discount = 10
        ),
        Product(
            id = "home2",
            name = "Egyptian Cotton Sheet Set",
            description = "1000 thread count, king size, ultra-soft, wrinkle-resistant",
            price = 129.99,
            imageUrl = "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=400",
            category = "home",
            rating = 4.8f,
            reviews = 560,
            discount = 25
        ),
        Product(
            id = "home3",
            name = "Robot Vacuum Cleaner",
            description = "Smart mapping, 2-in-1 vacuum & mop, app control, auto-charging",
            price = 399.99,
            imageUrl = "https://images.unsplash.com/photo-1558317374-067fb44f0c7d?w=400",
            category = "home",
            rating = 4.6f,
            reviews = 1230,
            discount = 20
        ),

        // ========== SPORTS ==========
        Product(
            id = "sport1",
            name = "Premium Yoga Mat",
            description = "6mm thick, non-slip surface, eco-friendly, includes strap",
            price = 49.99,
            imageUrl = "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400",
            category = "sports",
            rating = 4.6f,
            reviews = 890,
            discount = 15
        ),
        Product(
            id = "sport2",
            name = "Adjustable Dumbbell Set",
            description = "5-52.5 lbs per hand, space-saving design, quick adjust",
            price = 349.99,
            imageUrl = "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400",
            category = "sports",
            rating = 4.8f,
            reviews = 670,
            discount = 12
        ),
        Product(
            id = "sport3",
            name = "Resistance Bands Set",
            description = "5 bands, door anchor, handles, workout guide included",
            price = 29.99,
            imageUrl = "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400",
            category = "sports",
            rating = 4.5f,
            reviews = 1450,
            discount = 20
        ),

        // ========== BOOKS ==========
        Product(
            id = "book1",
            name = "Complete Programming Guide 2024",
            description = "Learn Python, JavaScript, and more. 1200 pages of expert knowledge",
            price = 59.99,
            imageUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=400",
            category = "books",
            rating = 4.9f,
            reviews = 2400,
            discount = 10
        ),
        Product(
            id = "book2",
            name = "The Art of Modern Design",
            description = "Coffee table book, 300+ illustrations, design principles",
            price = 69.99,
            imageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400",
            category = "books",
            rating = 4.7f,
            reviews = 580,
            discount = 15
        ),

        // ========== BEAUTY ==========
        Product(
            id = "beauty1",
            name = "Organic Skincare Set",
            description = "Natural ingredients, cleanser + toner + moisturizer, cruelty-free",
            price = 89.99,
            imageUrl = "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
            category = "beauty",
            rating = 4.7f,
            reviews = 1120,
            discount = 20
        ),
        Product(
            id = "beauty2",
            name = "Professional Hair Dryer",
            description = "Ionic technology, 3 heat settings, cold shot button, salon quality",
            price = 149.99,
            imageUrl = "https://images.unsplash.com/photo-1522338242992-e1a54906a8da?w=400",
            category = "beauty",
            rating = 4.6f,
            reviews = 890,
            discount = 18
        )
    )

    private val _products = MutableStateFlow(allProducts)
    val products: Flow<List<Product>> = _products

    fun getProductById(id: String): Product? {
        return allProducts.find { it.id == id }
    }

    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return products.map { list ->
            list.filter { it.category == category }
        }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return products.map { list ->
            if (query.isBlank()) {
                list
            } else {
                list.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true) ||
                            it.category.contains(query, ignoreCase = true)
                }
            }
        }
    }

    fun getFeaturedProducts(): Flow<List<Product>> {
        return products.map { list ->
            listOf(
                list.find { it.id == "phone1" },    // iPhone 15 Pro Max
                list.find { it.id == "fashion6" },  // Evening Dress
                list.find { it.id == "elec4" },     // 4K Gaming Monitor
                list.find { it.id == "fashion11" }, // Running Shoes
                list.find { it.id == "cam2" },      // Sony A7 IV Camera
                list.find { it.id == "elec1" },     // Wireless Headphones
                list.find { it.id == "fashion1" },  // Leather Jacket
                list.find { it.id == "sport2" },    // Adjustable Dumbbells
                list.find { it.id == "cam6" },      // GoPro Hero 12
                list.find { it.id == "home3" }      // Robot Vacuum
            ).filterNotNull()
        }
    }

    fun getTopRatedProducts(): Flow<List<Product>> {
        return products.map { list ->
            list.sortedByDescending { it.rating }.take(6)
        }
    }
}
