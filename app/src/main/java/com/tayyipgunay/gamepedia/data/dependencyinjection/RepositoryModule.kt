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



@Module // Bu sınıfın bir Hilt Modülü olduğunu belirtir.
@InstallIn(SingletonComponent::class) // Bu modülün SingletonComponent'e bağlı olduğunu belirtir.
object RepositoryModule { // Modül, bir singleton nesnesi olarak tanımlanır.

    // HomeRepository örneğini sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideHomeRepository(homeApi: HomeApi): HomeRepository {
        // HomeRepositoryImpl örneğini oluşturur ve döner.
        return HomeRepositoryImpl(homeApi)
    }

    // DetailsRepository örneğini sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideDetailsRepository(detailsApi: DetailsApi): DetailsRepository {
        // DetailsRepositoryImpl örneğini oluşturur ve döner.
        return DetailsRepositoryImpl(detailsApi)
    }

    // VideoPlayerRepository örneğini sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideVideoPlayerRepository(mediaApi: MediaApi): VideoPlayerRepository {
        // MediaRepositoryImpl örneğini oluşturur ve döner.
        return MediaRepositoryImpl(mediaApi)
    }

    // LocalGameRepository örneğini sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideLocalGameRepository(gameDao: GameDao): LocalGameRepository {
        // LocalGameRepositoryImpl örneğini oluşturur ve döner.
        return LocalGameRepositoryImpl(gameDao)
    }
}