package com.tayyipgunay.gamepedia.presentation.favorites.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.gamepedia.domain.usecase.favorites.deleteFavoriteGameFromRoomUseCase
import com.tayyipgunay.gamepedia.domain.usecase.favorites.getFavoritesGameFromRoomUseCase
import com.tayyipgunay.gamepedia.domain.usecase.details.isGameFavoritedUseCase
import com.tayyipgunay.gamepedia.presentation.favorites.event.FavoriteEvent
import com.tayyipgunay.gamepedia.presentation.favorites.state.GetAllGamesState
import com.tayyipgunay.gamepedia.presentation.favorites.state.DeleteGameState
import com.tayyipgunay.gamepedia.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesGamesUseCase: getFavoritesGameFromRoomUseCase,
    private val deleteFavoriteGameFromRoomUseCase: deleteFavoriteGameFromRoomUseCase,
    private val isGameFavoritedUseCase: isGameFavoritedUseCase
) : ViewModel() {

    // Favori oyunları yönetmek için MutableLiveData
    private val _getFavoriteState = MutableLiveData<GetAllGamesState>(GetAllGamesState())
    val getFavoriteState: LiveData<GetAllGamesState> get() = _getFavoriteState

    // Favori oyun silme işlemini yönetmek için MutableLiveData
    private val _deleteFavoriteState = MutableLiveData<DeleteGameState>(DeleteGameState())
    val deleteFavoriteState: LiveData<DeleteGameState> get() = _deleteFavoriteState

    /**
     * Favori oyunları veritabanından çeken fonksiyon.
     */
    fun getFavoritesGames() {
        println("getFavoritesGames çalıştı")

        // İlk olarak yükleme durumunu gösterir
        _getFavoriteState.value = _getFavoriteState.value?.copy(
            isLoading = true,
            games = null,
            errorMessage = null
        )

        println("Coroutine çalışma evresi başladı")
        viewModelScope.launch(Dispatchers.IO) { // IO thread'inde çalıştır
            println("Coroutine içinde çalışıyor")
            val result = getFavoritesGamesUseCase.executegetFavoritesGameFromRoomUseCase()
            println("Result çağrısı tamamlandı")

            withContext(Dispatchers.Main) { // UI thread'e geri dön
                println("Dispatcher Main içinde çalışıyoruz")

                when (result) {
                    is Resource.Success -> {
                        println("ViewModel getFavorite success: " + result.data?.get(0))

                        _getFavoriteState.value = _getFavoriteState.value?.copy(
                            isLoading = false,
                            games = result.data,
                            errorMessage = null
                        )

                        println("ViewModel getFavorite success tamamlandı")
                        println(result.data?.get(0)?.backgroundImage +" -------")
                    }

                    is Resource.Error -> {
                        println("ViewModel getFavorite error başladı")

                        _getFavoriteState.value = _getFavoriteState.value?.copy(
                            isLoading = false,
                            games = null,
                            errorMessage = result.message
                        )

                        println("ViewModel getFavorite error tamamlandı")
                    }

                    is Resource.Loading -> {
                        println("ViewModel getFavorite loading içinde")

                        _getFavoriteState.value = _getFavoriteState.value?.copy(
                            isLoading = true
                        )

                        println("ViewModel getFavorite loading tamamlandı")
                    }
                }
            }
        }
    }

    /**
     * Belirtilen oyun ID'sine sahip favori oyunu silen fonksiyon.
     */
    fun deleteFavoriteGame(gameId: Int) {
        // İlk olarak yükleme durumunu gösterir
        _deleteFavoriteState.value = _deleteFavoriteState.value?.copy(
            isLoading = true,
            isSuccess = false,
            errorMessage = null
        )

        viewModelScope.launch(Dispatchers.IO) { // IO thread'inde çalıştır
            val result = deleteFavoriteGameFromRoomUseCase.executeDeleteFavoriteGame(gameId)

            withContext(Dispatchers.Main) { // UI thread'e geri dön
                println("Dispatcher Main içindeyiz")

                when (result) {
                    is Resource.Success -> {
                        println("ViewModel deleteFavoriteGame success başladı")

                        _deleteFavoriteState.value = _deleteFavoriteState.value?.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }

                    is Resource.Error -> {
                        println("ViewModel deleteFavoriteGame error başladı")

                        _deleteFavoriteState.value = _deleteFavoriteState.value?.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = result.message
                        )
                    }

                    is Resource.Loading -> {
                        println("ViewModel deleteFavoriteGame loading içinde")

                        _deleteFavoriteState.value = _deleteFavoriteState.value?.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    /**
     * UI'dan gelen olayları yöneten fonksiyon.
     * @param event Kullanıcının gerçekleştirdiği olay
     */
    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.getFavoritesGames -> {
                getFavoritesGames()
            }
            is FavoriteEvent.DeleteFavorite -> {
                deleteFavoriteGame(event.gameId)
            }

            else -> {

            }
        }
    }
}


