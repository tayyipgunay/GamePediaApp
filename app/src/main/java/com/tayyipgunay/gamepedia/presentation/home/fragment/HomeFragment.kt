package com.tayyipgunay.gamepedia.presentation.home.fragment

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tayyipgunay.gamepedia.databinding.FragmentHomeBinding
import com.tayyipgunay.gamepedia.presentation.home.adapter.BannerAdapter
import com.tayyipgunay.gamepedia.presentation.home.adapter.GameAdapter
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView
import com.tayyipgunay.gamepedia.domain.model.Banner
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.presentation.home.event.HomeEvent
import com.tayyipgunay.gamepedia.presentation.home.viewModel.HomeViewModel


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import okhttp3.internal.http2.Http2Reader
import java.util.logging.Handler
import javax.inject.Inject

/**
 * HomeFragment, ana ekranı yöneten fragmenttir.
 * - Oyun listesini ve bannerları yükler.
 * - Kullanıcı sıralama, kategori ve sayfa boyutu seçimleri yapabilir.
 * - Arama özelliği sunar.
 * - Sonsuz kaydırma (pagination) desteği bulunur.
 * - Hata ve yükleme durumlarını yönetir.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    // ViewBinding için değişkenler
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel'i kullanmak için değişken
    private val homeViewModel: HomeViewModel by viewModels()

    // Seçilen tür ve sıralama seçenekleri için değişkenler
    private var selectedGenre: String? = null
    private var selectedOption: String = "default"
    private var currentPageSize: Int = 10
    private var currentPage: Int = 1

    // Handler ve banner listesi için değişkenler
    private val handler = android.os.Handler(Looper.getMainLooper())
    var oldBannerList2: ArrayList<Banner> = ArrayList()

    // Adapter'ları enjekte etmek için değişkenler
    @Inject
    lateinit var gameAdapter: GameAdapter
    @Inject
    lateinit var bannerAdapter: BannerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding'i kullanarak layout'u bağla
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView, Spinner, SearchView ve Retry Button'ları kur
        setupRecyclerView()
        setupSpinners()
        setupSearchView()
        setupRetryButton()

        // ViewModel'den banner ve oyun verilerini al
        homeViewModel.onEvent(HomeEvent.getBanner)
        observeTopRated()
        observeViewModel()
        updataGamesForSize()

        // Toast mesajı ile fragment'in oluşturulduğunu bildir
        Toast.makeText(
            requireContext(),
            "$currentPageSize HomeFragment oluşturuldu  " + currentPage,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupRecyclerView() {
        // RecyclerView ve ViewPager için adapter'ları ayarla
        binding.gamesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.gamesRecyclerView.adapter = gameAdapter
        binding.bannerViewPager.adapter = bannerAdapter
    }

    private fun setupSpinners() {
        // Sıralama, tür ve sayfa boyutu için Spinner'ları kur
        setupSortingSpinner()
        setupGenreSpinner()
        setupPageSizeSpinner()
    }

    private fun setupSortingSpinner() {
        // Sıralama seçenekleri için Spinner'ı ayarla
        val sortingOptions = listOf("Default", "Rating", "Release Date", "Genre")
        val sortingAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortingOptions)
        sortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortingSpinner.adapter = sortingAdapter

        binding.sortingSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedOption = sortingOptions[position]
                    currentPage = 1
                    if (selectedOption == "Genre") {
                        binding.genreSpinner.visibility = View.VISIBLE
                    } else {
                        binding.genreSpinner.visibility = View.GONE
                    }
                    fetchGames()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setupGenreSpinner() {
        // Tür seçenekleri için Spinner'ı ayarla
        val genreOptions = listOf("Adventure", "RPG", "Shooter", "Action", "Indie")
        val genreAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genreOptions)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genreSpinner.adapter = genreAdapter
        binding.genreSpinner.visibility = View.GONE

        binding.genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGenre = genreOptions[position]
                currentPage = 1
                fetchGames()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupPageSizeSpinner() {
        // Sayfa boyutu seçenekleri için Spinner'ı ayarla
        val pageSizeOptions = listOf(10, 20, 30, 40)
        val pageSizeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pageSizeOptions)
        pageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.pageSizeSpinner.adapter = pageSizeAdapter

        binding.pageSizeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentPage = 1
                    currentPageSize = pageSizeOptions[position]
                    fetchGames()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setupSearchView() {
        // SearchView için dinleyici ayarla
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    println("search boşş")
                    resetSearchView()
                } else {
                    binding.bannerViewPager.visibility = View.GONE
                    currentPage = 1
                    homeViewModel.onEvent(HomeEvent.searchGames(newText, currentPage))
                }
                return true
            }
        })
    }

    private fun setupRetryButton() {
        // Retry butonu için dinleyici ayarla
        binding.retryButton.setOnClickListener {
            if (binding.searchView.query.isNullOrEmpty()) {
                fetchGames()
                homeViewModel.onEvent(HomeEvent.getBanner)
            } else {
                homeViewModel.onEvent(
                    HomeEvent.searchGames(
                        binding.searchView.query.toString(),
                        currentPage
                    )
                )
            }
        }
    }

    fun observeViewModel() {
        // ViewModel'den gelen durumları gözlemle
        homeViewModel.homeState.observe(viewLifecycleOwner) { homeState ->
            println("observeViewModel çalıştı home ve loadingg-> " + homeState.isLoading + " " + homeState.isSearchLoading)
            updateLoadingStates(homeState.isLoading, homeState.isSearchLoading)

            when {
                homeState.games != null -> {
                    println("When games null olaamz içindeyiz")
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    binding.gamesRecyclerView.visibility = View.VISIBLE
                    updateRecyclerView(homeState.games)
                    println("Oyunlar başarıyla yüklendi: ${homeState.games.size} oyun")
                    println(homeState.games)
                    println(homeState.games.get(0).name)
                }
                homeState.errorMessage != null -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.errorMessageTextView.text = homeState.errorMessage
                    println("Hata mesajı: ${homeState.errorMessage}")
                }
                else -> {
                    println("Varsayılan durum tetiklendi. Herhangi bir işlem yapılmadı.")
                }
            }
        }
    }

    fun observeTopRated() {
        // Top Rated oyunların durumunu gözlemle
        homeViewModel.topRatedGamesState.observe(viewLifecycleOwner) { topRatedGamesState ->
            if (topRatedGamesState.isLoading) {
                println("topRatedGamesState loading " + topRatedGamesState.isLoading)
            }
            topRatedGamesState.bannerImages?.let {
                println("topRatedGamesState bannerImages " + topRatedGamesState.bannerImages)
                oldBannerList2.clear()
                oldBannerList2.addAll(topRatedGamesState.bannerImages)
                bannerAdapter.updateBannerImages(topRatedGamesState.bannerImages)
                bannerAdapter.updateError(false)
            }
            topRatedGamesState.error?.let {
                println("topRatedGamesState error " + topRatedGamesState.error)
                if (oldBannerList2.isNotEmpty()) {
                    bannerAdapter.updateBannerImages(oldBannerList2)
                    println("Hata: Eski veriler gösteriliyor.")
                } else {
                    bannerAdapter.updateBannerImages(emptyList())
                    bannerAdapter.updateError(true)
                    println("Hata: Liste boş ve hata ekranı gösteriliyor.")
                }
                Toast.makeText(
                    requireContext(), "${topRatedGamesState.error} Eski veriler gösteriliyor.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateLoadingStates(isLoading: Boolean, isSearchLoading: Boolean) {
        // Yükleme durumlarını güncelle
        when {
            isSearchLoading -> {
                binding.errorLayout.visibility = View.GONE
                binding.gamesRecyclerView.visibility = View.GONE
                binding.bannerViewPager.visibility = View.GONE
                binding.pageSizeSpinner.visibility = View.GONE
                binding.sortingSpinner.visibility = View.GONE
                binding.genreSpinner.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
                binding.loadingTextView.text = "Arama yapılıyor, lütfen bekleyin..."
                println("Arama yükleme durumu aktif.")
            }
            isLoading -> {
                binding.errorLayout.visibility = View.GONE
                binding.sortingSpinner.visibility = View.VISIBLE
                binding.pageSizeSpinner.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.VISIBLE
                binding.loadingTextView.text = "Yükleniyor, lütfen bekleyin..."
                println("Genel yükleme durumu aktif.")
            }
            else -> {
                println("Varsayılan durum tetiklendi. Herhangi bir işlem yapılmadı.")
            }
        }
    }

    private fun fetchGames() {
        // Seçilen seçeneklere göre oyunları getir
        val genreSlugMap = mapOf(
            "Adventure" to "adventure",
            "RPG" to "role-playing-games-rpg",
            "Shooter" to "shooter",
            "Action" to "action",
            "Indie" to "indie"
        )

        when (selectedOption) {
            "Default" -> homeViewModel.getGames("-added", currentPageSize, currentPage)
            "Rating" -> homeViewModel.getGames("-rating", currentPageSize, currentPage)
            "Release Date" -> homeViewModel.getGames("-released", currentPageSize, currentPage)
            "Genre" -> selectedGenre?.let { selectedGenre ->
                println(selectedGenre)
                val slug = genreSlugMap[selectedGenre] ?: return
                homeViewModel.onEvent(
                    HomeEvent.getGenres(
                        slug,
                        currentPageSize,
                        page = currentPage
                    )
                )
            }
        }
    }

    private fun resetSearchView() {
        // SearchView'i sıfırla
        binding.sortingSpinner.visibility = View.VISIBLE
        binding.pageSizeSpinner.visibility = View.VISIBLE
        binding.gamesRecyclerView.visibility = View.VISIBLE
        binding.bannerViewPager.visibility = View.VISIBLE
        binding.gamesRecyclerView.visibility = View.VISIBLE
        currentPage = 1

        if (selectedOption == "Genre") {
            binding.genreSpinner.visibility = View.VISIBLE
        } else {
            binding.genreSpinner.visibility = View.GONE
        }
        fetchGames()
    }

    private fun updateRecyclerView(games: List<Game>) {
        // RecyclerView'i güncelle
        gameAdapter.updateGameList(games)
    }

    private fun updataGamesForSize() {
        // Sayfa boyutuna göre oyunları güncelle
        println("updataGamesForSize içindeyiz")

        binding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                println("En alta ulaşıldı")
                println(currentPage)
                currentPage += 1
                println("updataGamesForSize içindeki fetchGames metodu çalıştı ve değer artırıldı")
                println(currentPage)

                if (binding.bannerViewPager.visibility == View.GONE) {
                    // Eğer banner boş ise arama yapılıyor demektir, fetchGame çağrısı yapma
                } else {
                    handler.postDelayed({
                        fetchGames()
                        binding.nestedScrollView.smoothScrollTo(0, 0)
                    }, 2000)
                }
            }
        }
    }
}

// Global Layout Listener ekle













