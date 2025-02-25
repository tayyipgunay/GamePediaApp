package com.tayyipgunay.gamepedia.data.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.tayyipgunay.gamepedia.data.local.AppDatabase
import com.tayyipgunay.gamepedia.data.local.GameDao
import com.tayyipgunay.gamepedia.data.remote.api.ApiService
import com.tayyipgunay.gamepedia.data.remote.api.DetailsApi
import com.tayyipgunay.gamepedia.data.remote.api.HomeApi
import com.tayyipgunay.gamepedia.data.remote.api.MediaApi
import com.tayyipgunay.gamepedia.data.repository.localgamerepositoryimpl.LocalGameRepositoryImpl
import com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.details.DetailsRepositoryImpl
import com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.home.HomeRepositoryImpl
import com.tayyipgunay.gamepedia.data.repository.remotegamerepositoryimpl.mediaTrailer.MediaRepositoryImpl
import com.tayyipgunay.gamepedia.domain.model.Banner
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.DetailsRepository
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.HomeRepository
import com.tayyipgunay.gamepedia.domain.repository.remotegamerepository.VideoPlayerRepository
import com.tayyipgunay.gamepedia.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
object AppModule {


    /*   @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMediaApi(retrofit: Retrofit): MediaApi {
        return retrofit.create(MediaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailsApi(retrofit: Retrofit): DetailsApi {
        return retrofit.create(DetailsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }




    @Provides
    @Singleton
    fun provideHomeRepository(homeApi: HomeApi): HomeRepository {
        return HomeRepositoryImpl(homeApi)
    }

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
    fun provideGameList(): ArrayList<Game> = ArrayList()

    @Provides
    @Singleton
    fun provideBannerList(): ArrayList<Banner> = ArrayList()

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

    @Provides
    @Singleton
    fun provideLocalGameRepository(gameDao: GameDao): LocalGameRepository {
        return LocalGameRepositoryImpl(gameDao)
    }
}

*/

}






