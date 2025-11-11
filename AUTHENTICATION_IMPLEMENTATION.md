# Authentication System Implementation

## Overview

Implemented a complete authentication system with Room database for persistent user storage, proper
email validation, and user registration checking.

## Features Implemented

### 1. **Database Layer (Room Database)**

- **UserEntity** (`app/src/main/java/com/runanywhere/startup_hackathon20/data/UserEntity.kt`)
    - Stores user information: email (primary key), id, name, phone, password
    - Addresses stored as JSON string for flexibility

- **UserDao** (`app/src/main/java/com/runanywhere/startup_hackathon20/data/UserDao.kt`)
    - CRUD operations for user data
    - `getUserByEmail()` - Retrieve user by email
    - `userExists()` - Check if email is already registered
    - `insertUser()` - Register new user
    - `updateUser()` - Update user information (including addresses)

- **AppDatabase** (`app/src/main/java/com/runanywhere/startup_hackathon20/data/AppDatabase.kt`)
    - Singleton pattern for database instance
    - Database name: `techxon_database`
    - Includes type converters for Address list serialization

### 2. **Authentication Logic**

- **Updated AuthViewModel** with database integration
- **Login Flow**:
    1. Check if email exists in database
    2. If not found → Show error: "User not registered. Please sign up first."
    3. If found → Validate password
    4. If password incorrect → Show error: "Invalid password"
    5. If successful → Load user data and addresses

- **Signup Flow**:
    1. Validate all fields are filled
    2. Check if email already exists
    3. If exists → Show error: "Email already registered. Please login."
    4. If new → Create user, save to database, and log them in

### 3. **Error Handling**

- Added `loginError` StateFlow in AuthViewModel
- Real-time error message updates in UI
- Clear, user-friendly error messages:
    - "User not registered. Please sign up first."
    - "Invalid password"
    - "Email already registered. Please login."
    - "Please fill all required fields"

### 4. **ViewModelFactory**

- **AuthViewModelFactory** (
  `app/src/main/java/com/runanywhere/startup_hackathon20/viewmodels/AuthViewModelFactory.kt`)
- Properly injects UserDao into AuthViewModel
- Used in MainActivity's EcommerceApp composable

### 5. **Persistent Address Storage**

- Addresses are saved to database when added
- Addresses are loaded from database on login
- Updates are persisted immediately

## Files Modified/Created

### Created Files:

1. `app/src/main/java/com/runanywhere/startup_hackathon20/data/UserEntity.kt`
2. `app/src/main/java/com/runanywhere/startup_hackathon20/data/UserDao.kt`
3. `app/src/main/java/com/runanywhere/startup_hackathon20/data/AppDatabase.kt`
4. `app/src/main/java/com/runanywhere/startup_hackathon20/viewmodels/AuthViewModelFactory.kt`

### Modified Files:

1. `app/build.gradle.kts` - Added Room compiler dependency (KSP)
2. `app/src/main/java/com/runanywhere/startup_hackathon20/viewmodels/AuthViewModel.kt`
    - Added database integration
    - Updated login/signup methods to be asynchronous
    - Added loginError StateFlow
    - Updated address management to persist to database
3. `app/src/main/java/com/runanywhere/startup_hackathon20/MainActivity.kt`
    - Updated EcommerceApp to initialize database
    - Updated AuthenticationScreen to observe loginError
    - Removed synchronous return values from login/signup

## How It Works

### User Registration:

1. User fills signup form (name, email, phone, password)
2. App checks if email already exists in database
3. If new email, user is created and saved to database
4. User is automatically logged in after successful registration

### User Login:

1. User enters email and password
2. App checks database for user with that email
3. If user not found → Error: "User not registered. Please sign up first."
4. If user found → Validates password
5. If password correct → User is logged in, data and addresses loaded
6. If password incorrect → Error: "Invalid password"

### Data Persistence:

- All user data stored in SQLite database via Room
- Survives app restarts
- Addresses automatically saved when added/modified
- Demo user (demo@techxon.com / demo123) automatically seeded on first run

## Testing

### Test Cases:

1. **New User Registration**
    - Enter new email → Should allow signup
    - Enter existing email → Should show "Email already registered"

2. **User Login**
    - Enter unregistered email → Should show "User not registered. Please sign up first."
    - Enter wrong password → Should show "Invalid password"
    - Enter correct credentials → Should login successfully

3. **Demo User**
    - Email: demo@techxon.com
    - Password: demo123
    - Has pre-configured address

4. **Data Persistence**
    - Register a new user
    - Close app completely
    - Reopen app and try to login with same credentials
    - Should work successfully with persisted data

## Technical Notes

- Database operations are asynchronous using Kotlin Coroutines
- ViewModelScope used for proper lifecycle management
- Type converters handle Address list serialization/deserialization
- Fallback to in-memory storage if database initialization fails
- Singleton pattern ensures single database instance
- KSP (Kotlin Symbol Processing) used for Room annotation processing

## Dependencies Added

```kotlin
id("com.google.devtools.ksp") version "2.0.21-1.0.27"
ksp("androidx.room:room-compiler:2.6.1")
```

## Future Enhancements (Optional)

- Password hashing for security
- Email validation (regex)
- Password strength requirements
- Forgot password functionality
- Profile picture support
- Social login integration
