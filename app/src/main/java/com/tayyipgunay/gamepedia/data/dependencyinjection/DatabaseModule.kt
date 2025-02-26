package com.tayyipgunay.gamepedia.data.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.tayyipgunay.gamepedia.data.local.AppDatabase
import com.tayyipgunay.gamepedia.data.local.GameDao
import com.tayyipgunay.gamepedia.data.repository.localgamerepositoryimpl.LocalGameRepositoryImpl
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // Bu sınıfın bir Hilt Modülü olduğunu belirtir.
@InstallIn(SingletonComponent::class) // Bu modülün SingletonComponent'e bağlı olduğunu belirtir.
object DatabaseModule { // Modül, bir singleton nesnesi olarak tanımlanır.

    // Room veritabanını sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        // Room veritabanını oluşturur ve döner.
        return Room.databaseBuilder(
            context, // Uygulama context'i.
            AppDatabase::class.java, // Veritabanı sınıfı.
            "game_database" // Veritabanı adı.
        ).build() // Veritabanını oluşturur.
    }

    // GameDao'yu sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideGameDao(appDatabase: AppDatabase): GameDao {
        // Veritabanından GameDao'yu döner.
        return appDatabase.gameDao()
    }
}