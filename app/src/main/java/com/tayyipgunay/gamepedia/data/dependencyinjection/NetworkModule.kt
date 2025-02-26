package com.tayyipgunay.gamepedia.data.dependencyinjection

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
//
import android.content.Context
import androidx.room.Room
import com.tayyipgunay.gamepedia.data.local.AppDatabase
import com.tayyipgunay.gamepedia.data.local.GameDao
import com.tayyipgunay.gamepedia.data.remote.api.ApiService
import com.tayyipgunay.gamepedia.data.remote.api.DetailsApi
import com.tayyipgunay.gamepedia.data.remote.api.HomeApi
import com.tayyipgunay.gamepedia.data.remote.api.MediaApi
import com.tayyipgunay.gamepedia.util.Constants
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module // Bu sınıfın bir Hilt Modülü olduğunu belirtir.
@InstallIn(SingletonComponent::class) // Bu modülün SingletonComponent'e bağlı olduğunu belirtir.
object NetworkModule { // Modül, bir singleton nesnesi olarak tanımlanır.

    // Retrofit örneğini sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideRetrofit(): Retrofit {
        // Retrofit örneğini oluşturur ve döner.
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // API'nin temel URL'si.
            .addConverterFactory(GsonConverterFactory.create()) // JSON verilerini Java/Kotlin nesnelerine dönüştürmek için Gson kullanır.
            .build() // Retrofit örneğini oluşturur.
    }

    // HomeApi arayüzünü sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideHomeApi(retrofit: Retrofit): HomeApi {
        // Retrofit üzerinden HomeApi arayüzünü oluşturur ve döner.
        return retrofit.create(HomeApi::class.java)
    }

    // DetailsApi arayüzünü sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideDetailsApi(retrofit: Retrofit): DetailsApi {
        // Retrofit üzerinden DetailsApi arayüzünü oluşturur ve döner.
        return retrofit.create(DetailsApi::class.java)
    }

    // MediaApi arayüzünü sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideMediaApi(retrofit: Retrofit): MediaApi {
        // Retrofit üzerinden MediaApi arayüzünü oluşturur ve döner.
        return retrofit.create(MediaApi::class.java)
    }
}
