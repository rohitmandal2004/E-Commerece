package com.runanywhere.startup_hackathon20

import com.runanywhere.startup_hackathon20.R

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.runanywhere.startup_hackathon20.models.*
import com.runanywhere.startup_hackathon20.ui.theme.Startup_hackathon20Theme
import com.runanywhere.startup_hackathon20.viewmodels.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.UUID
import java.text.NumberFormat
import java.util.Locale

// Utility function to format price in Indian currency
fun formatIndianCurrency(amount: Double): String {
    return "₹${String.format("%,.0f", amount)}"
}

// ========== NOTIFICATIONS SCREEN ==========
data class NotificationItem(
    val id: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val body: String,
    val time: String,
    val isUnread: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onBack: () -> Unit
) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Notifications") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No notifications yet",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications) { notification ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (notification.isUnread)
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                else
                                    MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Icon(
                                            notification.icon,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            notification.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            notification.time,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        notification.body,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
}

// ========== FLASH SALES SCREEN ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashSalesScreen(
    products: List<Product>,
    onProductClick: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚡ Flash Sales") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6B35),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product.id) },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                product.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "₹${String.format("%.0f", product.discountedPrice)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF6B35)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                if (product.discount > 0) {
                                    Text(
                                        "₹${String.format("%.0f", product.price)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        textDecoration = TextDecoration.LineThrough,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            if (product.discount > 0) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFFF6B35)
                                ) {
                                    Text(
                                        "${product.discount}% OFF",
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ========== ORDER TRACKING SCREEN ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    order: Order,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Order") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Order ID: ${order.id}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Status: ${order.status.name}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = when (order.status) {
                                OrderStatus.DELIVERED -> Color(0xFF4CAF50)
                                OrderStatus.SHIPPED -> Color(0xFF2196F3)
                                OrderStatus.CANCELLED -> Color(0xFFF44336)
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    "Order Items",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            items(order.items) { orderItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = orderItem.product.imageUrl,
                            contentDescription = orderItem.product.name,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                orderItem.product.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2
                            )
                            Text(
                                "Qty: ${orderItem.quantity}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            "₹${String.format("%.0f", orderItem.price * orderItem.quantity)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ========== FILTER/SORT DIALOG ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortDialog(
    show: Boolean,
    availableCategories: List<Category>,
    selectedCategory: String?,
    sortOptions: List<String>,
    selectedSort: String,
    onCategorySelect: (String?) -> Unit,
    onSortSelect: (String) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Filter & Sort",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Category Filter
                    Text(
                        "Category",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { onCategorySelect(null) },
                                label = { Text("All") }
                            )
                        }
                        items(availableCategories) { category ->
                            FilterChip(
                                selected = selectedCategory == category.id,
                                onClick = { onCategorySelect(category.id) },
                                label = { Text(category.name) }
                            )
                        }
                    }

                    Divider()

                    // Sort Options
                    Text(
                        "Sort By",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        sortOptions.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSortSelect(option) }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedSort == option,
                                    onClick = { onSortSelect(option) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(option)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onApply,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Startup_hackathon20Theme {
                EcommerceApp()
            }
        }
    }
}

 // ========== AUTHENTICATION SCREEN ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(authViewModel: AuthViewModel) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
     val loginError by authViewModel.loginError.collectAsState()

     // Update error message when loginError changes
     LaunchedEffect(loginError) {
         loginError?.let {
             errorMessage = it
         }
     }

     Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(100.dp),
            shadowElevation = 8.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.techxon_logo),
                    contentDescription = "Techxon Logo",
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Techxon",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = if (isLogin) "Welcome Back!" else "Create Account",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Login/Signup Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                if (!isLogin) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!isLogin) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Default.Lock else Icons.Default.Lock,
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        errorMessage = ""
                        if (isLogin) {
                            authViewModel.login(email, password)
                        } else {
                            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                                errorMessage = "Please fill all fields"
                            } else {
                                authViewModel.signup(name, email, phone, password)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isLogin) "Sign In" else "Sign Up",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        isLogin = !isLogin
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Sign In"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Demo credentials hint
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Demo Account",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Email: demo@techxon.com\nPassword: demo123",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// ========== PROFILE SCREEN ==========
@Composable
fun ProfileScreen(authViewModel: AuthViewModel, ecommerceViewModel: EcommerceViewModel) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val orders by authViewModel.orders.collectAsState()
    var showOrderDetails by remember { mutableStateOf<Order?>(null) }
    var showAddAddressDialog by remember { mutableStateOf(false) }
    var addressToEdit by remember { mutableStateOf<Address?>(null) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    val recentlyViewedProducts by ecommerceViewModel.recentlyViewedProducts.collectAsState()
    val selectedLanguage by authViewModel.selectedLanguage.collectAsState()

    if (showOrderDetails != null) {
        OrderDetailsDialog(
            order = showOrderDetails!!,
            onDismiss = { showOrderDetails = null }
        )
    }

    if (showAddAddressDialog || addressToEdit != null) {
        AddEditAddressDialog(
            address = addressToEdit,
            onDismiss = {
            showAddAddressDialog = false
                addressToEdit = null
            },
            onSave = { address ->
                if (addressToEdit != null) {
                    // Delete old and add new (simple update)
                    authViewModel.deleteAddress(addressToEdit!!.id)
                }
                authViewModel.addAddress(address)
                showAddAddressDialog = false
                addressToEdit = null
            }
        )
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            currentUser = currentUser,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, phone ->
                authViewModel.updateProfile(name, phone)
                showEditProfileDialog = false
            }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = { language ->
                authViewModel.changeLanguage(language)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Profile Header
        item {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(100.dp),
                            shadowElevation = 8.dp
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentUser?.name ?: "Guest",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = currentUser?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )

                        if (currentUser?.phone?.isNotEmpty() == true) {
                            Text(
                                text = currentUser?.phone ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Edit Profile Button
                        FilledTonalButton(
                            onClick = { showEditProfileDialog = true },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit Profile", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Profile Options Section
        item {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Language Settings
                ProfileOptionCard(
                    icon = Icons.Default.Settings,
                    title = "Language",
                    subtitle = selectedLanguage,
                    onClick = { showLanguageDialog = true }
                )
            }
        }

        // Recently Viewed Products Section
        if (recentlyViewedProducts.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Recently Viewed",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recentlyViewedProducts.take(5)) { product ->
                        RecentlyViewedProductCard(product, ecommerceViewModel)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Addresses Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Addresses",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                FilledTonalButton(
                    onClick = { showAddAddressDialog = true },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add")
                }
            }
        }

        // Address Cards
        val addresses = currentUser?.addresses ?: emptyList()
        if (addresses.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "No addresses saved",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Add an address for faster checkout",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            items(addresses) { address ->
                AddressCard(
                    address = address,
                    onEdit = { addressToEdit = address },
                    onDelete = { authViewModel.deleteAddress(address.id) },
                    onSetDefault = { authViewModel.setDefaultAddress(address.id) }
                )
            }
        }

        // Orders Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Orders",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${orders.size} orders",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        if (orders.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No orders yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            items(orders) { order ->
                OrderCard(
                    order = order,
                    onClick = { showOrderDetails = order }
                )
            }
        }

        // Logout Button
        item {
            Spacer(modifier = Modifier.height(24.dp))

            // Support Option
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { /* TODO: Open support/help center */ },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF95E1D3).copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF95E1D3),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Help & Support",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Get help with your orders and account",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { authViewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Logout",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${order.items.size} ${if (order.items.size == 1) "item" else "items"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                    .format(java.util.Date(order.orderDate)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ${String.format("%.2f", order.total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun AddressCard(
    address: Address,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (address.isDefault)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surface
        ),
        border = if (address.isDefault)
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (address.isDefault)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (address.isDefault)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = address.label,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (address.isDefault) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = "DEFAULT",
                                        modifier = Modifier.padding(
                                            horizontal = 6.dp,
                                            vertical = 2.dp
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text(
                            text = address.fullName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = address.addressLine1,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (address.addressLine2.isNotEmpty()) {
                    Text(
                        text = address.addressLine2,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "${address.city}, ${address.state} - ${address.zipCode}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Phone: ${address.phoneNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            if (!address.isDefault) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = onSetDefault,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Set as Default")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAddressDialog(
    address: Address?,
    onDismiss: () -> Unit,
    onSave: (Address) -> Unit
) {
    var label by remember { mutableStateOf(address?.label ?: "Home") }
    var fullName by remember { mutableStateOf(address?.fullName ?: "") }
    var phoneNumber by remember { mutableStateOf(address?.phoneNumber ?: "") }
    var addressLine1 by remember { mutableStateOf(address?.addressLine1 ?: "") }
    var addressLine2 by remember { mutableStateOf(address?.addressLine2 ?: "") }
    var city by remember { mutableStateOf(address?.city ?: "") }
    var state by remember { mutableStateOf(address?.state ?: "") }
    var zipCode by remember { mutableStateOf(address?.zipCode ?: "") }
    var isDefault by remember { mutableStateOf(address?.isDefault ?: false) }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (address == null) "Add New Address" else "Edit Address",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Address Label Selection
                item {
                    Text(
                        "Address Type",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Home", "Work", "Other").forEach { type ->
                            FilterChip(
                                selected = label == type,
                                onClick = { label = type },
                                label = { Text(type) },
                                leadingIcon = {
                                    Icon(
                                        when (type) {
                                            "Home" -> Icons.Default.Home
                                            "Work" -> Icons.Default.Info
                                            else -> Icons.Default.LocationOn
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null)
                        },
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = addressLine1,
                        onValueChange = { addressLine1 = it },
                        label = { Text("Address Line 1 *") },
                        placeholder = { Text("House No., Building, Street") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                        },
                        maxLines = 2
                    )
                }

                item {
                    OutlinedTextField(
                        value = addressLine2,
                        onValueChange = { addressLine2 = it },
                        label = { Text("Address Line 2 (Optional)") },
                        placeholder = { Text("Area, Landmark") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                        },
                        maxLines = 2
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("City *") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = state,
                            onValueChange = { state = it },
                            label = { Text("State *") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = zipCode,
                        onValueChange = { zipCode = it },
                        label = { Text("ZIP Code *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isDefault = !isDefault }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Set as default address",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Checkbox(
                            checked = isDefault,
                            onCheckedChange = { isDefault = it }
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    item {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    errorMessage = ""
                    when {
                        fullName.isBlank() -> errorMessage = "Please enter full name"
                        phoneNumber.isBlank() -> errorMessage = "Please enter phone number"
                        addressLine1.isBlank() -> errorMessage = "Please enter address"
                        city.isBlank() -> errorMessage = "Please enter city"
                        state.isBlank() -> errorMessage = "Please enter state"
                        zipCode.isBlank() -> errorMessage = "Please enter ZIP code"
                        else -> {
                            val newAddress = Address(
                                id = address?.id ?: UUID.randomUUID().toString(),
                                label = label,
                                fullName = fullName,
                                phoneNumber = phoneNumber,
                                addressLine1 = addressLine1,
                                addressLine2 = addressLine2,
                                city = city,
                                state = state,
                                zipCode = zipCode,
                                isDefault = isDefault
                            )
                            onSave(newAddress)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (address == null) "Add Address" else "Update Address")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ProfileOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    currentUser: User?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Profile",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null)
                    },
                    singleLine = true
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "Email: ${currentUser?.email ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    errorMessage = ""
                    when {
                        name.isBlank() -> errorMessage = "Please enter your name"
                        phone.isBlank() -> errorMessage = "Please enter phone number"
                        else -> onSave(name, phone)
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun LanguageSelectionDialog(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf(
        "English" to "🇺🇸",
        "Spanish" to "🇪🇸",
        "French" to "🇫🇷",
        "German" to "🇩🇪",
        "Italian" to "🇮🇹",
        "Portuguese" to "🇵🇹",
        "Chinese" to "🇨🇳",
        "Japanese" to "🇯🇵",
        "Korean" to "🇰🇷",
        "Hindi" to "🇮🇳",
        "Arabic" to "🇸🇦",
        "Russian" to "🇷🇺"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Language",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                items(languages) { (language, flag) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageSelected(language)
                            },
                        shape = RoundedCornerShape(12.dp),
                        border = if (selectedLanguage == language) {
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        } else {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedLanguage == language)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = flag,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = language,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = if (selectedLanguage == language)
                                        FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            if (selectedLanguage == language) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Close")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyViewedProductCard(product: Product, viewModel: EcommerceViewModel) {
    Card(
        modifier = Modifier
            .width(140.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            viewModel.trackProductView(product)
        }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "${product.discount}%",
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${String.format("%.2f", product.discountedPrice)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (product.discount > 0) {
                        Text(
                            text = "${product.price}",
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: OrderStatus) {
    val (color, text) = when (status) {
        OrderStatus.PENDING -> Color(0xFFFFA000) to "Pending"
        OrderStatus.CONFIRMED -> Color(0xFF2196F3) to "Confirmed"
        OrderStatus.SHIPPED -> Color(0xFF9C27B0) to "Shipped"
        OrderStatus.DELIVERED -> Color(0xFF00BFA5) to "Delivered"
        OrderStatus.CANCELLED -> Color(0xFFF44336) to "Cancelled"
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun OrderDetailsDialog(order: Order, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Order #${order.id}",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    StatusBadge(order.status)
                }

                item {
                    Text(
                        text = "Order Date",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = java.text.SimpleDateFormat(
                            "MMM dd, yyyy hh:mm a",
                            java.util.Locale.getDefault()
                        )
                            .format(java.util.Date(order.orderDate)),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                item {
                    HorizontalDivider()
                }

                item {
                    Text(
                        text = "Items",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(order.items) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.product.name,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Qty: ${item.quantity}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Text(
                            text = "${String.format("%.2f", item.price * item.quantity)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                item {
                    HorizontalDivider()
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${String.format("%.2f", order.total)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                item {
                    Text(
                        text = "Delivery Address",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = order.deliveryAddress,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item {
                    Text(
                        text = "Payment Method",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = order.paymentMethod,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

// ========== WISHLIST SCREEN ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    viewModel: EcommerceViewModel,
    onBack: () -> Unit,
    onProductClick: (String) -> Unit
) {
    val wishlistProducts by viewModel.wishlistProducts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Column {
                        Text(
                            "My Wishlist",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "${wishlistProducts.size} items",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        if (wishlistProducts.isEmpty()) {
            // Empty wishlist
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.size(120.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Your wishlist is empty",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Add products you love to your wishlist!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wishlistProducts) { product ->
                    WishlistProductCard(
                        product = product,
                        viewModel = viewModel,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistProductCard(
    product: Product,
    viewModel: EcommerceViewModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.height(150.dp)
        ) {
            Box(
                modifier = Modifier.width(150.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 12.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "${product.discount}%",
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${product.rating}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "(${product.reviews})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (product.discount > 0) {
                            Text(
                                text = formatIndianCurrency(product.price),
                                style = MaterialTheme.typography.bodySmall,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        Text(
                            text = formatIndianCurrency(product.discountedPrice),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledIconButton(
                            onClick = { viewModel.removeFromWishlist(product.id) },
                            modifier = Modifier.size(42.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        FilledIconButton(
                            onClick = { viewModel.addToCart(product) },
                            modifier = Modifier.size(42.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Add to Cart",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ========== PRODUCT DETAIL SCREEN ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: EcommerceViewModel,
    onBack: () -> Unit
) {
    val product = viewModel.getProductById(productId)
    val isInWishlist by remember {
        derivedStateOf { viewModel.isInWishlist(productId) }
    }

    if (product == null) {
        // Product not found
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Product not found")
            Button(onClick = onBack) {
                Text("Go Back")
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Image Gallery
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                val images = listOf(product.imageUrl) + product.additionalImages
                var selectedImageIndex by remember { mutableStateOf(0) }

                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = images[selectedImageIndex],
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Back button
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                CircleShape
                            )
                            .align(Alignment.TopStart)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    // Wishlist button
                    IconButton(
                        onClick = { viewModel.toggleWishlist(product) },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                CircleShape
                            )
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isInWishlist) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Image indicators
                    if (images.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            images.forEachIndexed { index, _ ->
                                Box(
                                    modifier = Modifier
                                        .size(if (selectedImageIndex == index) 10.dp else 8.dp)
                                        .background(
                                            if (selectedImageIndex == index)
                                                MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                        .clickable { selectedImageIndex = index }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Product Info
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "${product.rating}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        "${product.reviews} reviews",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        formatIndianCurrency(product.discountedPrice),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (product.discount > 0) {
                        Text(
                            formatIndianCurrency(product.price),
                            style = MaterialTheme.typography.titleMedium,
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF00BFA5).copy(alpha = 0.2f)
                        ) {
                            Text(
                                "${product.discount}% OFF",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00BFA5)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (product.inStock) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00BFA5),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "In Stock",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00BFA5)
                        )
                    }
                } else {
                    Text(
                        "Out of Stock",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Product Description",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    product.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }
        }

        // Customer Reviews
        if (product.customerReviews.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        "Customer Reviews",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            items(product.customerReviews) { review ->
                CustomerReviewCard(review)
            }
        }

        // Bottom spacing for add to cart button
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // Add to Cart Button - Fixed at bottom
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp,
            shadowElevation = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.toggleWishlist(product) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isInWishlist) Color.Red else MaterialTheme.colorScheme.primary
                    )
                }
                Button(
                    onClick = { viewModel.addToCart(product) },
                    modifier = Modifier
                        .weight(3f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = product.inStock
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Add to Cart",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerReviewCard(review: CustomerReview) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                review.customerName.first().uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                review.customerName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            if (review.verified) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = Color(0xFF00BFA5),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Text(
                            review.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        review.rating.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                review.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun EcommerceApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val database =
        remember { com.runanywhere.startup_hackathon20.data.AppDatabase.getDatabase(context) }
    val authViewModelFactory =
        remember { com.runanywhere.startup_hackathon20.viewmodels.AuthViewModelFactory(database.userDao()) }

    val ecommerceViewModel: EcommerceViewModel = viewModel()
    val aiViewModel: AIShoppingAssistantViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

    var selectedTab by remember { mutableStateOf(0) }
    val cartItemCount by ecommerceViewModel.cartItemCount.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    var showWishlist by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf<String?>(null) }
    var showNotifications by remember { mutableStateOf(false) }
    var showFlashSales by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedOrderForTracking by remember { mutableStateOf<Order?>(null) }

    // Show login screen if not logged in
    if (!isLoggedIn) {
        AuthenticationScreen(authViewModel)
    } else if (showNotifications) {
        val notifications = listOf(
            NotificationItem(
                id = "1",
                icon = Icons.Default.Info,
                title = "Welcome to Techxon!",
                body = "Enjoy shopping.",
                time = "Now",
                isUnread = true
            ),
            NotificationItem(
                id = "2",
                icon = Icons.Default.Star,
                title = "Flash Deal!",
                body = "Limited time discount on select products.",
                time = "5m ago"
            ),
            // add more demo notifications as needed
        )
        NotificationsScreen(
            notifications = notifications,
            onBack = { showNotifications = false }
        )
    } else if (showFlashSales) {
        val flashProducts by ecommerceViewModel.featuredProducts.collectAsState()
        FlashSalesScreen(
            products = flashProducts,
            onProductClick = { productId ->
                selectedProductId = productId
                showFlashSales = false
            },
            onBack = { showFlashSales = false }
        )
    } else if (showWishlist) {
        WishlistScreen(
            viewModel = ecommerceViewModel,
            onBack = { showWishlist = false },
            onProductClick = { productId ->
                selectedProductId = productId
                showWishlist = false
            }
        )
    } else if (selectedProductId != null) {
        ProductDetailScreen(
            productId = selectedProductId!!,
            viewModel = ecommerceViewModel,
            onBack = {
                selectedProductId = null
            }
        )
    } else if (selectedOrderForTracking != null) {
        OrderTrackingScreen(
            order = selectedOrderForTracking!!,
            onBack = { selectedOrderForTracking = null }
        )
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                "Home",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Categories",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                "Categories",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (cartItemCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.error
                                        ) {
                                            Text(
                                                "$cartItemCount",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Cart",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                "Cart",
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "AI Assistant",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                "Pookie",
                                fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                "Profile",
                                fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 }
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                // Show Filter/Sort dialog if needed
                val selectedCategoryValue by ecommerceViewModel.selectedCategory.collectAsState()
                FilterSortDialog(
                    show = showFilterDialog,
                    availableCategories = getCategories(),
                    selectedCategory = selectedCategoryValue,
                    sortOptions = listOf(
                        "Relevance",
                        "Price: Low to High",
                        "Price: High to Low",
                        "Rating"
                    ),
                    selectedSort = "Relevance",
                    onCategorySelect = { categoryId -> ecommerceViewModel.selectCategory(categoryId) },
                    onSortSelect = { /* handle sort selection, update VM as needed */ },
                    onApply = { showFilterDialog = false },
                    onDismiss = { showFilterDialog = false }
                )
                when (selectedTab) {
                    0 -> HomeScreen(
                        viewModel = ecommerceViewModel,
                        onShowWishlist = { showWishlist = true }
                    )
                    1 -> CategoriesScreen(ecommerceViewModel)
                    2 -> CartScreen(
                        viewModel = ecommerceViewModel,
                        onNavigateHome = { selectedTab = 0 }
                    )
                    3 -> AIAssistantScreen(aiViewModel, ecommerceViewModel)
                    4 -> ProfileScreen(authViewModel, ecommerceViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: EcommerceViewModel,
    onShowWishlist: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val featuredProducts by viewModel.featuredProducts.collectAsState()
    val topRatedProducts by viewModel.topRatedProducts.collectAsState()
    val displayedProducts by viewModel.displayedProducts.collectAsState()
    var showSearchBar by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var selectedCategoryForView by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Header with Quick Service Buttons (Flipkart-style)
        item {
            Surface(
                color = Color(0xFF2874F0),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Top Bar with Logo and Icons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.techxon_logo),
                                contentDescription = "Logo",
                                modifier = Modifier.size(36.dp)
                            )
                            Column {
                                Text(
                                    "Techxon",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    "Shop Smart, Live Better",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = onShowWishlist,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "Wishlist",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        placeholder = { 
                            Text(
                                "Search for products, brands and more",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            ) 
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }
        }

        // Quick Service Buttons (Flipkart, Minutes, Travel, Grocery style)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    QuickServiceCard(
                        title = "Flash Sale",
                        icon = Icons.Default.Star,
                        backgroundColor = Color(0xFFFFF59D),
                        onClick = { /* Navigate to flash sales */ }
                    )
                }
                item {
                    QuickServiceCard(
                        title = "Categories",
                        icon = Icons.Default.Menu,
                        backgroundColor = Color(0xFFE1BEE7),
                        onClick = { showCategoryMenu = true }
                    )
                }
                item {
                    QuickServiceCard(
                        title = "Deals",
                        icon = Icons.Default.ShoppingCart,
                        backgroundColor = Color(0xFFB2DFDB),
                        onClick = { /* Navigate to deals */ }
                    )
                }
                item {
                    QuickServiceCard(
                        title = "New",
                        icon = Icons.Default.Add,
                        backgroundColor = Color(0xFFFFCCBC),
                        onClick = { /* Navigate to new arrivals */ }
                    )
                }
            }
        }

        // Location Bar (like Flipkart)
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { /* Handle location change */ },
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Location",
                        tint = Color(0xFF2874F0),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        "HOME",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2874F0)
                    )
                    Text(
                        "Deliver to your location",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Change",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Horizontal Category Scroll (Baby, Auto, Sports, Furniture, etc.)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val mainCategories = getMainCategories().take(8)
                items(mainCategories) { category ->
                    CategoryIconCard(
                        category = category,
                        onClick = { 
                            viewModel.selectCategory(category.id)
                            selectedCategoryForView = category.id
                        }
                    )
                }
            }
        }

        // Banner/Promotional Carousel
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) { index ->
                    ModernPromoBanner(
                        index = index,
                        onClick = { /* Navigate to promo details */ }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // "Still looking for these?" Section (like Flipkart)
        if (displayedProducts.isNotEmpty()) {
            item {
                Surface(
                    color = Color(0xFFE3F2FD),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Still looking for these?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(displayedProducts.take(5)) { product ->
                                StillLookingProductCard(
                                    product = product,
                                    onClick = { viewModel.trackProductView(product) }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Product Showcase Cards with Offers
        if (featuredProducts.isNotEmpty()) {
            items(featuredProducts.take(3).chunked(1)) { products ->
                products.forEach { product ->
                    ShowcaseProductCard(
                        product = product,
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        // "Lowest prices: Only on LIVE" Section
        item {
            Surface(
                color = Color(0xFFE1BEE7),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { /* Navigate to live sales */ },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = Color(0xFFBA68C8),
                            shape = CircleShape,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                "Lowest prices: Only on LIVE",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A148C)
                            )
                            Text(
                                "Exclusive deals starting now",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6A1B9A)
                            )
                        }
                    }
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "View",
                        tint = Color(0xFF4A148C),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // "Must-have for smart shoppers" Section
        if (topRatedProducts.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        "Must-have for smart shoppers",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(topRatedProducts) { product ->
                        MustHaveProductCard(
                            product = product,
                            viewModel = viewModel
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // "Top deals on fashion!" Section
        if (displayedProducts.filter { it.category == "fashion" }.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        "Top deals on fashion!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val fashionProducts = displayedProducts.filter { it.category == "fashion" }.take(6)
                    items(fashionProducts) { product ->
                        FashionDealCard(
                            product = product,
                            viewModel = viewModel
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // All Products Grid
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Explore All Products",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    TextButton(onClick = { /* Show all products */ }) {
                        Text("View All")
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        items(displayedProducts.chunked(2)) { rowProducts ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowProducts.forEach { product ->
                    Box(modifier = Modifier.weight(1f)) {
                        CompactProductCard(product, viewModel)
                    }
                }
                if (rowProducts.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Quick Service Card (Flipkart, Minutes, Travel, Grocery style)
@Composable
fun QuickServiceCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF212121)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Category Icon Card (Baby, Auto, Sports, etc.)
@Composable
fun CategoryIconCard(
    category: Category,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .clickable(onClick = onClick)
    ) {
        Surface(
            color = category.color.copy(alpha = 0.2f),
            shape = CircleShape,
            modifier = Modifier.size(64.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    category.icon,
                    contentDescription = category.name,
                    tint = category.color,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            category.name.split(" ").first(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF212121),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Modern Promo Banner
@Composable
fun ModernPromoBanner(index: Int, onClick: () -> Unit) {
    val gradients = listOf(
        listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
        listOf(Color(0xFFF093FB), Color(0xFFF5576C)),
        listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
    )
    val gradient = gradients[index % gradients.size]

    Card(
        modifier = Modifier
            .width(300.dp)
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = gradient
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        when (index) {
                            0 -> "MEGA SALE"
                            1 -> "NEW COLLECTION"
                            else -> "FLASH DEALS"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        when (index) {
                            0 -> "Up to 70% OFF"
                            1 -> "Fresh Arrivals"
                            else -> "Limited Time Only"
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "Shop Now",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = gradient.first()
                    )
                }
            }
        }
    }
}

// Still Looking Product Card
@Composable
fun StillLookingProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Surface(
                        color = Color(0xFFFF6B00),
                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            "${product.discount}% OFF",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "₹${String.format("%.0f", product.discountedPrice)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2874F0)
                )
            }
        }
    }
}

// Showcase Product Card (Large promotional card)
@Composable
fun ShowcaseProductCard(
    product: Product,
    viewModel: EcommerceViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { viewModel.trackProductView(product) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = Color(0xFF388E3C),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    "${product.rating}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                        Text(
                            "(${product.reviews})",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
                
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (product.discount > 0) {
                            Text(
                                "₹${String.format("%.0f", product.price)}",
                                style = MaterialTheme.typography.bodyMedium,
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )
                            Surface(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    "${product.discount}% off",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD32F2F)
                                )
                            }
                        }
                    }
                    Text(
                        "₹${String.format("%.0f", product.discountedPrice)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF212121)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.addToCart(product) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF9800)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text(
                                "ADD TO CART",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// Must Have Product Card
@Composable
fun MustHaveProductCard(
    product: Product,
    viewModel: EcommerceViewModel
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(280.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp),
        onClick = { viewModel.trackProductView(product) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF212121)
                )
                
                Column {
                    if (product.discount > 0) {
                        Text(
                            "₹${String.format("%.0f", product.price)}",
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "₹${String.format("%.0f", product.discountedPrice)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                        if (product.discount > 0) {
                            Text(
                                "${product.discount}% off",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF388E3C)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Fashion Deal Card
@Composable
fun FashionDealCard(
    product: Product,
    viewModel: EcommerceViewModel
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(240.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { viewModel.trackProductView(product) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Surface(
                        color = Color(0xFFD32F2F),
                        shape = RoundedCornerShape(bottomEnd = 12.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            "${product.discount}%",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        if (product.discount > 0) {
                            Text(
                                "₹${String.format("%.0f", product.price)}",
                                style = MaterialTheme.typography.labelSmall,
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )
                        }
                        Text(
                            "₹${String.format("%.0f", product.discountedPrice)}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                    }
                }
            }
        }
    }
}

// Compact Product Card for grid
@Composable
fun CompactProductCard(
    product: Product,
    viewModel: EcommerceViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { viewModel.trackProductView(product) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Surface(
                        color = Color(0xFFFF6B00),
                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            "${product.discount}%",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                // Wishlist icon
                IconButton(
                    onClick = { viewModel.toggleWishlist(product) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        if (viewModel.isInWishlist(product.id)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (viewModel.isInWishlist(product.id)) Color.Red else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF212121)
                )
                
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = Color(0xFF388E3C),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${product.rating}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    if (product.discount > 0) {
                        Text(
                            "₹${String.format("%.0f", product.price)}",
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                    }
                    Text(
                        "₹${String.format("%.0f", product.discountedPrice)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                }
            }
        }
    }
}

@Composable
fun PromotionalBanner(index: Int) {
    val colors = listOf(
        Color(0xFF7C4DFF) to Color(0xFF00BFA5),
        Color(0xFFFF4081) to Color(0xFFFFC400),
        Color(0xFF00BFA5) to Color(0xFF00897B)
    )
    val (startColor, endColor) = colors[index % colors.size]

    Card(
        modifier = Modifier
            .width(320.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(startColor, endColor)
                    )
                )
                .clickable { }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        when (index) {
                            0 -> "MEGA SALE"
                            1 -> "NEW ARRIVALS"
                            else -> "FLASH DEALS"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        when (index) {
                            0 -> "Up to 70% OFF"
                            1 -> "Fresh Collection"
                            else -> "Limited Time"
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "Shop Now",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = startColor
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChip(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    FilterChip(
        selected = false,
        onClick = { },
        label = { Text(name) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun QuickAccessCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = color.copy(alpha = 0.1f),
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ModernProductCard(product: Product, viewModel: EcommerceViewModel) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Surface(
                        color = Color(0xFFFF6F00),
                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            "${product.discount}% OFF",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "${String.format("%.0f", product.discountedPrice)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (product.discount > 0) {
                        Text(
                            "${product.price}",
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFF00BFA5),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${product.rating}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "(${product.reviews})",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.addToCart(product) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A1B9A)
                    ),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text("ADD", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun GridProductCard(product: Product, viewModel: EcommerceViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Surface(
                        color = Color(0xFFFF6F00),
                        shape = RoundedCornerShape(bottomEnd = 8.dp)
                    ) {
                        Text(
                            "${product.discount}%",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "${String.format("%.0f", product.discountedPrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Surface(
                        color = Color(0xFF00BFA5),
                        shape = RoundedCornerShape(3.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${product.rating}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontSize = 10.sp
                            )
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryTab(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Icon(
            icon,
            contentDescription = name,
            modifier = Modifier.size(32.dp),
            tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
        )
        Text(
            name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color.Black else Color.Gray
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(40.dp)
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun RecommendedProductCard(product: Product, viewModel: EcommerceViewModel) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                product.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CompactProductCard(product: Product, viewModel: EcommerceViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { }
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "${String.format("%.0f", product.discountedPrice)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (product.discount > 0) {
                        Text(
                            "${product.price}",
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                        Text(
                            "${product.discount}% off",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF00BFA5),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC400),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        " ${product.rating}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedProductCard(product: Product, viewModel: EcommerceViewModel) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .width(280.dp)
            .scale(scale),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            viewModel.trackProductView(product)
        }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient overlay for better text visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )
                
                if (product.discount > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "${product.discount}% OFF",
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                
                // Featured Badge
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Featured",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${product.rating}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "(${product.reviews})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (product.discount > 0) {
                            Text(
                                text = "₹${product.price}",
                                style = MaterialTheme.typography.bodyMedium,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        Text(
                            text = "₹${String.format("%.2f", product.discountedPrice)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = { 
                            viewModel.addToCart(product)
                            isPressed = true
                        },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Cart",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    viewModel: EcommerceViewModel,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            viewModel.trackProductView(product)
        }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "${product.discount}% OFF",
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = " ${product.rating} (${product.reviews})",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (product.discount > 0) {
                            Text(
                                text = "₹${product.price}",
                                style = MaterialTheme.typography.bodySmall,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        Text(
                            text = "₹${String.format("%.2f", product.discountedPrice)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    FilledIconButton(
                        onClick = { 
                            viewModel.addToCart(product)
                            isPressed = true
                        },
                        modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Cart",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriesScreen(viewModel: EcommerceViewModel) {
    val categories = remember { getCategories() }
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val products by viewModel.displayedProducts.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Beautiful Header with Gradient
        item {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Categories",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "Browse by category",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Show category cards only when no category is selected
        if (selectedCategory == null) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Category Cards in Grid
            items(categories.chunked(2)) { rowCategories ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowCategories.forEach { category ->
                        Box(modifier = Modifier.weight(1f)) {
                            CategoryCard(
                                category = category,
                                onClick = { viewModel.selectCategory(category.id) }
                            )
                        }
                    }
                    if (rowCategories.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            // Show back button and products when category is selected
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledIconButton(
                            onClick = { viewModel.selectCategory(null) },
                            modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        val selectedCat = categories.find { it.id == selectedCategory }
                        Column {
                            Text(
                                selectedCat?.name ?: "Category",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${products.size} ${if (products.size == 1) "product" else "products"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Products List
            if (products.isNotEmpty()) {
                items(products) { product ->
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ProductListItem(product, viewModel)
                    }
                }
            } else {
                // Empty state with beautiful design
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(100.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "No products found",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Try selecting a different category",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .scale(scale),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            isPressed = true
            onClick()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            category.color.copy(alpha = 0.2f),
                            category.color.copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                // Icon at top
                Surface(
                    shape = CircleShape,
                    color = category.color.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            category.icon,
                            contentDescription = null,
                            tint = category.color,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Category name at bottom
                Column {
                    Text(
                        category.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Explore ${category.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Arrow icon at top right
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = category.color,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun CircularStoreItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFE1BEE7),
            modifier = Modifier.size(60.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Store Name",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CircularFeatureItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFFFE0B2),
            modifier = Modifier.size(60.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFFFF6F00)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Feature",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListItem(product: Product, viewModel: EcommerceViewModel) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            viewModel.trackProductView(product)
        }
    ) {
        Row(
            modifier = Modifier.height(150.dp)
        ) {
            Box(
                modifier = Modifier.width(150.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (product.discount > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 12.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "${product.discount}%",
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${product.rating}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "(${product.reviews})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (product.discount > 0) {
                            Text(
                                text = "₹${product.price}",
                                style = MaterialTheme.typography.bodySmall,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        Text(
                            text = "₹${String.format("%.2f", product.discountedPrice)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    FilledIconButton(
                        onClick = {
                            viewModel.addToCart(product)
                            isPressed = true
                        },
                        modifier = Modifier.size(42.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Cart",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AccountActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                title,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun FinanceOptionItem(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Icon(
            Icons.Default.ArrowForward,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun CartScreen(viewModel: EcommerceViewModel, onNavigateHome: () -> Unit = {}) {
    val cartItems by viewModel.cartItems.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()
    val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    var showCheckout by remember { mutableStateOf(false) }

    if (showCheckout) {
        CheckoutScreen(
            cartTotal = cartTotal,
            cartItems = cartItems,
            authViewModel = authViewModel,
            onBack = { showCheckout = false },
            onPaymentSuccess = {
                viewModel.clearCart()
                showCheckout = false
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Enhanced Header
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Shopping Cart",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        if (cartItems.isNotEmpty()) {
                            Text(
                                "${cartItems.size} ${if (cartItems.size == 1) "item" else "items"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            if (cartItems.isEmpty()) {
                // Enhanced Empty Cart
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(120.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Your cart is empty",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Add some products to get started!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { onNavigateHome() },
                        modifier = Modifier.fillMaxWidth(0.7f),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Start Shopping",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Cart Items with enhanced styling
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems, key = { it.product.id }) { cartItem ->
                        CartItemCard(cartItem, viewModel)
                    }

                    // Spacing at the bottom
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Enhanced Checkout Section
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 16.dp,
                    shadowElevation = 16.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Order Summary
                        Text(
                            "Order Summary",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Subtotal:",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                "₹${String.format("%.2f", cartTotal)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Shipping:",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                "FREE",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Total:",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                "₹${String.format("%.2f", cartTotal)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { showCheckout = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Proceed to Checkout",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { viewModel.clearCart() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Clear Cart",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------- CHECKOUT SCREEN RELATED CODE ---------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartTotal: Double,
    cartItems: List<CartItem>,
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onPaymentSuccess: () -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    var showPaymentProcessing by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showAddEditAddressDialog by remember { mutableStateOf(false) } // NEW STATE VAR
    val selectedAddress by authViewModel.selectedAddress.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Payment form states
    var upiId by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf("") }
    var walletNumber by remember { mutableStateOf("") }

    if (showSuccessDialog) {
        PaymentSuccessDialog(
            amount = cartTotal,
            onDismiss = {
                showSuccessDialog = false
                // CREATE ORDER with selected address
                val deliveryAddr = selectedAddress?.let {
                    "${it.addressLine1}, ${if (it.addressLine2.isNotEmpty()) it.addressLine2 + ", " else ""}${it.city}, ${it.state} - ${it.zipCode}"
                } ?: "Default Address"

                authViewModel.createOrder(
                    cartItems = cartItems,
                    total = cartTotal,
                    paymentMethod = when (selectedPaymentMethod) {
                        PaymentMethod.UPI -> "UPI"
                        PaymentMethod.CARD -> "Credit/Debit Card"
                        PaymentMethod.NET_BANKING -> "Net Banking"
                        PaymentMethod.WALLET -> "Wallet"
                        PaymentMethod.COD -> "Cash on Delivery"
                        null -> "N/A"
                    },
                    deliveryAddress = deliveryAddr
                )
                onPaymentSuccess()
            }
        )
    }

    if (showAddressDialog) {
        AddressSelectionDialog(
            addresses = currentUser?.addresses ?: emptyList(),
            selectedAddress = selectedAddress,
            onAddressSelected = { address ->
                authViewModel.selectAddressForOrder(address)
                showAddressDialog = false
            },
            onAddNew = {
                showAddressDialog = false
                showAddEditAddressDialog = true // Open add/edit dialog from checkout
            },
            onDismiss = { showAddressDialog = false }
        )
    }
    if (showAddEditAddressDialog) {
        AddEditAddressDialog(
            address = null,
            onDismiss = { showAddEditAddressDialog = false },
            onSave = { address ->
                authViewModel.addAddress(address)
                authViewModel.selectAddressForOrder(address)
                showAddEditAddressDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Column {
                        Text(
                            "Checkout",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "Review your order",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Amount Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Total Amount",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Text(
                                "₹${String.format("%.2f", cartTotal)}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // Delivery Address Section
            item {
                Text(
                    "Delivery Address",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                if (selectedAddress != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAddressDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedAddress!!.label,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = "SELECTED",
                                            modifier = Modifier.padding(
                                                horizontal = 6.dp,
                                                vertical = 2.dp
                                            ),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedAddress!!.fullName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = selectedAddress!!.addressLine1,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (selectedAddress!!.addressLine2.isNotEmpty()) {
                                    Text(
                                        text = selectedAddress!!.addressLine2,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(
                                    text = "${selectedAddress!!.city}, ${selectedAddress!!.state} - ${selectedAddress!!.zipCode}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Phone: ${selectedAddress!!.phoneNumber}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }

                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Change address",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAddressDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "Please select a delivery address",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // Payment Methods Section
            item {
                Text(
                    "Choose Payment Method",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // ... existing payment method items ...
            // UPI Payment
            item {
                PaymentMethodCard(
                    method = PaymentMethod.UPI,
                    isSelected = selectedPaymentMethod == PaymentMethod.UPI,
                    onSelect = { selectedPaymentMethod = PaymentMethod.UPI },
                    icon = Icons.Default.Star,
                    title = "UPI",
                    subtitle = "Google Pay, PhonePe, Paytm & more"
                )
            }

            if (selectedPaymentMethod == PaymentMethod.UPI) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Enter UPI ID",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = upiId,
                                onValueChange = { upiId = it },
                                label = { Text("UPI ID") },
                                placeholder = { Text("example@upi") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            }

            // Card Payment
            item {
                PaymentMethodCard(
                    method = PaymentMethod.CARD,
                    isSelected = selectedPaymentMethod == PaymentMethod.CARD,
                    onSelect = { selectedPaymentMethod = PaymentMethod.CARD },
                    icon = Icons.Default.Info,
                    title = "Credit / Debit Card",
                    subtitle = "Visa, Mastercard, Rupay, Amex"
                )
            }

            if (selectedPaymentMethod == PaymentMethod.CARD) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Enter Card Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { if (it.length <= 19) cardNumber = it },
                                label = { Text("Card Number") },
                                placeholder = { Text("1234 5678 9012 3456") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = {
                                    Icon(Icons.Default.Info, contentDescription = null)
                                }
                            )
                            OutlinedTextField(
                                value = cardName,
                                onValueChange = { cardName = it },
                                label = { Text("Cardholder Name") },
                                placeholder = { Text("JOHN DOE") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null)
                                }
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = expiryDate,
                                    onValueChange = { if (it.length <= 5) expiryDate = it },
                                    label = { Text("Expiry") },
                                    placeholder = { Text("MM/YY") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    leadingIcon = {
                                        Icon(Icons.Default.DateRange, contentDescription = null)
                                    }
                                )
                                OutlinedTextField(
                                    value = cvv,
                                    onValueChange = { if (it.length <= 4) cvv = it },
                                    label = { Text("CVV") },
                                    placeholder = { Text("123") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, contentDescription = null)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Net Banking
            item {
                PaymentMethodCard(
                    method = PaymentMethod.NET_BANKING,
                    isSelected = selectedPaymentMethod == PaymentMethod.NET_BANKING,
                    onSelect = { selectedPaymentMethod = PaymentMethod.NET_BANKING },
                    icon = Icons.Default.Home,
                    title = "Net Banking",
                    subtitle = "All major banks supported"
                )
            }

            if (selectedPaymentMethod == PaymentMethod.NET_BANKING) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Select Your Bank",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            val banks = listOf(
                                "State Bank of India",
                                "HDFC Bank",
                                "ICICI Bank",
                                "Axis Bank",
                                "Kotak Mahindra Bank",
                                "Punjab National Bank"
                            )

                            banks.forEach { bank ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedBank = bank },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (selectedBank == bank) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.outline
                                    ),
                                    color = if (selectedBank == bank)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surface
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Home,
                                            contentDescription = null,
                                            tint = if (selectedBank == bank)
                                                MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            bank,
                                            modifier = Modifier.weight(1f),
                                            fontWeight = if (selectedBank == bank)
                                                FontWeight.Bold else FontWeight.Normal
                                        )
                                        if (selectedBank == bank) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Wallets
            item {
                PaymentMethodCard(
                    method = PaymentMethod.WALLET,
                    isSelected = selectedPaymentMethod == PaymentMethod.WALLET,
                    onSelect = { selectedPaymentMethod = PaymentMethod.WALLET },
                    icon = Icons.Default.Phone,
                    title = "Wallets",
                    subtitle = "Paytm, PhonePe, Amazon Pay"
                )
            }

            if (selectedPaymentMethod == PaymentMethod.WALLET) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Enter Wallet Number",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = walletNumber,
                                onValueChange = { if (it.length <= 10) walletNumber = it },
                                label = { Text("Mobile Number") },
                                placeholder = { Text("9876543210") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            }

            // Cash on Delivery
            item {
                PaymentMethodCard(
                    method = PaymentMethod.COD,
                    isSelected = selectedPaymentMethod == PaymentMethod.COD,
                    onSelect = { selectedPaymentMethod = PaymentMethod.COD },
                    icon = Icons.Default.Done,
                    title = "Cash on Delivery",
                    subtitle = "Pay when you receive"
                )
            }

            // Spacing at bottom
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Pay Now Button
        if (selectedPaymentMethod != null) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Show warning if no address selected
                    if (selectedAddress == null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "Please select a delivery address to continue",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (selectedAddress != null) {
                                showPaymentProcessing = true
                                // Simulate payment processing
                                GlobalScope.launch {
                                    delay(2000)
                                    showPaymentProcessing = false
                                    showSuccessDialog = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !showPaymentProcessing && selectedAddress != null
                    ) {
                        if (showPaymentProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Processing...")
                        } else {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Pay ₹${String.format("%.2f", cartTotal)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class PaymentMethod {
    UPI, CARD, NET_BANKING, WALLET, COD
}

@Composable
fun PaymentMethodCard(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onSelect
            )
        }
    }
}

@Composable
fun PaymentSuccessDialog(
    amount: Double,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Surface(
                shape = CircleShape,
                color = Color(0xFF00BFA5).copy(alpha = 0.2f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF00BFA5),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        },
        title = {
            Text(
                "Payment Successful!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Your payment of ${
                        String.format(
                            "%.2f",
                            amount
                        )
                    } has been processed successfully.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Order ID: #${(10000..99999).random()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Thank you for shopping with Techxon!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue Shopping", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
// ------- END CHECKOUT SCREEN RELATED CODE ---------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelectionDialog(
    addresses: List<Address>,
    selectedAddress: Address?,
    onAddressSelected: (Address) -> Unit,
    onAddNew: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Delivery Address",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (addresses.isEmpty()) {
                    Text(
                        "No addresses available. Please add a delivery address.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {
                        items(addresses) { address ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAddressSelected(address) },
                                shape = RoundedCornerShape(12.dp),
                                border = if (selectedAddress?.id == address.id) {
                                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                                } else {
                                    BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                                },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedAddress?.id == address.id)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            address.label,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (address.isDefault) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = MaterialTheme.colorScheme.primary
                                            ) {
                                                Text(
                                                    text = "DEFAULT",
                                                    modifier = Modifier.padding(
                                                        horizontal = 6.dp,
                                                        vertical = 2.dp
                                                    ),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        address.fullName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        "${address.addressLine1}${if (address.addressLine2.isNotEmpty()) ", ${address.addressLine2}" else ""}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "${address.city}, ${address.state} - ${address.zipCode}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "Phone: ${address.phoneNumber}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onAddNew,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add New Address")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Close")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun CartItemCard(cartItem: CartItem, viewModel: EcommerceViewModel) {
    var isDeleting by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isDeleting) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = cartItem.product.imageUrl,
                contentDescription = cartItem.product.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = cartItem.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${String.format("%.2f", cartItem.product.discountedPrice)} each",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "Subtotal: ${String.format("%.2f", cartItem.subtotal)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Quantity Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledIconButton(
                        onClick = {
                            viewModel.updateCartItemQuantity(
                                cartItem.product.id,
                                cartItem.quantity - 1
                            )
                        },
                        modifier = Modifier.size(36.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Decrease quantity",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.widthIn(min = 40.dp)
                    ) {
                        Text(
                            text = "${cartItem.quantity}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    FilledIconButton(
                        onClick = {
                            viewModel.updateCartItemQuantity(
                                cartItem.product.id,
                                cartItem.quantity + 1
                            )
                        },
                        modifier = Modifier.size(36.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "Increase quantity",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Delete Button
            FilledIconButton(
                onClick = {
                    isDeleting = true
                    viewModel.removeFromCart(cartItem.product.id)
                },
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from cart",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(
    aiViewModel: AIShoppingAssistantViewModel,
    ecommerceViewModel: EcommerceViewModel
) {
    val messages by aiViewModel.messages.collectAsState()
    val isLoading by aiViewModel.isLoading.collectAsState()
    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Enhanced Header with Gradient
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            modifier = Modifier.size(50.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                "Pookie",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                "Your AI Shopping Assistant",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }

                    IconButton(
                        onClick = { aiViewModel.clearChat() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Clear chat",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        // Messages with Enhanced Styling
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                ChatMessageBubble(message)
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Animated dots
                                repeat(3) { index ->
                                    val infiniteTransition = rememberInfiniteTransition()
                                    val scale by infiniteTransition.animateFloat(
                                        initialValue = 0.5f,
                                        targetValue = 1.2f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(600),
                                            repeatMode = RepeatMode.Reverse,
                                            initialStartOffset = StartOffset(index * 150)
                                        )
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .scale(scale)
                                            .background(
                                                MaterialTheme.colorScheme.onSecondaryContainer,
                                                CircleShape
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Enhanced Input Area
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        placeholder = {
                            Text(
                                "Ask me anything...",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    FloatingActionButton(
                        onClick = {
                            if (userInput.isNotBlank()) {
                                aiViewModel.sendMessage(userInput)
                                userInput = ""
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Quick Suggestions
                if (userInput.isBlank() && messages.size <= 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Try asking:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            listOf(
                                "Best deals" to "🔥",
                                "Electronics" to "📱",
                                "Gift ideas" to "🎁"
                            )
                        ) { (text, emoji) ->
                            SuggestionChip(
                                onClick = { userInput = "Show me $text" },
                                label = {
                                    Text(
                                        "$emoji $text",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isUser) {
            // AI Avatar
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.size(32.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = if (message.isUser) 16.dp else 4.dp,
                topEnd = if (message.isUser) 4.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = if (message.isUser) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            },
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(14.dp),
                color = if (message.isUser) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (message.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // User Avatar
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(32.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
