package com.runanywhere.startup_hackathon20.models

import androidx.compose.runtime.Immutable
import java.util.UUID

// ========== SELLER MODELS ==========
@Immutable
data class Seller(
    val id: String,
    val name: String,
    val rating: Float,
    val totalRatings: Int,
    val yearsOnPlatform: Int,
    val positiveRating: Int, // percentage
    val isVerified: Boolean = false,
    val isPlusSeller: Boolean = false,
    val responseTime: String = "Within hours",
    val returnPolicy: String = "7 days return"
)

// ========== PRODUCT Q&A ==========
@Immutable
data class ProductQuestion(
    val id: String,
    val question: String,
    val askedBy: String,
    val askedDate: String,
    val answers: List<ProductAnswer> = emptyList(),
    val upvotes: Int = 0
)

@Immutable
data class ProductAnswer(
    val id: String,
    val answer: String,
    val answeredBy: String,
    val answeredDate: String,
    val upvotes: Int = 0,
    val isSellerAnswer: Boolean = false
)

// ========== SIZE & FIT ==========
@Immutable
data class SizeGuide(
    val sizes: List<SizeInfo>,
    val measurements: List<Measurement>
)

@Immutable
data class SizeInfo(
    val size: String,
    val chest: String,
    val waist: String,
    val length: String
)

@Immutable
data class Measurement(
    val name: String,
    val instruction: String
)

// ========== DELIVERY OPTIONS ==========
@Immutable
data class DeliveryOption(
    val id: String,
    val type: DeliveryType,
    val estimatedDays: String,
    val price: Double,
    val isFree: Boolean = false,
    val description: String
)

enum class DeliveryType {
    STANDARD,
    EXPRESS,
    SAME_DAY,
    SCHEDULED
}

// ========== PAYMENT METHODS ==========
@Immutable
data class PaymentOption(
    val id: String,
    val type: PaymentType,
    val name: String,
    val icon: String,
    val offers: List<PaymentOffer> = emptyList(),
    val isSaved: Boolean = false
)

enum class PaymentType {
    CREDIT_CARD,
    DEBIT_CARD,
    NET_BANKING,
    UPI,
    WALLET,
    COD,
    EMI,
    PAY_LATER,
    GIFT_CARD
}

@Immutable
data class PaymentOffer(
    val title: String,
    val description: String,
    val discount: String,
    val code: String?
)

// ========== EMI OPTIONS ==========
@Immutable
data class EMIOption(
    val bank: String,
    val tenure: Int, // months
    val monthlyPayment: Double,
    val interestRate: Float,
    val totalAmount: Double,
    val processingFee: Double = 0.0
)

// ========== RETURN & EXCHANGE ==========
@Immutable
data class ReturnPolicy(
    val returnable: Boolean,
    val returnWindow: Int, // days
    val exchangeable: Boolean,
    val conditions: List<String>,
    val process: List<String>
)

@Immutable
data class ReturnRequest(
    val id: String,
    val orderId: String,
    val reason: ReturnReason,
    val description: String,
    val status: ReturnStatus,
    val refundAmount: Double,
    val pickupDate: String?
)

enum class ReturnReason {
    WRONG_PRODUCT,
    DEFECTIVE,
    SIZE_ISSUE,
    NOT_AS_DESCRIBED,
    QUALITY_ISSUE,
    CHANGED_MIND,
    OTHER
}

enum class ReturnStatus {
    REQUESTED,
    APPROVED,
    PICKUP_SCHEDULED,
    PICKED_UP,
    REFUND_INITIATED,
    REFUND_COMPLETED,
    REJECTED
}

// ========== SUPER COINS / REWARDS ==========
@Immutable
data class SuperCoin(
    val balance: Int,
    val expiringCoins: Int = 0,
    val expiryDate: String?,
    val earnRate: Int = 2 // coins per 100 spent
)

@Immutable
data class CoinTransaction(
    val id: String,
    val type: CoinTransactionType,
    val amount: Int,
    val description: String,
    val date: String
)

enum class CoinTransactionType {
    EARNED,
    REDEEMED,
    EXPIRED,
    BONUS
}

// ========== FLIPKART PLUS / MEMBERSHIP ==========
@Immutable
data class PlusMembership(
    val isActive: Boolean,
    val expiryDate: String?,
    val benefits: List<PlusBenefit>,
    val coinsEarned: Int = 0
)

@Immutable
data class PlusBenefit(
    val title: String,
    val description: String,
    val icon: String
)

// ========== GIFT CARDS ==========
@Immutable
data class GiftCard(
    val id: String,
    val code: String,
    val balance: Double,
    val expiryDate: String,
    val isActive: Boolean = true
)

// ========== BANK OFFERS ==========
@Immutable
data class BankOffer(
    val id: String,
    val bank: String,
    val title: String,
    val description: String,
    val discount: String,
    val minOrderAmount: Double,
    val maxDiscount: Double,
    val validTill: String,
    val termsAndConditions: List<String>
)

// ========== PRODUCT SPECIFICATIONS ==========
@Immutable
data class ProductSpecification(
    val category: String,
    val specifications: List<SpecificationItem>
)

@Immutable
data class SpecificationItem(
    val name: String,
    val value: String
)

// ========== PRODUCT VARIANTS ==========
@Immutable
data class ProductVariant(
    val id: String,
    val type: VariantType,
    val value: String,
    val available: Boolean = true,
    val priceChange: Double = 0.0
)

enum class VariantType {
    COLOR,
    SIZE,
    STORAGE,
    RAM,
    OTHER
}

// ========== SAVED FOR LATER ==========
@Immutable
data class SavedItem(
    val productId: String,
    val savedDate: Long,
    val priceAtSave: Double
)

// ========== RECENTLY SEARCHED ==========
@Immutable
data class SearchHistory(
    val query: String,
    val timestamp: Long
)

// ========== PRODUCT FILTERS ==========
@Immutable
data class FilterOption(
    val id: String,
    val name: String,
    val type: FilterType,
    val values: List<FilterValue>,
    val isExpanded: Boolean = false
)

@Immutable
data class FilterValue(
    val value: String,
    val count: Int,
    val isSelected: Boolean = false
)

enum class FilterType {
    BRAND,
    PRICE,
    DISCOUNT,
    RATING,
    OFFERS,
    AVAILABILITY,
    SELLER,
    SIZE,
    COLOR,
    CATEGORY
}

// ========== DEALS & OFFERS ==========
@Immutable
data class DealOfTheDay(
    val id: String,
    val productId: String,
    val title: String,
    val discount: Int,
    val originalPrice: Double,
    val dealPrice: Double,
    val startTime: Long,
    val endTime: Long,
    val stockAvailable: Int
)

@Immutable
data class ComboOffer(
    val id: String,
    val products: List<String>, // product IDs
    val title: String,
    val discount: Int,
    val totalPrice: Double,
    val savedAmount: Double
)

// ========== WALLET ==========
@Immutable
data class Wallet(
    val balance: Double,
    val transactions: List<WalletTransaction> = emptyList()
)

@Immutable
data class WalletTransaction(
    val id: String,
    val type: WalletTransactionType,
    val amount: Double,
    val description: String,
    val timestamp: Long,
    val orderId: String?
)

enum class WalletTransactionType {
    CREDIT,
    DEBIT,
    REFUND,
    CASHBACK
}

// ========== SUBSCRIPTION ==========
@Immutable
data class Subscription(
    val id: String,
    val productId: String,
    val frequency: SubscriptionFrequency,
    val nextDeliveryDate: String,
    val isActive: Boolean = true,
    val discount: Int = 0
)

enum class SubscriptionFrequency {
    WEEKLY,
    BIWEEKLY,
    MONTHLY,
    QUARTERLY
}

// ========== STORE LOCATOR ==========
@Immutable
data class Store(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val pincode: String,
    val phone: String,
    val latitude: Double,
    val longitude: Double,
    val openingTime: String,
    val closingTime: String,
    val isOpen: Boolean
)

// ========== CUSTOMER SUPPORT ==========
@Immutable
data class SupportTicket(
    val id: String,
    val orderId: String?,
    val category: SupportCategory,
    val subject: String,
    val description: String,
    val status: TicketStatus,
    val createdDate: String,
    val messages: List<SupportMessage> = emptyList()
)

enum class SupportCategory {
    ORDER_ISSUE,
    PAYMENT_ISSUE,
    DELIVERY_ISSUE,
    PRODUCT_QUALITY,
    RETURN_REFUND,
    ACCOUNT_ISSUE,
    OTHER
}

enum class TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

@Immutable
data class SupportMessage(
    val id: String,
    val message: String,
    val isFromCustomer: Boolean,
    val timestamp: Long
)

// ========== SOCIAL FEATURES ==========
@Immutable
data class ProductReview(
    val id: String,
    val userId: String,
    val userName: String,
    val rating: Float,
    val title: String,
    val review: String,
    val images: List<String> = emptyList(),
    val verified: Boolean = true,
    val helpful: Int = 0,
    val timestamp: Long,
    val replies: List<ReviewReply> = emptyList()
)

@Immutable
data class ReviewReply(
    val id: String,
    val message: String,
    val isFromSeller: Boolean,
    val timestamp: Long
)

// ========== REFERRAL PROGRAM ==========
@Immutable
data class ReferralProgram(
    val referralCode: String,
    val referralsCount: Int,
    val rewardsEarned: Double,
    val referralHistory: List<ReferralHistory> = emptyList()
)

@Immutable
data class ReferralHistory(
    val friendName: String,
    val joinedDate: String,
    val reward: Double,
    val status: ReferralStatus
)

enum class ReferralStatus {
    PENDING,
    COMPLETED,
    EXPIRED
}

// ========== BUDGET TRACKER ==========
@Immutable
data class BudgetTracker(
    val monthlyBudget: Double,
    val spent: Double,
    val remaining: Double,
    val categorySpending: Map<String, Double>
)

// ========== PRODUCT COMPARISON ==========
@Immutable
data class ComparisonTable(
    val products: List<Product>,
    val attributes: List<ComparisonAttribute>
)

@Immutable
data class ComparisonAttribute(
    val name: String,
    val values: List<String> // one per product
)
