package com.tayyipgunay.gamepedia.presentation.game_details.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.tayyipgunay.gamepedia.R
import com.tayyipgunay.gamepedia.databinding.FragmentGameDetailsBinding
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.model.Genres
import com.tayyipgunay.gamepedia.presentation.game_details.adapter.ScreenShootsAdapter
import com.tayyipgunay.gamepedia.presentation.game_details.event.DetailsFavoritesEvent
import com.tayyipgunay.gamepedia.presentation.game_details.viewModel.GameDetailsViewModel
import com.tayyipgunay.gamepedia.presentation.game_details.event.GameDetailsEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameDetailsFragment : Fragment() {

    // Binding değişkenini tanımlıyoruz
    private var _binding: FragmentGameDetailsBinding? = null
    private val binding get() = _binding!!
    private val gameDetailsviewModel: GameDetailsViewModel by viewModels()


    private val screenShootsAdapter = ScreenShootsAdapter(arrayListOf())

    private var gameidd = 0
    private var gameSlugg: String = ""
    private var imageUrl: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Binding'i başlatıyoruz
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Binding ile UI bileşenlerine erişim örneği
        println("game details fragment çalıştı")
        binding.imageViewpager2.adapter = screenShootsAdapter



        binding.backButton.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.detailsTab.setOnClickListener {
            binding.detailsLayout.visibility = View.VISIBLE
            binding.SkillsLayout.visibility = View.GONE
           binding.detailsTab.setBackgroundColor(Color.parseColor("#4CAF50"))
            binding.skillsTab.setBackgroundColor(Color.parseColor("#505050"))


        }

        binding.skillsTab.setOnClickListener {
            binding.detailsLayout.visibility = View.GONE
            binding.SkillsLayout.visibility = View.VISIBLE

            binding.detailsTab.setBackgroundColor(Color.parseColor("#505050"))
            binding.skillsTab.setBackgroundColor(Color.parseColor("#4CAF50"))

        }

        binding.retryButton.setOnClickListener {
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameDetails(gameidd))
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameScreenshots(gameSlugg))
            println("retry button tıklandı")

        }

        binding.favoriteButton.setOnClickListener {
            val game = getGameDetailsForFavorite()
            // gameDetailsviewModel.insertFavoriteGame(game)
            gameDetailsviewModel.onDetailsFavoritesEvent(DetailsFavoritesEvent.AddToFavorites(game))

        }



        arguments?.let { bundle ->
            val gameId = GameDetailsFragmentArgs.fromBundle(bundle).gameId
            val gameSlug = GameDetailsFragmentArgs.fromBundle(bundle).slug



            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameDetails(gameId))
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameScreenshots(gameSlug))
            gameDetailsviewModel.isGameFavorited(gameId)



            gameidd = gameId
            gameSlugg = gameSlug
            println("gameiddd $gameidd")





        }

        observeGameDetailState()
        observeScreenShootState()
        observeGameTrailerState()
        observeInsertFavorite()

        observeIsFavorited()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Hafıza sızıntısını önlemek için binding'i null yapıyoruz
    }

    fun observeGameDetailState() {
        gameDetailsviewModel.gameDetailsState.observe(viewLifecycleOwner) { gameDetailsState ->
            println(gameDetailsState.isLoading)
            println("observe başladı")
            println(gameDetailsState.isLoading)
            if (gameDetailsState.isLoading) {
                binding.loadingLayout.visibility = View.VISIBLE
                binding.starIcon.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
                binding.imageViewpager2.visibility = View.GONE
                binding.descriptionTitle.visibility = View.GONE
                binding.descriptionContent.visibility = View.GONE
                binding.playButton.visibility = View.GONE
                binding.favoriteButton.visibility = View.GONE
                binding.gameTitle.visibility = View.GONE
                binding.releaseDate.visibility = View.GONE
                binding.gameIcon.visibility = View.GONE
                binding.ratingText.visibility = View.GONE
                binding.pegiIcon.visibility = View.GONE
                binding.pegi.visibility = View.GONE
                binding.genre.visibility = View.GONE
                binding.detailsTab.visibility = View.GONE
                binding.skillsTab.visibility = View.GONE
                binding.detailsLayout.visibility = View.GONE
                binding.SkillsLayout.visibility = View.GONE


            }

            gameDetailsState.gameDetail?.let {

                println("gameDetails let içindeyiz")
                binding.loadingLayout.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
                binding.starIcon.visibility = View.VISIBLE
                binding.imageViewpager2.visibility = View.VISIBLE
                binding.descriptionTitle.visibility = View.VISIBLE
                binding.descriptionContent.visibility = View.VISIBLE
                binding.playButton.visibility = View.VISIBLE
                binding.favoriteButton.visibility = View.VISIBLE
                binding.gameTitle.visibility = View.VISIBLE
                binding.releaseDate.visibility = View.VISIBLE
                binding.gameIcon.visibility = View.VISIBLE
                binding.ratingText.visibility = View.VISIBLE
                binding.pegiIcon.visibility = View.VISIBLE
                binding.pegi.visibility = View.VISIBLE
                binding.genre.visibility = View.VISIBLE
                binding.detailsTab.visibility = View.VISIBLE
                binding.skillsTab.visibility = View.VISIBLE
                binding.detailsLayout.visibility = View.VISIBLE
                binding.detailsLayout.visibility = View.VISIBLE



                binding.gameTitle.text = gameDetailsState.gameDetail.title
                binding.releaseDate.text = gameDetailsState.gameDetail.releaseDate
                Glide.with(this).load(gameDetailsState.gameDetail.backgroundImageUrl)
                    .into(binding.gameIcon)
                imageUrl = gameDetailsState.gameDetail.backgroundImageUrl

                binding.ratingText.text = gameDetailsState.gameDetail.rating.toString()

                binding.genre.text = gameDetailsState.gameDetail.genres.joinToString(", ") {
                    it.name
                }
                binding.descriptionContent.text = gameDetailsState.gameDetail.description


                binding.pegi.text = "${gameDetailsState.gameDetail.esrbRating},"

                val statedGame = gameDetailsState.gameDetail.esrbRating
                when (statedGame) {
                    "(Early Childhood" -> {
                        binding.pegiIcon.setImageResource(R.drawable.everychild)
                    }

                    "Everyone" -> {
                        binding.pegiIcon.setImageResource(R.drawable.everyone)

                    }

                    "Everyone 10+" -> {
                        binding.pegiIcon.setImageResource(R.drawable.everyone10)

                    }

                    "Teen" -> {
                        binding.pegiIcon.setImageResource(R.drawable.teen)
                    }

                    "Mature" -> {
                        binding.pegiIcon.setImageResource(R.drawable.mature)
                    }

                    "Adults Only" -> {
                        binding.pegiIcon.setImageResource(R.drawable.adultonly)
                    }

                    "Rating Pending" -> {
                        binding.pegiIcon.setImageResource(R.drawable.ratingpanding)
                    }

                    else -> {
                        binding.pegiIcon.setImageResource(R.drawable.ratingpanding)
                    }


                }


                val publisler = gameDetailsState.gameDetail.publisher
                val platforms = gameDetailsState.gameDetail.platforms
                val tags = gameDetailsState.gameDetail.tags

                publisler.let {
                    binding.flexboxPublishers.removeAllViews()
                    publisler.split(",").forEach { publisher ->
                        val textView = TextView(requireContext()).apply {
                            text = publisher
                            setPadding(16, 8, 16, 8) // Yatay 16dp, Dikey 8dp padding
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                            background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.rounded_background
                            )

                            // LayoutParams ile margin ekle
                            layoutParams = FlexboxLayout.LayoutParams(
                                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                                FlexboxLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(16, 16, 16, 16) // Dış kenarlar için 16dp margin
                            }
                        }
                        binding.flexboxPublishers.addView(textView)
                    }
                }

                platforms.let {
                    binding.flexboxPlatforms.removeAllViews()
                    platforms.split(",").forEach { platform ->
                        val textView = TextView(requireContext()).apply {
                            text = platform
                            setPadding(16, 8, 16, 8) // Yatay 16dp, Dikey 8dp padding
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                            background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.rounded_background
                            )

                            layoutParams = FlexboxLayout.LayoutParams(
                                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                                FlexboxLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(16, 16, 16, 16) // Dış kenarlar için 16dp margin
                            }
                        }
                        binding.flexboxPlatforms.addView(textView)
                    }
                }

                tags.let {
                    binding.flexboxTags.removeAllViews()
                    tags.split(",").forEach { tag ->
                        val textView = TextView(requireContext()).apply {
                            text = tag
                            setPadding(16, 8, 16, 8) // Yatay 16dp, Dikey 8dp padding
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                            background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.rounded_background
                            )

                            layoutParams = FlexboxLayout.LayoutParams(
                                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                                FlexboxLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(16, 16, 16, 16) // Dış kenarlar için 16dp margin
                            }
                        }
                        binding.flexboxTags.addView(textView)
                    }
                }
            }



            gameDetailsState.error?.let {
                binding.SkillsLayout.visibility = View.GONE
                binding.detailsLayout.visibility = View.GONE
                binding.imageViewpager2.visibility = View.GONE
                binding.descriptionTitle.visibility = View.GONE
                binding.descriptionContent.visibility = View.GONE
                binding.playButton.visibility = View.GONE
                binding.favoriteButton.visibility = View.GONE
                binding.gameTitle.visibility = View.GONE
                binding.releaseDate.visibility = View.GONE
                binding.gameIcon.visibility = View.GONE
                binding.ratingText.visibility = View.GONE
                binding.pegiIcon.visibility = View.GONE
                binding.pegi.visibility = View.GONE
                binding.genre.visibility = View.GONE
                binding.detailsTab.visibility = View.GONE
                binding.skillsTab.visibility = View.GONE
                binding.loadingLayout.visibility = View.GONE
                binding.starIcon.visibility = View.GONE


                binding.errorLayout.visibility = View.VISIBLE
                binding.errorMessage.text = gameDetailsState.error




            }


        }


    }


    fun observeGameTrailerState() {
        binding.playButton.setOnClickListener {
            println("play butona basıldı")

            // Play butonuna basıldığında getGameTrailer çağrılır
            //   gameDetailsviewModel.getGameTrailer(gameidd)
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameTrailers(gameidd))
            println("observe2 metot  içindeyiz  " + gameidd)

            // Trailer verisi geldikten sonra observe edilir
            gameDetailsviewModel.gameTrailerState.observe(viewLifecycleOwner) { gameTrailerState ->


                println("observer 2 içindeyiz")




                if (gameTrailerState.isLoading) {
                    println("trailer loadin ")
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.starIcon.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    binding.imageViewpager2.visibility = View.GONE
                    binding.descriptionTitle.visibility = View.GONE
                    binding.descriptionContent.visibility = View.GONE
                    binding.playButton.visibility = View.GONE
                    binding.favoriteButton.visibility = View.GONE
                    binding.gameTitle.visibility = View.GONE
                    binding.releaseDate.visibility = View.GONE
                    binding.gameIcon.visibility = View.GONE
                    binding.ratingText.visibility = View.GONE
                    binding.pegiIcon.visibility = View.GONE
                    binding.pegi.visibility = View.GONE
                    binding.genre.visibility = View.GONE
                    binding.detailsTab.visibility = View.GONE
                    binding.skillsTab.visibility = View.GONE
                    binding.detailsLayout.visibility = View.GONE
                    binding.SkillsLayout.visibility = View.GONE


                }



                gameTrailerState.gameTrailer?.let {
                    println(" gameTrailerState.gameTrailer let  içindeyiz")
                    val url = gameTrailerState.gameTrailer.get(0).videoUrl
                    val action =
                        GameDetailsFragmentDirections.actionGameDetailsFragmentToVideoPlayerFragment(
                            url
                        )
                    Navigation.findNavController(requireView()).navigate(action)
                    println("fragmenta geçtşk")
                }

                gameTrailerState.error?.let {
                    println("trailer error ")
                    binding.SkillsLayout.visibility = View.GONE
                    binding.detailsLayout.visibility = View.GONE
                    binding.imageViewpager2.visibility = View.GONE
                    binding.descriptionTitle.visibility = View.GONE
                    binding.descriptionContent.visibility = View.GONE
                    binding.playButton.visibility = View.GONE
                    binding.favoriteButton.visibility = View.GONE
                    binding.gameTitle.visibility = View.GONE
                    binding.releaseDate.visibility = View.GONE
                    binding.gameIcon.visibility = View.GONE
                    binding.ratingText.visibility = View.GONE
                    binding.pegiIcon.visibility = View.GONE
                    binding.pegi.visibility = View.GONE
                    binding.genre.visibility = View.GONE
                    binding.detailsTab.visibility = View.GONE
                    binding.skillsTab.visibility = View.GONE
                    binding.loadingLayout.visibility = View.GONE
                    binding.starIcon.visibility = View.GONE

                    binding.errorLayout.visibility = View.VISIBLE
                    binding.errorMessage.text = gameTrailerState.error

                    binding.retryButton.text = "Geri Dön"


                }


            }
        }

    }

    fun observeScreenShootState() {
        // Observe işlemi burada yapılır
        gameDetailsviewModel.screenShootState.observe(viewLifecycleOwner) { screenShootState ->

            if (screenShootState.isLoading) {
                println("Screen shoot loading içinde ")

            }


            screenShootState.gameScreenshoots?.let { screenShoots ->
                println("screenShootState let içinde")
                screenShootsAdapter.updateScreenshotList(screenShoots)

            }
            screenShootState.error?.let { error ->
                println("errorScreenShoot")

            }

        }
    }



    fun observeInsertFavorite() {//favoriye ekleme işlemi

        gameDetailsviewModel.insertState.observe(viewLifecycleOwner) { insertState ->

            when {
                insertState.isLoading -> {
                    println("Favoriye ekleme işlemi başladı")
                }

                insertState.isSuccess -> {
                    println("Favoriye ekleme işlemi başarılı oldu")

                    binding.favoriteButton.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    )

                    Toast.makeText(
                        requireContext(),
                        " Favorilere Eklendi",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                insertState.errorMessage != null -> {
                    println("Favoriye ekleme işlemi başarısız oldu")
                    println(insertState.errorMessage)
                    Toast.makeText(
                        requireContext(),
                        insertState.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                }

                }
            }
        }


        fun observeIsFavorited() {//yıldız durumunu ayarlama

            gameDetailsviewModel.isGameFavoritedState.observe(viewLifecycleOwner) { isFavoritedState ->

                if (isFavoritedState.isLoading) {
                    println("Favori durumu sorgulanıyor")
                }



                if (isFavoritedState.success) {
                    println("Favori durumu sorgulandı  " + isFavoritedState.success)

                    binding.favoriteButton.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    )

                } else {
                    println("Favori durumu sorgulandı " + isFavoritedState.success)
                    binding.favoriteButton.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }




                   if( isFavoritedState.errorMessage != null) {
                        println("Favori durumu sorgulanırken hata oluştu")
                        println(isFavoritedState.errorMessage)
                    }

                }
            }



   private fun getGameDetailsForFavorite(): Game {
        return Game(
            id = gameidd,
            name = binding.gameTitle.text.toString(),
            released = binding.releaseDate.text.toString(),
            backgroundImage = imageUrl,
            rating = binding.ratingText.text.toString().toFloatOrNull(),
            slug = gameSlugg,
            genres = binding.genre.text.toString().split(",").map { genres ->
                Genres(
                    name = genres
                )
            }
        )
    }




    }













