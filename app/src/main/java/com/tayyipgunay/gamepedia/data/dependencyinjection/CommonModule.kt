package com.tayyipgunay.gamepedia.data.dependencyinjection

import com.tayyipgunay.gamepedia.domain.model.Banner
import com.tayyipgunay.gamepedia.domain.model.Game
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Provides
    @Singleton
    fun provideGameList(): ArrayList<Game> = ArrayList()

    @Provides
    @Singleton
    fun provideBannerList(): ArrayList<Banner> = ArrayList()
}