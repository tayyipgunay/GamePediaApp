package com.tayyipgunay.gamepedia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tayyipgunay.gamepedia.domain.model.Genres

/*@Database(entities = [GameEntity::class], version = 1)
@TypeConverters(GenresConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }*/
// Veritabanı sınıfını tanımlar. entities parametresi ile kullanılacak entity'leri belirtir.
// version parametresi ile veritabanı sürümü belirlenir.
@Database(entities = [GameEntity::class], version = 1)
// TypeConverters ile özel dönüştürücüleri (converters) belirtir.
@TypeConverters(GenresConverter::class)
abstract class AppDatabase : RoomDatabase() {
    // GameDao arayüzünü (interface) kullanarak veritabanı işlemlerini gerçekleştirecek metodu tanımlar.
    abstract fun gameDao(): GameDao
}

