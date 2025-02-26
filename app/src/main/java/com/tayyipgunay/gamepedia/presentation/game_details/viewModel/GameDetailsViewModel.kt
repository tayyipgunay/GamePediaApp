package com.tayyipgunay.gamepedia.presentation.game_details.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.usecase.details.GetGameDetailsUseCase
import com.tayyipgunay.gamepedia.domain.usecase.videoPlayer.GetGameTrailersUseCase
import com.tayyipgunay.gamepedia.domain.usecase.details.GetScreenShootsUseCase
import com.tayyipgunay.gamepedia.domain.usecase.details.insertGameIntoFavorites
import com.tayyipgunay.gamepedia.domain.usecase.details.isGameFavoritedUseCase
import com.tayyipgunay.gamepedia.presentation.game_details.event.DetailsFavoritesEvent
import com.tayyipgunay.gamepedia.presentation.game_details.state.InsertState
import com.tayyipgunay.gamepedia.presentation.game_details.state.ScreenShootState
import com.tayyipgunay.gamepedia.presentation.game_details.event.GameDetailsEvent
import com.tayyipgunay.gamepedia.presentation.game_details.state.isGameFavoritedState
import com.tayyipgunay.gamepedia.presentation.game_details.state.GameDetailsState
import com.tayyipgunay.gamepedia.presentation.videoPlayer.state.TrailerState
import com.tayyipgunay.gamepedia.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class GameDetailsViewModel @Inject constructor(private val gameDetailsUseCase: GetGameDetailsUseCase, private val getScreenShootsUseCase: GetScreenShootsUseCase, private val getGameTrailersUseCase: GetGameTrailersUseCase, private val insertFavoriteGameToRoomUseCase: insertGameIntoFavorites, private val isGameFavoritedUseCase: isGameFavoritedUseCase):ViewModel() {

    // Oyun detaylarının durumunu takip eder
    private val _gameDetailsState = MutableLiveData(GameDetailsState())
    val gameDetailsState: LiveData<GameDetailsState> = _gameDetailsState

    // Oyun fragmanlarının durumunu takip eder
    private val _gameTrailerState = MutableLiveData(TrailerState())
    val gameTrailerState: LiveData<TrailerState> get() = _gameTrailerState

    // Oyun ekran görüntülerinin durumunu takip eder
    private val _screenShootState = MutableLiveData(ScreenShootState())
    val screenShootState: LiveData<ScreenShootState> get() = _screenShootState

    // Favorilere ekleme durumunu takip eder
    private val _insertState = MutableLiveData(InsertState())
    val insertState: LiveData<InsertState> get() = _insertState

    // Oyunun favorilerde olup olmadığını takip eder
    private val _isGameFavorited = MutableLiveData(isGameFavoritedState())
    val isGameFavoritedState: LiveData<isGameFavoritedState> get() = _isGameFavorited


    /*
   MutableLiveData, başlangıçta null değer alır.
 Bu durumda, LiveData gözlemcileri (observers) null değerle karşılaşabilir ve NullPointerException riski oluşabilir.
private val _gameTrailerState = MutableLiveData<TrailerState>()//null olan
    val gameTrailerState: LiveData<TrailerState> = _gameTrailerState
*/


    /* MutableLiveData, başlangıçta varsayılan bir TrailerState değeri ile başlatılır.
 Bu sayede LiveData'nın değeri asla null olmaz ve gözlemcilerde null kontrolü yapmaya gerek kalmaz.
*/


// ViewModel içinde oyun detaylarını, fragmanlarını, ekran görüntülerini ve favorilere ekleme işlemlerini yöneten sınıf


        /**
         * Oyun detaylarını getiren fonksiyon
         */
        fun getGameDetails(gameId: Int) {
            // Yükleme başladığında isLoading'i true yap, hata mesajını ve mevcut veriyi temizle
            _gameDetailsState.value = gameDetailsState.value?.copy(
                isLoading = true,
                error = null,
                gameDetail = null
            )

            println("GET GAME DETAILS ÇAĞRILDI")

            // Arka planda oyun detaylarını çek
            viewModelScope.launch(Dispatchers.IO) {
                val gameDetails = gameDetailsUseCase.executeGetGameDetailsUseCase(gameId)
                withContext(Dispatchers.Main) {
                    when (gameDetails) {
                        is Resource.Success -> {
                            println("Başarılı: ${gameDetails.data?.title}")
                            _gameDetailsState.value = gameDetailsState.value?.copy(
                                gameDetail = gameDetails.data,
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _gameDetailsState.value = gameDetailsState.value?.copy(
                                isLoading = false,
                                error = gameDetails.message
                            )
                        }

                        is Resource.Loading -> {
                            _gameDetailsState.value = gameDetailsState.value?.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }

        /**
         * Oyun ekran görüntülerini getiren fonksiyon
         */
        fun getScreenShoots(gameSlug: String) {
            _screenShootState.value = screenShootState.value?.copy(
                error = null,
                gameScreenshoots = null,
                isLoading = true
            )

            viewModelScope.launch(Dispatchers.IO) {
                val result = getScreenShootsUseCase.executeGetScreenShoots(gameSlug)
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Resource.Success -> _screenShootState.value =
                            screenShootState.value?.copy(
                                isLoading = false,
                                gameScreenshoots = result.data
                            )

                        is Resource.Error -> {
                            println(result.message)
                            _screenShootState.value = screenShootState.value?.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }

                        is Resource.Loading -> _screenShootState.value =
                            screenShootState.value?.copy(
                                isLoading = true
                            )
                    }
                }
            }
        }

        /**
         * Oyun fragmanlarını getiren fonksiyon
         */
        fun getGameTrailer(gameId: Int) {
            _gameTrailerState.value = gameTrailerState.value?.copy(
                isLoading = true
            )

            viewModelScope.launch(Dispatchers.IO) {
                val gameTrailer = getGameTrailersUseCase.executeGetGameTrailers(gameId)
                println("ViewModel içindeyiz")

                withContext(Dispatchers.Main) {
                    when (gameTrailer) {
                        is Resource.Success -> _gameTrailerState.value =
                            _gameTrailerState.value?.copy(
                                gameTrailer = gameTrailer.data,
                                isLoading = false
                            )

                        is Resource.Error -> _gameTrailerState.value =
                            _gameTrailerState.value?.copy(
                                isLoading = false,
                                error = gameTrailer.message
                            )

                        is Resource.Loading -> _gameTrailerState.value =
                            _gameTrailerState.value?.copy(
                                isLoading = true
                            )
                    }
                }
            }
        }

        /**
         * Oyunu favorilere ekleyen fonksiyon
         */
        fun insertFavoriteGame(game: Game) {
            _insertState.value = _insertState.value!!.copy(
                isLoading = true,
                errorMessage = null,
                isSuccess = false
            )

            viewModelScope.launch(Dispatchers.IO) {
                val result = insertFavoriteGameToRoomUseCase.executeinsertGameIntoFavorites(game)
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Resource.Success -> {
                            _insertState.value = _insertState.value?.copy(
                                isLoading = false,
                                errorMessage = null,
                                isSuccess = true
                            )
                            println("Başarıyla favorilere eklendi: ${result.data}")
                        }

                        is Resource.Error -> {
                            _insertState.value = _insertState.value?.copy(
                                isLoading = false,
                                isSuccess = false,
                                errorMessage = result.message
                            )
                        }

                        is Resource.Loading -> {
                            _insertState.value = _insertState.value?.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }

        /**
         * Oyunun favorilere eklenip eklenmediğini kontrol eden fonksiyon
         */
        fun isGameFavorited(gameId: Int) {
            _isGameFavorited.value = _isGameFavorited.value?.copy(
                isLoading = true,
                success = false,
                errorMessage = null
            )

            viewModelScope.launch(Dispatchers.IO) {
                val result = isGameFavoritedUseCase.execute(gameId)
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Resource.Success -> {
                            _isGameFavorited.value = _isGameFavorited.value?.copy(
                                isLoading = false,
                                success = result.data!!,
                                errorMessage = null
                            )
                        }

                        is Resource.Error -> {
                            _isGameFavorited.value = _isGameFavorited.value?.copy(
                                isLoading = false,
                                success = false,
                                errorMessage = result.message
                            )
                        }

                        is Resource.Loading -> {
                            _isGameFavorited.value = _isGameFavorited.value?.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }

        /**
         * Oyun detaylarıyla ilgili olayları yöneten fonksiyon
         */
        fun onDetailsEvent(gameDetailsEvent: GameDetailsEvent) {
            when (gameDetailsEvent) {
                is GameDetailsEvent.getGameDetails -> getGameDetails(gameDetailsEvent.gameId)
                is GameDetailsEvent.getGameScreenshots -> getScreenShoots(gameDetailsEvent.gameSlug)
                is GameDetailsEvent.getGameTrailers -> getGameTrailer(gameDetailsEvent.gameId)
            }
        }

        /**
         * Favorilere ekleme olaylarını yöneten fonksiyon
         */
        fun onDetailsFavoritesEvent(detailsFavoritesEvent: DetailsFavoritesEvent) {
            when (detailsFavoritesEvent) {
                is DetailsFavoritesEvent.AddToFavorites -> insertFavoriteGame(detailsFavoritesEvent.game)
                is DetailsFavoritesEvent.CheckIfFavorited -> isGameFavorited(detailsFavoritesEvent.gameId)

            }
        }

        /**
         * ViewModel yok edilirken çalışan fonksiyon
         */
        override fun onCleared() {
            super.onCleared()
            //job?.cancel()
            //gameJob?.cancel()
        }
    }

