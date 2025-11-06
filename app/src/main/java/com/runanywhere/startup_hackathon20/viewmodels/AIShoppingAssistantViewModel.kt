package com.runanywhere.startup_hackathon20.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.startup_hackathon20.data.ProductRepository
import com.runanywhere.startup_hackathon20.models.ChatMessage
import com.runanywhere.startup_hackathon20.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank()) return

        // Add user message
        _messages.value += ChatMessage(userMessage, isUser = true)

        viewModelScope.launch {
            _isLoading.value = true

            try {
                // Build context with product information
                val productContext = buildProductContext()

                // Build conversation history
                val conversationHistory = _messages.value.takeLast(6).joinToString("\n") { msg ->
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

                // Generate response using RunAnywhere SDK
                var assistantResponse = ""

                RunAnywhere.generateStream(fullPrompt).collect { token ->
                    assistantResponse += token

                    // Update the last message or create new one
                    val currentMessages = _messages.value.toMutableList()
                    if (currentMessages.lastOrNull()?.isUser == false && _isLoading.value) {
                        // Update existing assistant message
                        currentMessages[currentMessages.lastIndex] =
                            ChatMessage(assistantResponse, isUser = false)
                    } else {
                        // Create new assistant message
                        currentMessages.add(ChatMessage(assistantResponse, isUser = false))
                    }
                    _messages.value = currentMessages
                }

            } catch (e: Exception) {
                // Handle error
                _messages.value += ChatMessage(
                    "I apologize, but I'm having trouble responding right now. Error: ${e.message}",
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
            "- ${product.name}: $${product.discountedPrice}$discountInfo - ${product.category}"
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
            "I need running shoes under $150",
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
