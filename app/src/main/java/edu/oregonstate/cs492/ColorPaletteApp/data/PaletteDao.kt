package edu.oregonstate.cs492.ColorPaletteApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PaletteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(palette: Palette)

    @Delete
    suspend fun delete(palette: Palette)

    @Query("SELECT * FROM palette")
    fun getAll(): Flow<List<Palette>>

    @Query("DELETE FROM palette")
    suspend fun deleteAll()
}