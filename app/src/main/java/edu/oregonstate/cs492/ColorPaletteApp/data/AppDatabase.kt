package edu.oregonstate.cs492.ColorPaletteApp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Palette::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun paletteDao(): PaletteDao

    companion object {
        const val DATABASE_NAME = "palette-db"

        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}