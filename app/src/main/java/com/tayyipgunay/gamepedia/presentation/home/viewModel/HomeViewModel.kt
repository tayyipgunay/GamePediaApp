package com.tayyipgunay.gamepedia.presentation.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.gamepedia.domain.usecase.home.GetBannersUseCase
import com.tayyipgunay.gamepedia.domain.usecase.home.GetGamesUseCase
import com.tayyipgunay.gamepedia.domain.usecase.home.SearchGamesUseCase
import com.tayyipgunay.gamepedia.domain.usecase.home.getGenresUseCase
import com.tayyipgunay.gamepedia.presentation.home.event.HomeEvent
import com.tayyipgunay.gamepedia.presentation.home.state.HomeState
import com.tayyipgunay.gamepedia.presentation.home.state.TopRatedGamesState
import com.tayyipgunay.gamepedia.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor (private val getGamesUseCase: GetGamesUseCase, private val getBannerUseCase: GetBannersUseCase, private val searchGamesUseCase: SearchGamesUseCase, private val genresUseCase: getGenresUseCase) : ViewModel() {

    private var bannerJob: Job? = null
    private var exceptionJob: Job? = null
    private var currentJob: Job? = null

// Diğer işlemler için de benzer şekilde Job değişkenleri tanımlayın


    // private val home_uiState = MutableLiveData<HomeState>()


    /*private val home_uiState = MutableLiveData<HomeState>(HomeState())


    val homeState: LiveData<HomeState> get() = home_uiState*/


    // private val _homeState = MutableLiveData<Resource<HomeState>>(Resource.Loading(HomeState()))
    /* private val _homeState = MutableLiveData<Resource<HomeState>>()
    val homeState: LiveData<Resource<HomeState>> get() = _homeState*/

    //val state: LiveData<HomeState> = _state//dışarıya salt okuma olarak sunma


    private val _state = MutableLiveData<HomeState>(HomeState())

    val homeState: LiveData<HomeState> get() = _state



    /*private var _topRatedGamesState = MutableLiveData<TopRatedGamesState>(TopRatedGamesState())

    val topRatedGamesState: LiveData<TopRatedGamesState> get() = _topRatedGamesState*/


    private val _topRatedGamesState = MutableLiveData(TopRatedGamesState())
    val topRatedGamesState: LiveData<TopRatedGamesState> get() = _topRatedGamesState





    fun getGames(ordering: String, pageSize: Int,page:Int) {
        println("getGames çağrıldı.")
        currentJob?.cancel()

        _state.value= homeState.value?.copy(
            games = null,
            isLoading = true,
            errorMessage = null
           // isSearchLoading = false
        )

        /*
        // MutableLiveData<T>, LiveData<T> sınıfından türetilir ve T nullable olarak tanımlanmıştır.
// Bu, MutableLiveData'nın value özelliğinin başlangıçta null olabileceği anlamına gelir.
// Bu yüzden ?. ile null kontrolü yapılır.
         */

        currentJob= viewModelScope.launch(Dispatchers.IO) {

                val ResourceResponse = getGamesUseCase.GetGamesExecute(ordering, pageSize,page)
                println("getGames API çağrısı başarılı. Gelen yanıt: "+ResourceResponse.data?.get(0)?.name)

                withContext(Dispatchers.Main) {
                    when (ResourceResponse) {
                        is Resource.Success -> {
                            println("Başarılı: ${ResourceResponse.data?.size ?: 0} oyun alındı.")

                            _state.value= homeState.value?.copy(
                                isLoading = false,
                                games = ResourceResponse.data
                            )

                        }

                        is Resource.Error -> {
                            println("Hata: ${ResourceResponse.message}")

                            _state.value= homeState.value?.copy(
                                isLoading = false,
                                errorMessage = ResourceResponse.message,
                                games = null
                            )
                        }

                        is Resource.Loading -> {
                            println("Yükleniyor...")


                            _state.value= homeState.value?.copy(
                                isLoading = true
                            )

                        }
                    }
                }


            }
        }
    fun getByGenre(genres: String, pageSize: Int,page: Int) {
        println("getByGenre çağrıldı. Genre: $genres, PageSize: $pageSize")
        // Mevcut işi iptal et
        currentJob?.cancel()
        println("Mevcut iş iptal edildi (currentJob).")


        _state.value= homeState.value?.copy(
            games = null,
            isLoading = true,
            errorMessage = null
         //   isSearchLoading = false,
        )





        println("isLoading durumu true olarak ayarlandı.")

        // Coroutine başlat
        currentJob = viewModelScope.launch(Dispatchers.IO) {

                println("getByGenre coroutine başlatıldı.")

                // Genre bazlı oyunları getir
                val resourceResponse = genresUseCase.executeGenres(genres, pageSize,page)
                println("getByGenre API çağrısı başarılı. Gelen yanıt: $resourceResponse")

                withContext(Dispatchers.Main) {
                    when (resourceResponse) {
                        is Resource.Success -> {
                            println("Başarılı: ${resourceResponse.data?.size ?: 0} oyun alındı.")

                            _state.value= homeState.value?.copy(
                                games = resourceResponse.data,
                                isLoading = false

                            )



                        }
                        is Resource.Error -> {
                            println("Hata: ${resourceResponse.message}")
                            _state.value= homeState.value?.copy(
                                isLoading = false,
                                errorMessage = resourceResponse.message,
                               // games = null
                            )



                        }
                        is Resource.Loading -> {
                            println("Yükleniyor...")
                            _state.value= homeState.value?.copy(
                                isLoading =true
                            )
                        }
                    }
                }

        }
    }
    fun searchGames(query: String,page: Int){
        currentJob?.cancel()

        println("getSearchGame çağrıldı. Query: $query")


        _state.value= homeState.value?.copy(
            games = null,
            isSearchLoading = true,
            errorMessage = null
            // Loading = false
        )


        currentJob = viewModelScope.launch(Dispatchers.IO) {
            val resourceResponse = searchGamesUseCase.executeSearchGames(query,page = page)


            withContext(Dispatchers.Main) {
                when (resourceResponse) {
                    is Resource.Success -> {
                      //  println("SearchGames API çağrısı başarılı. Gelen yanıt: ${resourceResponse.data?.size} oyun")
                        _state.value= homeState.value?.copy(
                            games = resourceResponse.data,
                            isSearchLoading = false
                            //  errorMessage = null
                            // Loading = false
                        )



                      /*  if (resourceResponse.data.isNullOrEmpty()) {
                            // Boş liste durumunda istisna fırlatılıyor
                            throw NoSuchElementException("No games found for your search.")
                        }*/
                      /*  _state.value = HomeState(
                            games = resourceResponse.data,
                            isLoading = false,
                            errorMessage = null,
                            isSearchLoading = false
                        )*/


                    }





                    is Resource.Error -> {
                        println("SearchGames Hata: ${resourceResponse.message}")
                        _state.value= homeState.value?.copy(
                            //games = null,
                            isSearchLoading = false,
                            errorMessage = resourceResponse.message
                        )


                    }


                    is Resource.Loading ->{
                        println("SearchGames Yükleniyor...")
                        _state.value= homeState.value?.copy(
                            isSearchLoading = false
                        )
                    }

                    }

                }
            }
        }

    fun getBanner(){
        bannerJob?.cancel()

        _topRatedGamesState.value=_topRatedGamesState.value?.copy(
            isLoading = true,
            error = null,
            bannerImages = null
        )

        bannerJob=viewModelScope.launch(Dispatchers.IO) {
            val bannerResponse=getBannerUseCase.BannerExecute()
            withContext(Dispatchers.Main){
                when(bannerResponse){
                    is Resource.Success -> {
                        _topRatedGamesState.value = _topRatedGamesState.value?.copy(
                            isLoading = false,
                            bannerImages = bannerResponse.data
                           // error = null
                        )
                    }
                    is Resource.Error ->{
                        _topRatedGamesState.value = _topRatedGamesState.value?.copy(
                           // bannerImages = null,
                            isLoading = false,
                            error = bannerResponse.message
                        )


                    }
                    is Resource.Loading -> {
                        _topRatedGamesState.value = _topRatedGamesState.value?.copy(
                            isLoading = true
                            //error = null,
                            //bannerImages = null

                        )

                    }


                    }
                }

        }

    }

    fun onEvent(homeEvent: HomeEvent) {
        when (homeEvent) {
            is HomeEvent.getGames->{
                getGames(homeEvent.ordering,homeEvent.pageSize,homeEvent.page)


                println("search event tetiklendi ve çalıştı")
            }

            is HomeEvent.getGenres -> {
                getByGenre(homeEvent.genres, homeEvent.pageSize,homeEvent.page)
            }
            is HomeEvent.getBanner->{
                getBanner()

        }
            is HomeEvent.searchGames -> {
               searchGames(homeEvent.query,homeEvent.page)
            }
        }

    }


    override fun onCleared() {
            super.onCleared()
            currentJob?.cancel()
            bannerJob?.cancel()
            exceptionJob?.cancel()

        }

    }






