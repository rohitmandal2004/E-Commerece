package com.runanywhere.startup_hackathon20.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.startup_hackathon20.data.ProductRepository
import com.runanywhere.startup_hackathon20.models.ChatMessage
import com.runanywhere.startup_hackathon20.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log

class AIShoppingAssistantViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                "👋 Hi! I'm Pookie, your AI Shopping Assistant. I can help you find products, answer questions, compare items, and provide recommendations. How can I assist you today?",
                isUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _downloadProgress = MutableStateFlow<Float?>(null)
    val downloadProgress: StateFlow<Float?> = _downloadProgress.asStateFlow()

    private val systemPrompt = """
        You are Pookie, an AI Shopping Assistant for an e-commerce store called Techxon. You help customers find products, answer questions, and provide recommendations.
        
        Available product categories:
        - Electronics (headphones, watches, cameras)
        - Fashion (jackets, jeans, sneakers)
        - Home & Living (LED bulbs, bed sheets)
        - Sports (yoga mats, dumbbells)
        - Books (programming guides, design books)
        - Beauty (skincare sets, hair dryers)
        
        Your personality:
        - Friendly and helpful
        - Professional but conversational
        - Provide specific product recommendations when possible
        - Ask clarifying questions when needed
        - Be concise but informative
        
        Remember to:
        - Suggest products based on user needs
        - Mention price ranges when relevant
        - Highlight deals and discounts
        - Ask about budget preferences
        - Recommend related products
    """.trimIndent()

    init {
        // Check if model is available and download if needed
        viewModelScope.launch {
            try {
                ensureModelIsReady()
            } catch (e: Exception) {
                Log.e("AIViewModel", "Model initialization error: ${e.message}")
            }
        }
    }

    private suspend fun ensureModelIsReady() {
        try {
            // Wait a bit for SDK to initialize
            kotlinx.coroutines.delay(1000)

            val models = listAvailableModels()
            Log.d("AIViewModel", "Available models: ${models.size}")
            
            if (models.isEmpty()) {
                _messages.value = _messages.value + ChatMessage(
                    "⚠️ No AI models are registered. Please check your app configuration.",
                    isUser = false
                )
                return
            }

            // Get the first available model and check if it's downloaded
            val model = models.first()
            Log.d("AIViewModel", "Using model: ${model.name}, Downloaded: ${model.isDownloaded}")

            if (!model.isDownloaded) {
                _messages.value = _messages.value + ChatMessage(
                    "🔄 Downloading AI model (${model.name}). This may take a few minutes...",
                    isUser = false
                )
                
                // Download the model
                RunAnywhere.downloadModel(model.id).collect { progress ->
                    _downloadProgress.value = progress
                    Log.d("AIViewModel", "Download progress: ${(progress * 100).toInt()}%")
                }
                
                _downloadProgress.value = null

                // Load the model
                val loaded = RunAnywhere.loadModel(model.id)
                Log.d("AIViewModel", "Model loaded: $loaded")

                _messages.value = _messages.value.dropLast(1) + ChatMessage(
                    "✅ AI assistant is ready! How can I help you today?",
                    isUser = false
                )
            } else {
                // Model is already downloaded, just load it
                val loaded = RunAnywhere.loadModel(model.id)
                Log.d("AIViewModel", "Model loaded: $loaded")
            }
        } catch (e: Exception) {
            Log.e("AIViewModel", "Error ensuring model ready: ${e.message}", e)
            _messages.value = _messages.value + ChatMessage(
                "⚠️ There was an issue preparing the AI assistant: ${e.message}. You can still browse products!",
                isUser = false
            )
        }
    }

    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank()) return

        // Add user message
        _messages.value += ChatMessage(userMessage, isUser = true)

        viewModelScope.launch {
            _isLoading.value = true

            try {
                // Check if models are available
                val availableModels = listAvailableModels()
                if (availableModels.isEmpty()) {
                    _messages.value += ChatMessage(
                        "I'm still setting up. Please wait a moment and try again.",
                        isUser = false
                    )
                    _isLoading.value = false
                    return@launch
                }

                // Check if a model is downloaded
                val downloadedModel = availableModels.firstOrNull { it.isDownloaded }
                if (downloadedModel == null) {
                    _messages.value += ChatMessage(
                        "Please wait for the AI model to finish downloading first. Check the welcome message for progress.",
                        isUser = false
                    )
                    _isLoading.value = false
                    return@launch
                }

                // Build context with product information
                val productContext = buildProductContext()

                // Build conversation history (only include last 4 exchanges to keep context manageable)
                val conversationHistory = _messages.value
                    .filter { it.text.length < 500 } // Skip very long messages to save tokens
                    .takeLast(8)
                    .joinToString("\n") { msg ->
                        if (msg.isUser) "User: ${msg.text}" else "Assistant: ${msg.text}"
                    }

                // Create enhanced prompt
                val fullPrompt = """
                    $systemPrompt
                    
                    Current Products Available:
                    $productContext
                    
                    Recent Conversation:
                    $conversationHistory
                    
                    User: $userMessage
                    
                    Assistant:
                """.trimIndent()

                Log.d("AIViewModel", "Sending prompt to AI model")

                // Generate response using RunAnywhere SDK
                var assistantResponse = ""
                var hasReceivedTokens = false

                RunAnywhere.generateStream(fullPrompt).collect { token ->
                    hasReceivedTokens = true
                    assistantResponse += token

                    // Update the last message or create new one
                    val currentMessages = _messages.value.toMutableList()
                    val lastMessage = currentMessages.lastOrNull()
                    
                    if (lastMessage != null && !lastMessage.isUser && assistantResponse.isNotEmpty()) {
                        // Update existing assistant message
                        currentMessages[currentMessages.lastIndex] =
                            ChatMessage(assistantResponse.trim(), isUser = false)
                    } else if (assistantResponse.isNotEmpty()) {
                        // Create new assistant message
                        currentMessages.add(ChatMessage(assistantResponse.trim(), isUser = false))
                    }
                    _messages.value = currentMessages
                }

                if (!hasReceivedTokens) {
                    _messages.value += ChatMessage(
                        "I'm having trouble generating a response. Please try again.",
                        isUser = false
                    )
                }

                Log.d("AIViewModel", "Response generated successfully")

            } catch (e: Exception) {
                Log.e("AIViewModel", "Error generating response: ${e.message}", e)
                // Handle error with more helpful message
                _messages.value += ChatMessage(
                    "I apologize, I encountered an error: ${e.message}. Let me try to help you differently! You can browse our products or ask me specific questions about categories, prices, or recommendations.",
                    isUser = false
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun buildProductContext(): String {
        val products = ProductRepository.products.first()
        return products.take(10).joinToString("\n") { product ->
            val discountInfo = if (product.discount > 0) " (${product.discount}% off!)" else ""
            "- ${product.name}: ₹${product.discountedPrice}$discountInfo - ${product.category}"
        }
    }

    fun getProductRecommendations(query: String, callback: (List<Product>) -> Unit) {
        viewModelScope.launch {
            try {
                val products = ProductRepository.searchProducts(query).first()
                callback(products.take(5))
            } catch (e: Exception) {
                callback(emptyList())
            }
        }
    }

    fun suggestQuestions() {
        val suggestions = listOf(
            "What electronics do you have on sale?",
            "I need running shoes under ₹5000",
            "Can you recommend a gift for someone who loves technology?",
            "What are your best-selling products?",
            "I'm looking for home office equipment"
        )

        _messages.value += ChatMessage(
            "Here are some questions I can help with:\n\n${suggestions.joinToString("\n") { "• $it" }}",
            isUser = false
        )
    }

    fun clearChat() {
        _messages.value = listOf(
            ChatMessage(
                "👋 Hi! I'm Pookie, your AI Shopping Assistant. How can I help you today?",
                isUser = false
            )
        )
    }
}
