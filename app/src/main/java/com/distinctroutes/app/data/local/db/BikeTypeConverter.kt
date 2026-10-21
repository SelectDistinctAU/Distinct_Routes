package com.distinctroutes.app.data.local.db

import androidx.room.TypeConverter
import com.distinctroutes.app.domain.model.BikeType

/**
 * Room TypeConverter mapping between BikeType enum and database String.
 */
class BikeTypeConverter {

    @TypeConverter
    fun fromBikeType(bikeType: BikeType?): String {
        return bikeType?.name ?: BikeType.STANDARD.name
    }

    @TypeConverter
    fun toBikeType(value: String?): BikeType {
        if (value.isNullOrBlank()) return BikeType.STANDARD
        return try {
            BikeType.valueOf(value)
        } catch (e: Exception) {
            BikeType.STANDARD
        }
    }
}