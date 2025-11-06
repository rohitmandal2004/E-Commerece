# 🚀 Techxon - Quick Reference Card

## ⚡ 30-Second Overview

**Techxon** is a complete Android e-commerce app with RunAnywhere SDK integration for on-device AI.

- **14 Products** across 6 categories
- **Shopping Cart** with full management
- **AI Assistant** powered by RunAnywhere
- **Modern UI** with Material Design 3
- **Production Ready** with comprehensive error handling

## 📱 App Structure

```
Home → Browse products, search, featured deals
Categories → Filter by category
Cart → Manage items, quantities, checkout
AI → Chat with shopping assistant
```

## 🗂️ Key Files

| File | Purpose |
|------|---------|
| `MainActivity.kt` | All UI screens & components |
| `MyApplication.kt` | SDK initialization |
| `EcommerceViewModel.kt` | Cart, search, products |
| `AIShoppingAssistantViewModel.kt` | AI chat logic |
| `ProductRepository.kt` | Product data |

## 🔧 Quick Commands

### Build & Run

```bash
# Sync dependencies
File → Sync Project with Gradle Files

# Run app
Shift + F10 (Windows/Linux)
Ctrl + R (Mac)

# Clean build
Build → Clean Project
Build → Rebuild Project
```

### Common Tasks

**Add New Product:**

```kotlin
// ProductRepository.kt
Product(
    id = "15",
    name = "New Product",
    description = "Description",
    price = 99.99,
    imageUrl = "https://...",
    category = "electronics",
    rating = 4.5f,
    reviews = 100,
    discount = 10
)
```

**Change AI Personality:**

```kotlin
// AIShoppingAssistantViewModel.kt
private val systemPrompt = """
    Your custom AI personality here
""".trimIndent()
```

**Add Category:**

```kotlin
// Category.kt
Category("new_cat", "Display Name", Icons.Default.Icon)
```

## 🎨 UI Components

| Screen | Composable |
|--------|-----------|
| Home | `HomeScreen()` |
| Categories | `CategoriesScreen()` |
| Cart | `CartScreen()` |
| AI Chat | `AIAssistantScreen()` |
| Product Card | `ProductCard()` |
| Cart Item | `CartItemCard()` |

## 🤖 RunAnywhere SDK Usage

### Initialize (MyApplication.kt)

```kotlin
RunAnywhere.initialize(
    context = this,
    apiKey = "dev",
    environment = SDKEnvironment.DEVELOPMENT
)
LlamaCppServiceProvider.register()
```

### Download Model

```kotlin
RunAnywhere.downloadModel(modelId).collect { progress ->
    // progress: 0.0 to 1.0
}
```

### Load Model

```kotlin
val success = RunAnywhere.loadModel(modelId)
```

### Generate Response

```kotlin
// Streaming
RunAnywhere.generateStream(prompt).collect { token ->
    response += token
}

// Non-streaming
val response = RunAnywhere.generate(prompt)
```

## 📊 Data Flow

```
User Action → ViewModel → Repository → UI Update
          ↓
     StateFlow/Flow
```

## 🎯 StateFlow Patterns

### Reading State

```kotlin
val products by viewModel.displayedProducts.collectAsState()
```

### Updating State

```kotlin
private val _state = MutableStateFlow(initialValue)
val state: StateFlow<Type> = _state.asStateFlow()

// Update
_state.value = newValue
```

## 🛠️ Common Customizations

### Change App Name

```xml
<!-- strings.xml -->
<string name="app_name">Your Name</string>
```

### Modify Colors

```kotlin
// ui/theme/Color.kt
val Primary = Color(0xFF...)
```

### Update Welcome Message

```kotlin
// AIShoppingAssistantViewModel.kt
ChatMessage("Your message", isUser = false)
```

## 🐛 Quick Fixes

| Issue | Solution |
|-------|----------|
| Images not loading | Sync Gradle (Coil library) |
| AI not responding | Download & load model |
| Build errors | Clean & Rebuild |
| SDK not initialized | Check MyApplication in manifest |

## 📦 Dependencies

```kotlin
// Core
implementation(libs.androidx.compose.material3)
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Images
implementation("io.coil-kt:coil-compose:2.5.0")

// RunAnywhere SDK
implementation(files("libs/RunAnywhereKotlinSDK-release.aar"))
implementation(files("libs/runanywhere-llm-llamacpp-release.aar"))
```

## 🎨 Material 3 Components Used

- `Scaffold` - App structure
- `NavigationBar` - Bottom nav
- `Card` - Product cards
- `Button` - Actions
- `TextField` - Input
- `LazyColumn/Row` - Lists
- `BadgedBox` - Cart badge
- `FilterChip` - Categories
- `FloatingActionButton` - Send button

## 📱 Screens Flow

```
App Launch
    ↓
MyApplication.onCreate() → SDK Init
    ↓
MainActivity → Compose UI
    ↓
┌─────────────┬──────────────┬────────┬─────────┐
│    Home     │ Categories   │  Cart  │   AI    │
└─────────────┴──────────────┴────────┴─────────┘
```

## 🔍 Search & Filter

```kotlin
// Search
viewModel.updateSearchQuery("headphones")

// Filter by category
viewModel.selectCategory("electronics")

// Clear filter
viewModel.selectCategory(null)
```

## 🛒 Cart Operations

```kotlin
// Add item
viewModel.addToCart(product)

// Update quantity
viewModel.updateCartItemQuantity(productId, newQuantity)

// Remove item
viewModel.removeFromCart(productId)

// Clear cart
viewModel.clearCart()

// Get total
val total by viewModel.cartTotal.collectAsState()
```

## 💬 AI Chat

```kotlin
// Send message
aiViewModel.sendMessage("Your question")

// Clear chat
aiViewModel.clearChat()

// Get messages
val messages by aiViewModel.messages.collectAsState()
```

## 📈 State Properties

### EcommerceViewModel

- `searchQuery` - Current search text
- `selectedCategory` - Active category filter
- `displayedProducts` - Filtered products
- `cartItems` - Items in cart
- `cartTotal` - Total price
- `cartItemCount` - Number of items

### AIShoppingAssistantViewModel

- `messages` - Chat history
- `isLoading` - AI generating response

## 🎯 Product Properties

```kotlin
data class Product(
    val id: String,           // Unique ID
    val name: String,         // Display name
    val description: String,  // Full description
    val price: Double,        // Original price
    val imageUrl: String,     // Image URL
    val category: String,     // Category ID
    val rating: Float,        // 0.0-5.0
    val reviews: Int,         // Review count
    val inStock: Boolean,     // Availability
    val discount: Int         // % discount
)
```

## 🚀 Deployment Checklist

- [ ] Update app name
- [ ] Add real product data
- [ ] Configure AI model
- [ ] Test on device
- [ ] Add payment integration
- [ ] Set up backend
- [ ] Add analytics
- [ ] Test all features
- [ ] Generate signed APK
- [ ] Submit to Play Store

## 📖 Documentation

- `README_ECOMMERCE.md` - Full documentation
- `SETUP_GUIDE.md` - Setup instructions
- `PROJECT_SUMMARY.md` - Technical overview
- `RUNANYWHERE_SDK_COMPLETE_GUIDE.md` - SDK docs
- `QUICK_REFERENCE.md` - This file

## 🔗 Quick Links

**In Code:**

- Products: `ProductRepository.kt:10`
- Cart logic: `EcommerceViewModel.kt:85`
- AI chat: `AIShoppingAssistantViewModel.kt:58`
- Home UI: `MainActivity.kt:110`
- SDK init: `MyApplication.kt:20`

**External:**

- [RunAnywhere SDK](https://github.com/RunanywhereAI/runanywhere-sdks)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material 3](https://m3.material.io/)

## 💡 Pro Tips

1. **Small Models**: Use SmolLM2 360M for testing (119 MB)
2. **Real Device**: Test on real device for accurate performance
3. **Images**: Use Unsplash for free product images
4. **Memory**: Keep `largeHeap="true"` for AI features
5. **Clean Builds**: Clean project if seeing weird errors

## ⌨️ Keyboard Shortcuts

| Action | Shortcut |
|--------|----------|
| Run | `Shift + F10` |
| Build | `Ctrl + F9` |
| Sync | `Ctrl + Alt + Y` |
| Find | `Ctrl + F` |
| Search Files | `Ctrl + Shift + N` |
| Format Code | `Ctrl + Alt + L` |

---

**Quick Start**: Sync Gradle → Run App → Browse → Add to Cart → Chat with AI

**Need Help?** Check `README_ECOMMERCE.md` or `SETUP_GUIDE.md`
