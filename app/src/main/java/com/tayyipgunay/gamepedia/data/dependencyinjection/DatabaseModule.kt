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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "game_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGameDao(appDatabase: AppDatabase): GameDao {
        return appDatabase.gameDao()
    }


}