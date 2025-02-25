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
class FavoritesViewModel @Inject constructor(private val getFavoritesGamesUseCase: getFavoritesGameFromRoomUseCase, private val deleteFavoriteGameFromRoomUseCase: deleteFavoriteGameFromRoomUseCase, private val isGameFavoritedUseCase: isGameFavoritedUseCase) : ViewModel() {


    private val _getFavoriteState = MutableLiveData<GetAllGamesState>(GetAllGamesState())
    val getFavoriteState: LiveData<GetAllGamesState> get() = _getFavoriteState


    private val _deleteFavoriteState = MutableLiveData<DeleteGameState>(DeleteGameState())
    val deleteFavoriteState: LiveData<DeleteGameState> get() = _deleteFavoriteState


    fun getFavoritesGames() {
        println("get favorties game çalıştı")
        _getFavoriteState.value = _getFavoriteState.value?.copy(
            isLoading = true,
            games = null,
            errorMessage = null
        )
        println("coroutinne çalışma evresi")
        viewModelScope.launch(Dispatchers.IO) {
            println("coroutine içindeyiz")
            val result = getFavoritesGamesUseCase.executegetFavoritesGameFromRoomUseCase()
            println("result çağrısı yapıldı bitti")
            withContext(Dispatchers.Main) {
                println("Dispatcher main başındayız.")
                when (result) {
                    is Resource.Success -> {
                        println("viewModel getFavorite success başındayız  " + result.data?.get(0))
                        _getFavoriteState.value = _getFavoriteState.value?.copy(
                            isLoading = false,
                            games = result.data,
                            errorMessage = null
                        )
                        println("viewModel getFavorite success sonundayız")

                    }

                    is Resource.Error -> {
                        println("viewModel getFavorite error başındayız")

                        _getFavoriteState.value = _getFavoriteState.value?.copy(
                            isLoading = false,
                            games = null,
                            errorMessage = result.message
                        )
                        println("viewModel getFavorite error sonundayız")

                    }

                    is Resource.Loading -> {//neden ünlem gerekti
                        println("viewModel getFavorite laoding içindeyiz")

                        _getFavoriteState.value = _getFavoriteState.value?.copy(
                            isLoading = true
                        )
                        println("viewModel getFavorite laoding sonundayız")

                    }
                }


            }
        }
    }

    fun deleteFavoriteGame(gameId: Int) {
        _deleteFavoriteState.value= _deleteFavoriteState.value?.copy(
            isLoading = true,
            isSuccess = false,
            errorMessage = null
        )



        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteFavoriteGameFromRoomUseCase.executeDeleteFavoriteGame(gameId)
            withContext(Dispatchers.Main) {
println("dispatcher main içindeyizzz.")
                when (result) {
                    is Resource.Success -> {
                        println("viewModel deleteFavoriteGame success başındayız")
                        _deleteFavoriteState.value = _deleteFavoriteState.value?.copy(
                            isLoading = false,
                            isSuccess =true,
                            errorMessage = null
                        )
                    }

                    is Resource.Error -> {
                        println("viewModel deleteFavoriteGame error başındayız")
                        _deleteFavoriteState.value = _deleteFavoriteState.value?.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = result.message
                        )
                    }


                    is Resource.Loading -> {
                        println("viewModel deleteFavoriteGame laoding içindeyiz")
                        _deleteFavoriteState.value = _deleteFavoriteState.value?.copy(
                            isLoading = true
                        )

                    }


                }
            }
        }
    }
    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.getFavoritesGames -> {
                getFavoritesGames()

            }
            is FavoriteEvent.DeleteFavorite -> {
                deleteFavoriteGame(event.gameId)
            }
        }
    }
}


