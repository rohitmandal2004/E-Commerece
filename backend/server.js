const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const app = express();
const PORT = process.env.PORT || 3000;
const JWT_SECRET = 'techxon_secret_key_change_in_production';

// Middleware
app.use(cors());
app.use(express.json());

// MongoDB Connection
mongoose.connect('mongodb://127.0.0.1:27017/Techxon', {
    useNewUrlParser: true,
    useUnifiedTopology: true
})
.then(() => console.log('✅ Connected to MongoDB - Techxon Database'))
.catch(err => console.error('❌ MongoDB connection error:', err));

// User Schema
const userSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        trim: true
    },
    email: {
        type: String,
        required: true,
        unique: true,
        lowercase: true,
        trim: true
    },
    password: {
        type: String,
        required: true
    },
    phone: {
        type: String,
        default: ''
    },
    profileImage: {
        type: String,
        default: ''
    },
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
    wishlist: [{
        type: String // Product IDs
    }],
    cart: [{
        productId: String,
        quantity: Number
    }],
    orders: [{
        orderId: String,
        items: [{
            productId: String,
            quantity: Number,
            price: Number
        }],
        total: Number,
        status: String,
        orderDate: Date,
        deliveryAddress: String,
        paymentMethod: String
    }],
    createdAt: {
        type: Date,
        default: Date.now
    },
    lastLogin: {
        type: Date,
        default: Date.now
    }
});

const User = mongoose.model('User', userSchema);

// Order Schema
const orderSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    orderId: {
        type: String,
        required: true,
        unique: true
    },
    items: [{
        productId: String,
        productName: String,
        quantity: Number,
        price: Number
    }],
    subtotal: Number,
    discount: Number,
    total: Number,
    status: {
        type: String,
        enum: ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'],
        default: 'PENDING'
    },
    paymentMethod: String,
    paymentStatus: {
        type: String,
        enum: ['PENDING', 'PAID', 'FAILED'],
        default: 'PENDING'
    },
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
    createdAt: {
        type: Date,
        default: Date.now
    },
    updatedAt: {
        type: Date,
        default: Date.now
    }
});

const Order = mongoose.model('Order', orderSchema);

// Middleware to verify JWT token
const authenticateToken = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
        return res.status(401).json({ message: 'Access token required' });
    }

    jwt.verify(token, JWT_SECRET, (err, user) => {
        if (err) {
            return res.status(403).json({ message: 'Invalid or expired token' });
        }
        req.user = user;
        next();
    });
};

// =====================================================
// AUTH ROUTES
// =====================================================

// Register new user
app.post('/api/auth/register', async (req, res) => {
    try {
        const { name, email, password, phone } = req.body;

        // Validate input
        if (!name || !email || !password) {
            return res.status(400).json({ message: 'Name, email, and password are required' });
        }

        // Check if user already exists
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(400).json({ message: 'User with this email already exists' });
        }

        // Hash password
        const hashedPassword = await bcrypt.hash(password, 10);

        // Create new user
        const user = new User({
            name,
            email,
            password: hashedPassword,
            phone: phone || ''
        });

        await user.save();

        // Generate JWT token
        const token = jwt.sign(
            { userId: user._id, email: user.email },
            JWT_SECRET,
            { expiresIn: '30d' }
        );

        res.status(201).json({
            message: 'User registered successfully',
            token,
            user: {
                id: user._id,
                name: user.name,
                email: user.email,
                phone: user.phone,
                profileImage: user.profileImage
            }
        });
    } catch (error) {
        console.error('Registration error:', error);
        res.status(500).json({ message: 'Server error during registration' });
    }
});

// Login user
app.post('/api/auth/login', async (req, res) => {
    try {
        const { email, password } = req.body;

        // Validate input
        if (!email || !password) {
            return res.status(400).json({ message: 'Email and password are required' });
        }

        // Find user
        const user = await User.findOne({ email });
        if (!user) {
            return res.status(401).json({ message: 'Invalid email or password' });
        }

        // Verify password
        const isValidPassword = await bcrypt.compare(password, user.password);
        if (!isValidPassword) {
            return res.status(401).json({ message: 'Invalid email or password' });
        }

        // Update last login
        user.lastLogin = new Date();
        await user.save();

        // Generate JWT token
        const token = jwt.sign(
            { userId: user._id, email: user.email },
            JWT_SECRET,
            { expiresIn: '30d' }
        );

        res.json({
            message: 'Login successful',
            token,
            user: {
                id: user._id,
                name: user.name,
                email: user.email,
                phone: user.phone,
                profileImage: user.profileImage,
                addresses: user.addresses,
                wishlist: user.wishlist
            }
        });
    } catch (error) {
        console.error('Login error:', error);
        res.status(500).json({ message: 'Server error during login' });
    }
});

// =====================================================
// USER ROUTES
// =====================================================

// Get user profile
app.get('/api/user/profile', authenticateToken, async (req, res) => {
    try {
        const user = await User.findById(req.user.userId).select('-password');
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }
        res.json({ user });
    } catch (error) {
        console.error('Profile fetch error:', error);
        res.status(500).json({ message: 'Server error fetching profile' });
    }
});

// Update user profile
app.put('/api/user/profile', authenticateToken, async (req, res) => {
    try {
        const { name, phone, profileImage } = req.body;
        
        const user = await User.findById(req.user.userId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        if (name) user.name = name;
        if (phone) user.phone = phone;
        if (profileImage) user.profileImage = profileImage;

        await user.save();

        res.json({
            message: 'Profile updated successfully',
            user: {
                id: user._id,
                name: user.name,
                email: user.email,
                phone: user.phone,
                profileImage: user.profileImage
            }
        });
    } catch (error) {
        console.error('Profile update error:', error);
        res.status(500).json({ message: 'Server error updating profile' });
    }
});

// =====================================================
// ADDRESS ROUTES
// =====================================================

// Add address
app.post('/api/user/addresses', authenticateToken, async (req, res) => {
    try {
        const address = req.body;
        const user = await User.findById(req.user.userId);
        
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        user.addresses.push({
            ...address,
            id: Date.now().toString()
        });

        await user.save();

        res.status(201).json({
            message: 'Address added successfully',
            addresses: user.addresses
        });
    } catch (error) {
        console.error('Add address error:', error);
        res.status(500).json({ message: 'Server error adding address' });
    }
});

// Update address
app.put('/api/user/addresses/:addressId', authenticateToken, async (req, res) => {
    try {
        const { addressId } = req.params;
        const updatedAddress = req.body;
        
        const user = await User.findById(req.user.userId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        const addressIndex = user.addresses.findIndex(addr => addr.id === addressId);
        if (addressIndex === -1) {
            return res.status(404).json({ message: 'Address not found' });
        }

        user.addresses[addressIndex] = { ...updatedAddress, id: addressId };
        await user.save();

        res.json({
            message: 'Address updated successfully',
            addresses: user.addresses
        });
    } catch (error) {
        console.error('Update address error:', error);
        res.status(500).json({ message: 'Server error updating address' });
    }
});

// Delete address
app.delete('/api/user/addresses/:addressId', authenticateToken, async (req, res) => {
    try {
        const { addressId } = req.params;
        
        const user = await User.findById(req.user.userId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        user.addresses = user.addresses.filter(addr => addr.id !== addressId);
        await user.save();

        res.json({
            message: 'Address deleted successfully',
            addresses: user.addresses
        });
    } catch (error) {
        console.error('Delete address error:', error);
        res.status(500).json({ message: 'Server error deleting address' });
    }
});

// =====================================================
// WISHLIST ROUTES
// =====================================================

// Add to wishlist
app.post('/api/user/wishlist/:productId', authenticateToken, async (req, res) => {
    try {
        const { productId } = req.params;
        const user = await User.findById(req.user.userId);
        
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        if (!user.wishlist.includes(productId)) {
            user.wishlist.push(productId);
            await user.save();
        }

        res.json({
            message: 'Added to wishlist',
            wishlist: user.wishlist
        });
    } catch (error) {
        console.error('Add to wishlist error:', error);
        res.status(500).json({ message: 'Server error adding to wishlist' });
    }
});

// Remove from wishlist
app.delete('/api/user/wishlist/:productId', authenticateToken, async (req, res) => {
    try {
        const { productId } = req.params;
        const user = await User.findById(req.user.userId);
        
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        user.wishlist = user.wishlist.filter(id => id !== productId);
        await user.save();

        res.json({
            message: 'Removed from wishlist',
            wishlist: user.wishlist
        });
    } catch (error) {
        console.error('Remove from wishlist error:', error);
        res.status(500).json({ message: 'Server error removing from wishlist' });
    }
});

// =====================================================
// ORDER ROUTES
// =====================================================

// Create order
app.post('/api/orders', authenticateToken, async (req, res) => {
    try {
        const { items, subtotal, discount, total, paymentMethod, deliveryAddress } = req.body;
        
        // Generate order ID
        const orderId = `ORD${Date.now()}`;
        
        // Create order
        const order = new Order({
            userId: req.user.userId,
            orderId,
            items,
            subtotal,
            discount,
            total,
            paymentMethod,
            deliveryAddress,
            trackingNumber: `TXN${orderId}`,
            estimatedDelivery: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000) // 3 days
        });

        await order.save();

        // Add to user's orders
        const user = await User.findById(req.user.userId);
        user.orders.push({
            orderId: order.orderId,
            items: order.items,
            total: order.total,
            status: order.status,
            orderDate: order.createdAt,
            deliveryAddress: JSON.stringify(order.deliveryAddress),
            paymentMethod: order.paymentMethod
        });
        
        // Clear cart
        user.cart = [];
        await user.save();

        res.status(201).json({
            message: 'Order placed successfully',
            order: {
                orderId: order.orderId,
                trackingNumber: order.trackingNumber,
                total: order.total,
                status: order.status,
                estimatedDelivery: order.estimatedDelivery
            }
        });
    } catch (error) {
        console.error('Create order error:', error);
        res.status(500).json({ message: 'Server error creating order' });
    }
});

// Get user orders
app.get('/api/orders', authenticateToken, async (req, res) => {
    try {
        const orders = await Order.find({ userId: req.user.userId }).sort({ createdAt: -1 });
        res.json({ orders });
    } catch (error) {
        console.error('Fetch orders error:', error);
        res.status(500).json({ message: 'Server error fetching orders' });
    }
});

// Get order by ID
app.get('/api/orders/:orderId', authenticateToken, async (req, res) => {
    try {
        const { orderId } = req.params;
        const order = await Order.findOne({ orderId, userId: req.user.userId });
        
        if (!order) {
            return res.status(404).json({ message: 'Order not found' });
        }

        res.json({ order });
    } catch (error) {
        console.error('Fetch order error:', error);
        res.status(500).json({ message: 'Server error fetching order' });
    }
});

// =====================================================
// HEALTH CHECK
// =====================================================

app.get('/api/health', (req, res) => {
    res.json({
        status: 'OK',
        message: 'Techxon API is running',
        database: mongoose.connection.readyState === 1 ? 'Connected' : 'Disconnected'
    });
});

// Start server
app.listen(PORT, () => {
    console.log(`🚀 Techxon API Server running on http://localhost:${PORT}`);
    console.log(`📊 Database: mongodb://127.0.0.1:27017/Techxon`);
    console.log(`🔗 Health Check: http://localhost:${PORT}/api/health`);
});
