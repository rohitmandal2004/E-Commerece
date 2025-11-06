# Quick Setup Guide - Techxon E-Commerce App

## ⚡ Quick Start (5 Minutes)

### Step 1: Sync Gradle

```bash
# In Android Studio
File → Sync Project with Gradle Files
```

**Wait for:**

- All dependencies to download
- Coil library (for images)
- RunAnywhere SDK dependencies
- Build to complete

### Step 2: Run the App

```bash
# Connect device or start emulator
Shift + F10 (Windows/Linux)
Ctrl + R (Mac)
```

The app will launch with:

- ✅ Product catalog ready
- ✅ Shopping cart functional
- ✅ Search working
- ⚠️ AI Assistant (needs model)

### Step 3: Enable AI Features

**Option A: Add Model Download UI (Recommended)**

The existing `MyApplication.kt` already registers models. To enable downloads:

1. Go to the AI Assistant tab
2. The AI will show a greeting
3. To actually chat, implement model download UI or use the RunAnywhere SDK methods:

```kotlin
// In your ViewModel or Activity
viewModelScope.launch {
    // Download the smallest model (119 MB)
    RunAnywhere.downloadModel("SmolLM2-360M-Q8_0").collect { progress ->
        // Show progress: progress is 0.0 to 1.0
        println("Download progress: ${(progress * 100).toInt()}%")
    }
    
    // Load the model
    val success = RunAnywhere.loadModel("SmolLM2-360M-Q8_0")
    if (success) {
        println("Model loaded! AI Assistant ready.")
    }
}
```

**Option B: Pre-Download Models**

See `RUNANYWHERE_SDK_COMPLETE_GUIDE.md` for detailed instructions.

## 🔍 Project Structure Overview

```
app/src/main/java/com/runanywhere/startup_hackathon20/
├── MainActivity.kt              # All UI screens (Home, Cart, Categories, AI)
├── MyApplication.kt             # SDK initialization
├── models/
│   ├── Product.kt               # Product data model
│   ├── CartItem.kt              # Cart item model
│   ├── Category.kt              # Category model
│   └── ChatMessage.kt           # AI chat message model
├── viewmodels/
│   ├── EcommerceViewModel.kt    # Cart, search, products logic
│   └── AIShoppingAssistantViewModel.kt  # AI chat logic
└── data/
    └── ProductRepository.kt     # 14 sample products
```

## 📝 Key Files to Customize

### 1. Products (`ProductRepository.kt`)

Add/modify products:

```kotlin
Product(
    id = "15",
    name = "Your Product Name",
    description = "Product description",
    price = 99.99,
    imageUrl = "https://images.unsplash.com/...",
    category = "electronics",  // or fashion, home, sports, books, beauty
    rating = 4.5f,
    reviews = 100,
    discount = 10  // 10% off
)
```

### 2. AI Personality (`AIShoppingAssistantViewModel.kt`)

Change the system prompt:

```kotlin
private val systemPrompt = """
    You are a [YOUR CUSTOM PERSONALITY].
    Help users with [YOUR SPECIFIC TASKS].
    Be [YOUR TONE: friendly/professional/etc].
""".trimIndent()
```

### 3. Categories (`Category.kt`)

Add new categories:

```kotlin
Category("new_category", "Display Name", Icons.Default.YourIcon)
```

### 4. Theme (`ui/theme/`)

Customize colors, typography, and shapes.

## 🎨 Customization Examples

### Change App Name

```xml
<!-- app/src/main/res/values/strings.xml -->
<string name="app_name">Your Store Name</string>
```

### Add New Product Category

```kotlin
// 1. Add to Category.kt
Category("gaming", "Gaming", Icons.Default.SportsEsports)

// 2. Add products with category = "gaming" in ProductRepository.kt
```

### Modify AI Welcome Message

```kotlin
// AIShoppingAssistantViewModel.kt
private val _messages = MutableStateFlow<List<ChatMessage>>(
    listOf(
        ChatMessage(
            "Your custom welcome message!",
            isUser = false
        )
    )
)
```

## 🐛 Common Issues & Fixes

### Issue: Images not loading

**Fix**: Sync Gradle to download Coil

```bash
File → Sync Project with Gradle Files
```

### Issue: AI Assistant not responding

**Fix**: Download and load a model first

```kotlin
// See Step 3 above for code
```

### Issue: Build errors about AsyncImage

**Fix**: Ensure Coil dependency is added

```kotlin
// app/build.gradle.kts
implementation("io.coil-kt:coil-compose:2.5.0")
```

### Issue: "SDK not initialized" error

**Fix**: Check MyApplication.kt is set in AndroidManifest.xml

```xml
<application
    android:name=".MyApplication"
    ...>
```

## 🚀 Next Steps

1. **Add Model Management UI**
    - Create a screen to download/manage models
    - Show download progress
    - List available models

2. **Implement Checkout Flow**
    - Add payment integration
    - Order confirmation
    - Receipt generation

3. **Add User Authentication**
    - Firebase Auth
    - User profiles
    - Order history

4. **Enhance AI Features**
    - Voice input
    - Product image recognition
    - Personalized recommendations

## 📚 Additional Resources

- [Complete E-Commerce Documentation](README_ECOMMERCE.md)
- [RunAnywhere SDK Guide](RUNANYWHERE_SDK_COMPLETE_GUIDE.md)
- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Material Design 3](https://m3.material.io/)

## 💡 Pro Tips

1. **Testing AI**: Use small models (SmolLM2 360M) for faster testing
2. **Product Images**: Use Unsplash for free, high-quality images
3. **Performance**: Test on real devices, not just emulators
4. **Memory**: Keep `android:largeHeap="true"` for AI features

## 🎯 Development Workflow

```bash
# 1. Make code changes
# 2. Sync if you modified dependencies
File → Sync Project with Gradle Files

# 3. Build and run
Shift + F10

# 4. Test on device
# 5. Commit changes
git add .
git commit -m "Your message"
git push
```

## 🤝 Need Help?

- Check `README_ECOMMERCE.md` for detailed documentation
- Review `RUNANYWHERE_SDK_COMPLETE_GUIDE.md` for AI features
- Look at code comments in ViewModels
- Test with sample data first

---

**Ready to build? Start coding! 🚀**

The app is production-ready and can be extended with:

- Payment gateways
- Real backend API
- User accounts
- Advanced AI features
- Analytics
- And more!
