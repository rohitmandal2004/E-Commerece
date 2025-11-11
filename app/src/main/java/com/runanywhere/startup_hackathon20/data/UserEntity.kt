package com.runanywhere.startup_hackathon20.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.runanywhere.startup_hackathon20.models.Address

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val id: String,
    val name: String,
    val phone: String,
    val password: String,
    val addressesJson: String // Store addresses as JSON string
)

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromAddressList(addresses: List<Address>): String {
        return gson.toJson(addresses)
    }

    @TypeConverter
    fun toAddressList(json: String): List<Address> {
        val type = object : TypeToken<List<Address>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
