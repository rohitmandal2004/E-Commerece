# Techxon - E-Commerce App with RunAnywhere SDK

A modern, feature-rich e-commerce Android application powered by on-device AI using the RunAnywhere
SDK.

## 🌟 Features

### 📱 Core E-Commerce Features

- **Product Catalog**: Browse 14+ products across 6 categories
- **Smart Search**: Real-time product search functionality
- **Category Filtering**: Filter products by Electronics, Fashion, Home & Living, Sports, Books, and
  Beauty
- **Shopping Cart**: Add, remove, and manage items with quantity control
- **Pricing & Discounts**: Featured deals with percentage discounts
- **Product Ratings**: Star ratings and review counts for each product
- **High-Quality Images**: Product images loaded via Coil library

### 🤖 AI-Powered Shopping Assistant

- **On-Device AI**: Powered by RunAnywhere SDK for privacy-first assistance
- **Natural Language**: Chat with AI to find products and get recommendations
- **Product Knowledge**: AI understands your entire product catalog
- **Personalized Recommendations**: Suggests products based on your needs
- **Budget-Aware**: AI considers your budget when recommending products
- **Real-Time Streaming**: See AI responses token-by-token
- **Conversation History**: Maintains context across multiple messages

### 🎨 Modern UI/UX

- **Material Design 3**: Latest Material You design system
- **Bottom Navigation**: Easy access to Home, Categories, Cart, and AI Assistant
- **Responsive Layouts**: Grid and list views for different screens
- **Smooth Animations**: Fluid transitions and interactions
- **Badge Notifications**: Cart item count badge
- **Empty States**: Helpful messages when cart is empty

## 📦 Tech Stack

### Android Development

- **Kotlin**: 100% Kotlin codebase
- **Jetpack Compose**: Modern declarative UI
- **Material Design 3**: Latest MD3 components
- **ViewModel**: MVVM architecture
- **StateFlow**: Reactive state management
- **Coroutines**: Async operations

### AI & ML

- **RunAnywhere SDK**: On-device LLM inference
- **LlamaCpp**: 7 optimized ARM64 CPU variants
- **Streaming Generation**: Real-time token streaming
- **Model Management**: Download and load AI models

### Networking & Images

- **Coil**: Async image loading with caching
- **Ktor**: HTTP client for SDK networking
- **OkHttp**: Network layer
- **Retrofit**: REST API client

### Data & Storage

- **Room**: Local database (SDK requirement)
- **Kotlin Flows**: Reactive data streams
- **StateFlow**: UI state management

## 🏗️ Architecture

```
app/
├── data/
│   └── ProductRepository.kt       # Product data management
├── models/
│   ├── Product.kt                 # Product data class
│   ├── CartItem.kt                # Cart item data class
│   ├── Category.kt                # Category data class
│   └── ChatMessage.kt             # AI chat message data class
├── viewmodels/
│   ├── EcommerceViewModel.kt      # Main e-commerce logic
│   └── AIShoppingAssistantViewModel.kt  # AI assistant logic
├── ui/
│   └── theme/                     # Material 3 theme
├── MainActivity.kt                # Main UI composables
└── MyApplication.kt               # App initialization & SDK setup
```

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest version)
- Android SDK API 24+ (Android 7.0+)
- 2GB+ RAM device for AI features
- Internet connection (for initial model download)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Hackss
   ```

2. **Open in Android Studio**
    - Open Android Studio
    - Select "Open an Existing Project"
    - Navigate to the project directory

3. **Sync Gradle**
    - Android Studio will automatically sync Gradle
    - Wait for dependencies to download (including Coil library)

4. **Run the app**
    - Connect an Android device or start an emulator
    - Click "Run" or press Shift+F10

### First Launch

1. **App will initialize the RunAnywhere SDK**
    - This happens automatically in `MyApplication`

2. **To use AI Assistant**:
    - Navigate to the "AI" tab
    - You'll see a welcome message
    - Before chatting, you need to download and load a model

3. **Download an AI Model**:
    - Models need to be downloaded separately
    - See the RunAnywhere SDK documentation for model management
    - Recommended starter model: SmolLM2 360M (119 MB)

## 🛍️ How to Use

### Browse Products

1. **Home Tab**: See featured deals and top-rated products
2. **Search**: Use the search bar to find specific products
3. **Scroll**: Browse through all available products

### Shop by Category

1. **Categories Tab**: View all product categories
2. **Filter**: Tap a category chip to filter products
3. **Browse**: Scroll through category-specific products

### Shopping Cart

1. **Add Items**: Tap "Add to Cart" or "+" button on any product
2. **Manage Quantity**: Use +/- buttons in cart
3. **Remove Items**: Tap the delete icon
4. **Checkout**: Tap "Checkout" button (demo - not implemented)

### AI Shopping Assistant

1. **Open AI Tab**: Tap the star icon in bottom navigation
2. **Ask Questions**: Type any shopping-related question
3. **Example Queries**:
    - "What electronics do you have on sale?"
    - "I need running shoes under $150"
    - "Recommend a gift for a tech lover"
    - "What are your best deals?"
    - "I'm looking for home office equipment"

4. **Get Recommendations**: AI will suggest products based on your needs
5. **Chat Naturally**: Have a conversation about products

## 📊 Product Catalog

### Electronics (3 products)

- Premium Wireless Headphones - $299.99 (15% off)
- Smart Watch Pro - $399.99 (20% off)
- 4K Action Camera - $249.99

### Fashion (3 products)

- Designer Leather Jacket - $499.99 (30% off)
- Classic Denim Jeans - $79.99
- Running Sneakers - $129.99 (10% off)

### Home & Living (2 products)

- Smart LED Bulbs (4-Pack) - $49.99
- Luxury Bed Sheet Set - $89.99 (25% off)

### Sports (2 products)

- Yoga Mat Premium - $39.99
- Adjustable Dumbbells Set - $349.99 (15% off)

### Books (2 products)

- Complete Programming Guide - $49.99
- The Art of Design - $59.99

### Beauty (2 products)

- Organic Skincare Set - $79.99 (20% off)
- Professional Hair Dryer - $129.99

## 🤖 AI Assistant Features

### Capabilities

- **Product Search**: Find products by name, category, or description
- **Price Queries**: Answer questions about pricing and deals
- **Recommendations**: Suggest products based on needs
- **Comparisons**: Compare different products
- **Budget Help**: Find products within price ranges
- **General Knowledge**: Answer shopping-related questions

### Example Conversations

**Finding Products**:

```
You: "Show me wireless headphones"
AI: "We have Premium Wireless Headphones for $299.99, currently 15% off! 
     They feature noise-canceling and 30-hour battery life."
```

**Budget Shopping**:

```
You: "I need something under $100"
AI: "Here are great options under $100:
     - Classic Denim Jeans ($79.99)
     - Luxury Bed Sheet Set ($89.99, 25% off)
     - Smart LED Bulbs ($49.99)
     - Yoga Mat Premium ($39.99)"
```

**Recommendations**:

```
You: "Gift for someone who loves fitness"
AI: "For a fitness enthusiast, I'd recommend:
     1. Adjustable Dumbbells Set ($349.99, 15% off)
     2. Yoga Mat Premium ($39.99)
     3. Running Sneakers ($129.99, 10% off)
     These cover different fitness activities and budgets!"
```

## 🔧 Configuration

### Changing Products

Edit `app/src/main/java/.../data/ProductRepository.kt`:

- Add/remove products from the `allProducts` list
- Each product needs: id, name, description, price, imageUrl, category, rating, reviews

### Customizing Categories

Edit `app/src/main/java/.../models/Category.kt`:

- Modify the `getCategories()` function
- Add new categories with name and icon

### AI Assistant Personality

Edit `AIShoppingAssistantViewModel.kt`:

- Modify the `systemPrompt` to change AI behavior
- Adjust conversation history length (currently 6 messages)
- Customize greeting messages

## 📱 Screenshots

### Home Screen

- Search bar with product search
- Featured deals carousel
- Top-rated products
- Product grid with images, prices, and ratings

### Categories

- Category filter chips
- Filtered product list
- Easy navigation

### Shopping Cart

- Cart items with images
- Quantity controls
- Total price calculation
- Checkout button

### AI Assistant

- Chat interface
- Streaming responses
- Message bubbles
- Input field with send button

## 🔒 Privacy & Security

### On-Device AI

- **No Data Sent to Cloud**: All AI processing happens locally
- **Privacy-First**: Your conversations stay on your device
- **Offline Capable**: AI works without internet (after model download)

### Data Storage

- **Local Only**: Product data and cart stored locally
- **No User Tracking**: No analytics or tracking
- **Secure**: Room database encryption support via SDK

## 🚧 Future Enhancements

### Planned Features

- [ ] User Authentication
- [ ] Order History
- [ ] Product Reviews
- [ ] Wishlist
- [ ] Push Notifications
- [ ] Payment Integration
- [ ] Order Tracking
- [ ] Product Recommendations Engine
- [ ] Voice Shopping Assistant
- [ ] AR Product Preview

### AI Enhancements

- [ ] Multi-turn shopping conversations
- [ ] Product comparison tables
- [ ] Price alerts
- [ ] Personalized recommendations
- [ ] Image-based product search
- [ ] Voice commands

## 🐛 Troubleshooting

### Common Issues

**App crashes on launch**:

- Ensure minSdk is 24 or higher
- Check that `android:largeHeap="true"` is set in manifest
- Verify RunAnywhere SDK AARs are in `app/libs/`

**Images not loading**:

- Sync Gradle to download Coil library
- Check internet connection
- Verify image URLs are accessible

**AI Assistant not responding**:

- Download and load a model first
- Check model is properly loaded
- Ensure sufficient device memory (2GB+ RAM)
- See RunAnywhere SDK troubleshooting guide

**Search not working**:

- Check `EcommerceViewModel.searchProducts()` function
- Verify product data is loaded
- Try restarting the app

## 📄 License

[Add your license here]

## 👥 Contributors

[Add contributors here]

## 🙏 Acknowledgments

- **RunAnywhere AI**: For the amazing on-device AI SDK
- **Material Design**: For the beautiful UI components
- **Coil**: For efficient image loading
- **Jetpack Compose**: For modern Android UI development

## 📞 Support

For issues or questions:

- Open an issue on GitHub
- Check RunAnywhere SDK documentation
- Contact support@example.com

## 🔗 Links

- [RunAnywhere SDK Documentation](RUNANYWHERE_SDK_COMPLETE_GUIDE.md)
- [Android Developer Guide](https://developer.android.com)
- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Material Design 3](https://m3.material.io/)

---

**Made with ❤️ using RunAnywhere SDK**

*Experience AI-powered shopping, right on your device!*
