package com.tayyipgunay.gamepedia.data.dependencyinjection

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.tayyipgunay.gamepedia.data.local.GameDao
import com.tayyipgunay.gamepedia.data.remote.api.DetailsApi
import com.tayyipgunay.gamepedia.data.remote.api.HomeApi
import com.tayyipgunay.gamepedia.data.remote.api.MediaApi
import com.tayyipgunay.gamepedia.data.repository.localgamerepositoryimpl.LocalGameRepositoryImpl
import com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.details.DetailsRepositoryImpl
import com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.home.HomeRepositoryImpl
import com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.mediaTrailer.MediaRepositoryImpl
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.DetailsRepository
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.VideoPlayerRepository
import dagger.Provides



    @Module
    @InstallIn(SingletonComponent::class)
    object RepositoryModule {

        @Provides
        @Singleton
        fun provideHomeRepository(homeApi: HomeApi): HomeRepository {
            return HomeRepositoryImpl(homeApi)
        }//nedne impl değilde interface inejekte ederiz

        @Provides
        @Singleton
        fun provideDetailsRepository(detailsApi: DetailsApi): DetailsRepository {
            return DetailsRepositoryImpl(detailsApi)
        }

        @Provides
        @Singleton
        fun provideVideoPlayerRepository(mediaApi: MediaApi): VideoPlayerRepository {
            return MediaRepositoryImpl(mediaApi)
        }

        @Provides
        @Singleton
        fun provideLocalGameRepository(gameDao: GameDao): LocalGameRepository {
            return LocalGameRepositoryImpl(gameDao)
        }

    }

