# 🗄️ MongoDB Database Integration Guide - TECHXON

## ✅ What's Been Implemented

I've created a **complete Backend API** that connects your Android app to MongoDB!

---

## 📁 Files Created

### Backend API:

1. **`backend/server.js`** (566 lines) - Complete Express + MongoDB API
2. **`backend/package.json`** - Node.js dependencies

---

## 🏗️ Architecture

```
Android App (Kotlin)
        ↓ HTTP/REST
    Backend API (Node.js + Express)
        ↓ Mongoose ORM
    MongoDB Database (mongodb://127.0.0.1:27017/Techxon)
```

---

## 🚀 Setup Instructions

### Step 1: Install MongoDB

**Windows:**

```bash
# Download from: https://www.mongodb.com/try/download/community
# Or use Chocolatey:
choco install mongodb

# Start MongoDB service:
net start MongoDB
```

**macOS:**

```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

**Linux:**

```bash
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -
sudo apt-get install -y mongodb-org
sudo systemctl start mongod
```

### Step 2: Verify MongoDB is Running

```bash
# Check if MongoDB is running
mongo --eval "db.version()"

# Or connect to MongoDB shell
mongosh

# In mongosh:
show dbs
use Techxon
```

### Step 3: Install Node.js Dependencies

```bash
cd backend
npm install
```

This installs:

- `express` - Web framework
- `mongoose` - MongoDB ORM
- `cors` - Enable CORS
- `bcryptjs` - Password hashing
- `jsonwebtoken` - JWT authentication
- `dotenv` - Environment variables

### Step 4: Start the Backend Server

```bash
# Development mode (auto-restart on changes)
npm run dev

# Production mode
npm start
```

You should see:

```
🚀 Techxon API Server running on http://localhost:3000
📊 Database: mongodb://127.0.0.1:27017/Techxon
🔗 Health Check: http://localhost:3000/api/health
✅ Connected to MongoDB - Techxon Database
```

### Step 5: Test the API

Open browser or use curl:

```bash
curl http://localhost:3000/api/health
```

Expected response:

```json
{
  "status": "OK",
  "message": "Techxon API is running",
  "database": "Connected"
}
```

---

## 📡 API Endpoints

### Authentication

#### Register User

```http
POST http://localhost:3000/api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "+91-9876543210"
}
```

**Response:**

```json
{
  "message": "User registered successfully",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+91-9876543210",
    "profileImage": ""
  }
}
```

#### Login User

```http
POST http://localhost:3000/api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### User Profile

#### Get Profile

```http
GET http://localhost:3000/api/user/profile
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Update Profile

```http
PUT http://localhost:3000/api/user/profile
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "name": "John Updated",
  "phone": "+91-9876543210",
  "profileImage": "https://example.com/photo.jpg"
}
```

### Addresses

#### Add Address

```http
POST http://localhost:3000/api/user/addresses
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "label": "Home",
  "fullName": "John Doe",
  "phoneNumber": "+91-9876543210",
  "addressLine1": "123 Main St",
  "addressLine2": "Apt 4B",
  "city": "Mumbai",
  "state": "Maharashtra",
  "zipCode": "400001",
  "isDefault": true
}
```

#### Update Address

```http
PUT http://localhost:3000/api/user/addresses/:addressId
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Delete Address

```http
DELETE http://localhost:3000/api/user/addresses/:addressId
Authorization: Bearer YOUR_JWT_TOKEN
```

### Wishlist

#### Add to Wishlist

```http
POST http://localhost:3000/api/user/wishlist/:productId
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Remove from Wishlist

```http
DELETE http://localhost:3000/api/user/wishlist/:productId
Authorization: Bearer YOUR_JWT_TOKEN
```

### Orders

#### Create Order

```http
POST http://localhost:3000/api/orders
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "items": [
    {
      "productId": "P001",
      "productName": "iPhone 15 Pro Max",
      "quantity": 1,
      "price": 134900
    }
  ],
  "subtotal": 134900,
  "discount": 10000,
  "total": 124900,
  "paymentMethod": "COD",
  "deliveryAddress": {
    "fullName": "John Doe",
    "phoneNumber": "+91-9876543210",
    "addressLine1": "123 Main St",
    "city": "Mumbai",
    "state": "Maharashtra",
    "zipCode": "400001"
  }
}
```

#### Get All Orders

```http
GET http://localhost:3000/api/orders
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Get Order by ID

```http
GET http://localhost:3000/api/orders/:orderId
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## 🗄️ Database Schema

### User Collection

```javascript
{
  _id: ObjectId,
  name: String,
  email: String (unique),
  password: String (hashed),
  phone: String,
  profileImage: String,
  addresses: [{
    id: String,
    label: String,
    fullName: String,
    phoneNumber: String,
    addressLine1: String,
    addressLine2: String,
    city: String,
    state: String,
    zipCode: String,
    isDefault: Boolean
  }],
  wishlist: [String], // Product IDs
  cart: [{
    productId: String,
    quantity: Number
  }],
  orders: [{
    orderId: String,
    items: Array,
    total: Number,
    status: String,
    orderDate: Date,
    deliveryAddress: String,
    paymentMethod: String
  }],
  createdAt: Date,
  lastLogin: Date
}
```

### Order Collection

```javascript
{
  _id: ObjectId,
  userId: ObjectId (ref: User),
  orderId: String (unique),
  items: [{
    productId: String,
    productName: String,
    quantity: Number,
    price: Number
  }],
  subtotal: Number,
  discount: Number,
  total: Number,
  status: String, // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
  paymentMethod: String,
  paymentStatus: String, // PENDING, PAID, FAILED
  deliveryAddress: {
    fullName: String,
    phoneNumber: String,
    addressLine1: String,
    addressLine2: String,
    city: String,
    state: String,
    zipCode: String
  },
  trackingNumber: String,
  estimatedDelivery: Date,
  createdAt: Date,
  updatedAt: Date
}
```

---

## 🔐 Security Features

1. **Password Hashing**: Bcrypt with salt rounds
2. **JWT Authentication**: 30-day expiry tokens
3. **Protected Routes**: Bearer token required
4. **Input Validation**: All inputs validated
5. **Error Handling**: Comprehensive error messages

---

## 🧪 Testing the API

### Using curl:

```bash
# Register
curl -X POST http://localhost:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@test.com","password":"test123"}'

# Login
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"test123"}'

# Get Profile (use token from login)
curl http://localhost:3000/api/user/profile \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Using Postman:

1. Import endpoints
2. Set Authorization to "Bearer Token"
3. Paste JWT token from login response
4. Test all endpoints

---

## 📱 Android Integration (Next Steps)

### Add Dependencies to `build.gradle.kts`:

```kotlin
dependencies {
    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // OkHttp for HTTP client
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Coroutines for async operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

### Create API Service:

```kotlin
// api/TechxonApiService.kt
interface TechxonApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @GET("user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserResponse>
    
    // Add more endpoints...
}

// Create Retrofit instance
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/" // Android emulator
    // private const val BASE_URL = "http://localhost:3000/api/" // Physical device on same network
    
    val apiService: TechxonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TechxonApiService::class.java)
    }
}
```

---

## 🌐 Network Configuration for Android

### For Emulator:

```kotlin
const val BASE_URL = "http://10.0.2.2:3000/api/"
```

### For Physical Device:

```kotlin
// Find your computer's IP address:
// Windows: ipconfig
// Mac/Linux: ifconfig
const val BASE_URL = "http://192.168.1.100:3000/api/"
```

### Add Internet Permission (`AndroidManifest.xml`):

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 📊 MongoDB Monitoring

### View Database in MongoDB Compass:

1. Download MongoDB Compass
2. Connect to: `mongodb://127.0.0.1:27017`
3. Select `Techxon` database
4. View collections: `users`, `orders`

### Using MongoDB Shell:

```bash
mongosh

use Techxon
db.users.find().pretty()
db.orders.find().pretty()
db.users.countDocuments()
```

---

## 🔧 Environment Variables

Create `.env` file in `backend/`:

```env
PORT=3000
MONGODB_URI=mongodb://127.0.0.1:27017/Techxon
JWT_SECRET=your_super_secret_key_here_change_in_production
JWT_EXPIRE=30d
NODE_ENV=development
```

Update `server.js` to use `.env`:

```javascript
require('dotenv').config();
const PORT = process.env.PORT || 3000;
const JWT_SECRET = process.env.JWT_SECRET;
```

---

## 🚨 Troubleshooting

### MongoDB not starting?

```bash
# Windows
net start MongoDB

# Mac
brew services start mongodb-community

# Linux
sudo systemctl start mongod
```

### Cannot connect to database?

```bash
# Check if MongoDB is running
mongosh --eval "db.version()"

# Check connection string
mongodb://127.0.0.1:27017/Techxon
```

### Port 3000 already in use?

```javascript
// Change port in server.js
const PORT = 3001; // or any available port
```

### CORS errors?

Already handled! The server has CORS enabled:

```javascript
app.use(cors());
```

---

## 📈 Performance Tips

1. **Indexes**: Add indexes for frequently queried fields

```javascript
userSchema.index({ email: 1 });
orderSchema.index({ userId: 1, createdAt: -1 });
```

2. **Connection Pooling**: Already handled by Mongoose

3. **Caching**: Add Redis for session management

4. **Rate Limiting**: Add express-rate-limit

---

## 🎯 Next Steps

1. ✅ **Backend API**: Complete
2. ✅ **MongoDB Schema**: Designed
3. ⏳ **Android Integration**: Add Retrofit (see above)
4. ⏳ **Update AuthViewModel**: Connect to real API
5. ⏳ **Test End-to-End**: Register → Login → Profile

---

## 📚 Additional Resources

- [MongoDB Documentation](https://docs.mongodb.com/)
- [Mongoose Guide](https://mongoosejs.com/docs/guide.html)
- [Express.js](https://expressjs.com/)
- [JWT Authentication](https://jwt.io/)
- [Retrofit Android](https://square.github.io/retrofit/)

---

## ✅ Status

**Backend API**: ✅ Complete and ready to use!
**Database**: ✅ Schema designed
**Documentation**: ✅ Complete
**Android Integration**: 📝 Code examples provided

---

**Your MongoDB database backend is ready!** 🎉

Just run:

```bash
cd backend
npm install
npm start
```

Then connect your Android app! 🚀
