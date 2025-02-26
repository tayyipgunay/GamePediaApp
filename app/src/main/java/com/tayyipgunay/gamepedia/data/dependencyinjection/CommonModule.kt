package com.tayyipgunay.gamepedia.data.dependencyinjection

import com.tayyipgunay.gamepedia.domain.model.Banner
import com.tayyipgunay.gamepedia.domain.model.Game
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module // Bu sınıfın bir Hilt Modülü olduğunu belirtir.
@InstallIn(SingletonComponent::class) // Bu modülün SingletonComponent'e bağlı olduğunu belirtir.
object CommonModule { // Modül, bir singleton nesnesi olarak tanımlanır.

    // Game listesini sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideGameList(): ArrayList<Game> = ArrayList() // Boş bir Game listesi döner.

    // Banner listesini sağlayan bir fonksiyon.
    @Provides // Bu fonksiyonun bir bağımlılık sağladığını belirtir.
    @Singleton // Bu bağımlılığın uygulama genelinde tek bir örnek (singleton) olacağını belirtir.
    fun provideBannerList(): ArrayList<Banner> = ArrayList() // Boş bir Banner listesi döner.
}