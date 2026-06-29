package com.example.appdopt.core.data.local

import androidx.room.TypeConverter
import com.example.appdopt.core.model.*

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> = value.split(",").filter { it.isNotEmpty() }

    @TypeConverter
    fun fromSpecies(value: Species): String = value.name

    @TypeConverter
    fun toSpecies(value: String): Species = Species.valueOf(value)

    @TypeConverter
    fun fromGender(value: Gender): String = value.name

    @TypeConverter
    fun toGender(value: String): Gender = Gender.valueOf(value)

    @TypeConverter
    fun fromSize(value: Size): String = value.name

    @TypeConverter
    fun toSize(value: String): Size = Size.valueOf(value)

    @TypeConverter
    fun fromAgeGroup(value: AgeGroup): String = value.name

    @TypeConverter
    fun toAgeGroup(value: String): AgeGroup = AgeGroup.valueOf(value)

    @TypeConverter
    fun fromRequestStatus(value: RequestStatus): String = value.name

    @TypeConverter
    fun toRequestStatus(value: String): RequestStatus = RequestStatus.valueOf(value)
}
