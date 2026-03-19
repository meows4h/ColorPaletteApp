package edu.oregonstate.cs492.ColorPaletteApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "palette")
@TypeConverters(Converters::class)
data class Palette (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val colors: List<String>
)

class Converters {

    @TypeConverter
    fun fromColorList(colors: List<String>): String {
        return colors.joinToString(",")
    }

    @TypeConverter
    fun toColorList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}