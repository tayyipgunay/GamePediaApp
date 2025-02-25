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


   private val _gameDetailsState = MutableLiveData(GameDetailsState())

    val gameDetailsState: LiveData<GameDetailsState> = _gameDetailsState
    private val _gameTrailerState = MutableLiveData(TrailerState())//
    val gameTrailerState: LiveData<TrailerState> get() = _gameTrailerState

    private val _screenShootState=MutableLiveData(ScreenShootState())
    val screenShootState:LiveData<ScreenShootState> get()=_screenShootState



    private val _insertState = MutableLiveData(InsertState())
    val insertState: LiveData<InsertState>  get()=_insertState




    private val _isGameFavorited = MutableLiveData(isGameFavoritedState())
    val isGameFavoritedState: LiveData<isGameFavoritedState>  get()=_isGameFavorited




    /*
   MutableLiveData, başlangıçta null değer alır.
 Bu durumda, LiveData gözlemcileri (observers) null değerle karşılaşabilir ve NullPointerException riski oluşabilir.
private val _gameTrailerState = MutableLiveData<TrailerState>()//null olan
    val gameTrailerState: LiveData<TrailerState> = _gameTrailerState
*/


  /* MutableLiveData, başlangıçta varsayılan bir TrailerState değeri ile başlatılır.
 Bu sayede LiveData'nın değeri asla null olmaz ve gözlemcilerde null kontrolü yapmaya gerek kalmaz.
*/







    fun getGameDetails(gameId: Int) {
        _gameDetailsState.value = gameDetailsState.value?.copy(
            isLoading = true,
            error = null,
            gameDetail = null
        )
println("GET GAME DETAİLS ÇAĞRILDI")

        //job =
        viewModelScope.launch(Dispatchers.IO) {
            val gameDetails = gameDetailsUseCase.executeGetGameDetailsUseCase(gameId)
            withContext(Dispatchers.Main) {
                when (gameDetails) {
                    is Resource.Success -> {
                        println("Başarılı: $gameDetails.data?.title")

                        _gameDetailsState.value = gameDetailsState.value?.copy(
                            gameDetail = gameDetails.data,
                            isLoading = false
                          //  error = null
                        )


                    }

                    is Resource.Error -> {
                        _gameDetailsState.value = gameDetailsState.value?.copy(
                          //  gameDetail = null,
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

    fun getScreenShoots(gameSlug: String) {
        // Ekran görüntüleri yüklenmeye başlarken, state’i “loading” hale getiriyoruz
        _screenShootState.value = screenShootState.value?.copy(

            error = null,
            gameScreenshoots = null,
            isLoading = true

        )

        //job =
        viewModelScope.launch(Dispatchers.IO) {
            // UseCase üzerinden oyun ID ile ekran görüntülerini çekiyoruz
            val result = getScreenShootsUseCase.executeGetScreenShoots(gameSlug)


            withContext(Dispatchers.Main) {
                // Ekran görüntülerinin boş mu kontrol ediyoruz
                when (result) {
                    is Resource.Success ->
                        // Başarılı durum: Ekran görüntüleri elde edildi
                        _screenShootState.value = screenShootState.value?.copy(
                           isLoading = false,
                          //  error = null,
                            gameScreenshoots = result.data
                        )

                    is Resource.Error -> {
                        println(result.message)
                        _screenShootState.value = screenShootState.value?.copy(
                           isLoading = false,
                            error = result.message
                           // gameScreenshoots = null
                        )
                    }

                    is Resource.Loading -> {
                        _screenShootState.value = screenShootState.value?.copy(

                            isLoading = true
                           // error = null,
                           // gameScreenshoots = null


                        )
                    }
                }
            }
        }
    }



    fun getGameTrailer(gameId: Int){
        //job?.cancel()
        _gameTrailerState.value = gameTrailerState.value?.copy(
            isLoading = true
           // error = null,
            //gameTrailer = null

        )
        println("viewModel içindeyiz")

       // job =
            viewModelScope.launch(Dispatchers.IO) {
            val gameTrailer = getGameTrailersUseCase.executeGetGameTrailers(gameId)
            println("viewModel içindeyiz")



            withContext(Dispatchers.Main) {
                when(gameTrailer){
                    is  Resource.Success->
                        _gameTrailerState.value = _gameTrailerState.value?.copy(
                            gameTrailer = gameTrailer.data,
                            isLoading = false
                          //  error = null
                        )

                    is Resource.Error -> {
                        _gameTrailerState.value = _gameTrailerState.value?.copy(
                            //gameTrailer=null,
                            isLoading = false,
                            error = gameTrailer.message
                        )

                    }
                    is Resource.Loading -> {
                        _gameTrailerState.value = _gameTrailerState.value?.copy(
                            isLoading = true
                         //   error = null,
                           // gameTrailer = null
                        )

                    }

                    }
                }
                }
            }



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

                        ) // Başarı durumunda state'i güncelle
                        println("Sucess veritabaına eklendi " + result.data.toString())

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

    fun  isGameFavorited(gameId: Int) {

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
                            errorMessage = result.message,
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






fun onDetailsEvent(gameDetailsEvent: GameDetailsEvent) {
    when (gameDetailsEvent) {
        is GameDetailsEvent.getGameDetails -> {
            getGameDetails(gameId = gameDetailsEvent.gameId)

        }

        is GameDetailsEvent.getGameScreenshots -> {
           getScreenShoots(gameSlug = gameDetailsEvent.gameSlug)
        }
        is GameDetailsEvent.getGameTrailers -> {
            getGameTrailer(gameId = gameDetailsEvent.gameId)

        }
    }
}
    fun onDetailsFavoritesEvent(detailsFavoritesEvent: DetailsFavoritesEvent) {
        when (detailsFavoritesEvent) {
            is DetailsFavoritesEvent.AddToFavorites -> {
                insertFavoriteGame(game = detailsFavoritesEvent.game)
            }
            is DetailsFavoritesEvent.CheckIfFavorited -> {
                isGameFavorited(gameId = detailsFavoritesEvent.gameId)
            }
        }

    }










    override fun onCleared() {
        super.onCleared()
        //job?.cancel()
        //gameJob?.cancel()
    }


    }
