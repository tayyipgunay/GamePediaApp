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
import com.tayyipgunay.gamepedia.domain.model.GameDetail
import com.tayyipgunay.gamepedia.domain.model.Genres
import com.tayyipgunay.gamepedia.presentation.game_details.adapter.ScreenShootsAdapter
import com.tayyipgunay.gamepedia.presentation.game_details.event.DetailsFavoritesEvent
import com.tayyipgunay.gamepedia.presentation.game_details.viewModel.GameDetailsViewModel
import com.tayyipgunay.gamepedia.presentation.game_details.event.GameDetailsEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameDetailsFragment : Fragment() {

    // **View Binding değişkeni**
    // UI bileşenlerine erişmek için FragmentGameDetailsBinding kullanılır.
    private var _binding: FragmentGameDetailsBinding? = null
    private val binding get() = _binding!!

    // **ViewModel Tanımı**
    // Oyun detaylarını almak için GameDetailsViewModel kullanılır.
    private val gameDetailsviewModel: GameDetailsViewModel by viewModels()

    // **Ekran görüntüleri için RecyclerView Adaptörü**
    private val screenShootsAdapter = ScreenShootsAdapter(arrayListOf())

    // **Fragment içinde kullanılacak değişkenler**
    private var gameidd = 0 // Oyun ID
    private var gameSlugg: String = "" // Oyun slug bilgisi
    private var imageUrl: String = "" // Oyun görseli URL'si

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // **Binding başlatılır**
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("GameDetailsFragment çalıştı.")

        // **Ekran görüntüleri için adaptör bağlanıyor**
        binding.imageViewpager2.adapter = screenShootsAdapter

        // **Geri butonu tıklanınca önceki sayfaya dön**
        binding.backButton.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        // **Detaylar sekmesine tıklanınca detay bilgilerini göster**
        binding.detailsTab.setOnClickListener {
            binding.detailsLayout.visibility = View.VISIBLE
            binding.SkillsLayout.visibility = View.GONE
            binding.detailsTab.setBackgroundColor(Color.parseColor("#4CAF50")) // Aktif sekme
            binding.skillsTab.setBackgroundColor(Color.parseColor("#505050")) // Pasif sekme
        }

        // **Yetenekler sekmesine tıklanınca yetenek bilgilerini göster**
        binding.skillsTab.setOnClickListener {
            binding.detailsLayout.visibility = View.GONE
            binding.SkillsLayout.visibility = View.VISIBLE
            binding.detailsTab.setBackgroundColor(Color.parseColor("#505050")) // Pasif sekme
            binding.skillsTab.setBackgroundColor(Color.parseColor("#4CAF50")) // Aktif sekme
        }

        // **Tekrar dene butonu hata ekranında gösterilir**
        // Eğer veri çekme işlemi başarısız olursa, tekrar verileri almak için çağrılır.
        binding.retryButton.setOnClickListener {
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameDetails(gameidd))
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameScreenshots(gameSlugg))
            println("Retry button tıklandı.")
        }

        // **Favori butonuna tıklanınca oyunu favorilere ekle**
        binding.favoriteButton.setOnClickListener {
            val game = getGameDetailsForFavorite()
            gameDetailsviewModel.onDetailsFavoritesEvent(DetailsFavoritesEvent.AddToFavorites(game))
        }

        // **Fragment'a gelen argümanları al ve ViewModel'e ilet**
        arguments?.let { bundle ->
            val gameId = GameDetailsFragmentArgs.fromBundle(bundle).gameId
            val gameSlug = GameDetailsFragmentArgs.fromBundle(bundle).slug

            // **Oyun detaylarını getir**
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameDetails(gameId))

            // **Oyun için ekran görüntülerini getir**
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameScreenshots(gameSlug))

            // **Oyunun favori olup olmadığını kontrol et**
            gameDetailsviewModel.isGameFavorited(gameId)

            gameidd = gameId
            gameSlugg = gameSlug
            println("Oyun ID: $gameidd")
        }

        // **ViewModel'den gelen verileri gözlemle**
        observeGameDetailState()  // Oyun detayları
        observeScreenShootState() // Oyun ekran görüntüleri
        observeGameTrailerState() // Oyun fragmanları
        observeInsertFavorite()   // Favorilere ekleme işlemi
        observeIsFavorited()      // Oyunun favorilere eklenip eklenmediğini kontrol et
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // **Binding'i null yaparak hafıza sızıntısını önle**
        _binding = null
    }

    /**
     * Oyun detayları ekranındaki değişiklikleri gözlemleyen fonksiyon.
     * Kullanıcı detay sayfasına girdiğinde:
     *  1. Oyun detayları yüklenirken, yükleme ekranı gösterilir.
     *  2. Oyun detayları başarıyla yüklendiğinde UI güncellenir.
     *  3. Eğer hata oluşursa, kullanıcıya hata mesajı gösterilir.
     */
    fun observeGameDetailState() {
        gameDetailsviewModel.gameDetailsState.observe(viewLifecycleOwner) { gameDetailsState ->
            println("Observe başladı. Yükleme durumu: ${gameDetailsState.isLoading}")

            // Eğer veriler yükleniyorsa, sadece yükleme ekranını göster
            if (gameDetailsState.isLoading) {
                showLoadingState()
            }

            // Eğer oyun detayları başarıyla alındıysa, UI'yi güncelle
            gameDetailsState.gameDetail?.let { gameDetail ->
                println("Oyun detayları başarıyla alındı.")

                showGameDetails(gameDetail)
            }

            // Eğer hata oluştuysa, hata ekranını göster
            gameDetailsState.error?.let { errorMessage ->
                showErrorState(errorMessage)
            }
        }
    }

    /**
     * Oyun detayları yüklenirken UI'yi güncelleyen fonksiyon.
     */
    private fun showLoadingState() {
        binding.loadingLayout.visibility = View.VISIBLE
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
        binding.starIcon.visibility = View.GONE
    }

    /**
     * Oyun detayları başarıyla yüklendiğinde UI'yi güncelleyen fonksiyon.
     */
    private fun showGameDetails(gameDetail: GameDetail) {
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

        // Oyun detaylarını UI'ye set et
        binding.gameTitle.text = gameDetail.title
        binding.releaseDate.text = gameDetail.releaseDate
        Glide.with(binding.root.context).load(gameDetail.backgroundImageUrl).into(binding.gameIcon)
        imageUrl = gameDetail.backgroundImageUrl
        binding.ratingText.text = gameDetail.rating.toString()
        binding.genre.text = gameDetail.genres.joinToString(", ") { it.name }
        binding.descriptionContent.text = gameDetail.description
        binding.pegi.text = "${gameDetail.esrbRating},"

        // PEGI ikonunu belirle
        binding.pegiIcon.setImageResource(getPegiIcon(gameDetail.esrbRating))

        // Yayıncı, platform ve etiketleri ekle
        addTextToFlexbox(binding.flexboxPublishers, gameDetail.publisher)
        addTextToFlexbox(binding.flexboxPlatforms, gameDetail.platforms)
        addTextToFlexbox(binding.flexboxTags, gameDetail.tags)
    }

    /**
     * Oyun detayları yüklenirken hata oluştuğunda UI'yi güncelleyen fonksiyon.
     */
    private fun showErrorState(errorMessage: String) {
        binding.loadingLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.errorMessage.text = errorMessage

        // Diğer UI bileşenlerini gizle
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
    }

    /**
     * PEGI yaş sınıflandırmasına göre ilgili ikonun ID'sini döndüren fonksiyon.
     */
    private fun getPegiIcon(pegiRating: String): Int {
        return when (pegiRating) {
            "(Early Childhood" -> R.drawable.everychild
            "Everyone" -> R.drawable.everyone
            "Everyone 10+" -> R.drawable.everyone10
            "Teen" -> R.drawable.teen
            "Mature" -> R.drawable.mature
            "Adults Only" -> R.drawable.adultonly
            "Rating Pending" -> R.drawable.ratingpanding
            else -> R.drawable.ratingpanding
        }
    }

    /**
     * Bir metin listesini Flexbox içine ekleyen yardımcı fonksiyon.
     */
    private fun addTextToFlexbox(flexbox: FlexboxLayout, textData: String) {
        // Önce tüm mevcut görünümleri kaldır (önceki verileri temizler)
        flexbox.removeAllViews()

        // Metni virgülle ayırıp her bir elemanı oluştur ve ekle
        textData.split(",").forEach { item ->
            val textView = TextView(flexbox.context).apply {
                text = item.trim() // ✅ text özelliği doğru şekilde atandı
                setPadding(16, 8, 16, 8)
                setTextColor(ContextCompat.getColor(flexbox.context, R.color.white))
                background = ContextCompat.getDrawable(flexbox.context, R.drawable.rounded_background)

                layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
            }

            // Flexbox içine yeni oluşturulan TextView ekleniyor
            flexbox.addView(textView)
        }
    }





    /**
     * Oyun detayları ekranında fragmana (trailer) tıklanınca izlenmesini sağlayan fonksiyon.
     * Kullanıcı "Play" butonuna bastığında:
     *  1. ViewModel'e fragmanı yüklemesi için bir istek gönderilir.
     *  2. Fragman yüklenirken UI yükleme ekranına geçer.
     *  3. Fragman başarıyla yüklendiğinde video oynatıcı ekranına yönlendirilir.
     *  4. Eğer hata oluşursa, kullanıcıya hata mesajı gösterilir.
     */
    fun observeGameTrailerState() {
        // Play butonuna tıklama olayını dinle
        binding.playButton.setOnClickListener {
            println("Play butonuna basıldı.")

            // ViewModel'e fragman verisini çekmesi için istek gönder
            gameDetailsviewModel.onDetailsEvent(GameDetailsEvent.getGameTrailers(gameidd))
            println("Observe metodu çağrıldı, gameId: $gameidd")

            // Fragman verisi yüklendiğinde gözlemleyerek UI'yi güncelle
            gameDetailsviewModel.gameTrailerState.observe(viewLifecycleOwner) { gameTrailerState ->
                println("Trailer observer içinde.")

                // Eğer fragman yükleniyorsa, yükleme ekranını göster
                if (gameTrailerState.isLoading) {
                    println("Fragman yükleniyor...")
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

                // Fragman başarıyla yüklendiyse, video oynatma ekranına yönlendir
                gameTrailerState.gameTrailer?.let { trailers ->
                    println("Trailer başarıyla alındı.")

                    val url = trailers.first().videoUrl // İlk fragmanın URL'sini al
                    val action =
                        GameDetailsFragmentDirections.actionGameDetailsFragmentToVideoPlayerFragment(url)

                    // Video oynatıcı ekranına geçiş yap
                    Navigation.findNavController(requireView()).navigate(action)
                    println("Video oynatma ekranına geçildi.")
                }

                // Eğer fragman yükleme sırasında hata oluştuysa, hata ekranını göster
                gameTrailerState.error?.let { errorMessage ->
                    println("Fragman yüklenirken hata oluştu: $errorMessage")

                    binding.loadingLayout.visibility = View.GONE
                    binding.starIcon.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.errorMessage.text = errorMessage
                    binding.retryButton.text = "Geri Dön"

                    // Diğer UI bileşenlerini gizle
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
                }
            }
        }
    }


    /**
     * Ekran görüntülerini (screenshots) gözlemleyerek UI'yi günceller.
     * Eğer veri yükleniyorsa, yükleme durumu gösterilir.
     * Eğer hata oluşursa loglara yazılır.
     * Eğer başarıyla yüklenmişse, ekran görüntüleri adaptöre güncellenir.
     */
    fun observeScreenShootState() {
        gameDetailsviewModel.screenShootState.observe(viewLifecycleOwner) { screenShootState ->

            // Yükleme durumunda log yazdırılır
            if (screenShootState.isLoading) {
                println("ScreenShoot yükleniyor...")
            }

            // Eğer ekran görüntüleri başarıyla alınmışsa, RecyclerView adaptörüne güncellenir
            screenShootState.gameScreenshoots?.let { screenShoots ->
                println("ScreenShoot verisi başarıyla alındı.")
                screenShootsAdapter.updateScreenshotList(screenShoots)
            }

            // Hata durumu varsa loga yazdırılır
            screenShootState.error?.let { error ->
                println("ScreenShoot verisi yüklenirken hata oluştu: $error")
            }
        }
    }

    /**
     * Kullanıcının bir oyunu favorilere ekleme işlemini gözlemleyen fonksiyon.
     * - Eğer işlem yükleniyorsa, log atılır.
     * - Başarılı olursa UI güncellenir (favori butonu sarı yapılır).
     * - Hata olursa hata mesajı kullanıcıya gösterilir.
     */
    fun observeInsertFavorite() {
        gameDetailsviewModel.insertState.observe(viewLifecycleOwner) { insertState ->

            when {
                // Yükleme durumu devam ederken log atılır
                insertState.isLoading -> {
                    println("Favoriye ekleme işlemi başladı...")
                }

                // Eğer favoriye ekleme başarılı olduysa:
                insertState.isSuccess -> {
                    println("Favoriye ekleme işlemi başarılı!")

                    // Favori butonunun rengini sarıya çevir
                    binding.favoriteButton.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow // Favoriye eklendiyse buton sarı olur
                        )
                    )

                    // Kullanıcıya bilgi mesajı göster
                    Toast.makeText(
                        requireContext(),
                        "Favorilere Eklendi",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Eğer hata oluştuysa:
                insertState.errorMessage != null -> {
                    println("Favoriye ekleme işlemi başarısız: ${insertState.errorMessage}")

                    // Hata mesajını kullanıcıya göster
                    Toast.makeText(
                        requireContext(),
                        insertState.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    /**
     * Oyun detaylarının favorilere eklenip eklenmediğini kontrol eden fonksiyon.
     * Kullanıcı bir oyunu favorilere eklediğinde yıldız ikonunun rengini değiştirir.
     */
    fun observeIsFavorited() {
        // Favori durumu ViewModel'den gözlemlenir
        gameDetailsviewModel.isGameFavoritedState.observe(viewLifecycleOwner) { isFavoritedState ->

            // Eğer sorgulama devam ediyorsa log atılır
            if (isFavoritedState.isLoading) {
                println("Favori durumu sorgulanıyor...")
            }

            // Eğer oyun favorilere eklenmişse, favori butonunu sarı renge çevir
            if (isFavoritedState.success) {
                println("Favori durumu başarıyla sorgulandı: ${isFavoritedState.success}")

                binding.favoriteButton.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellow // Favori durumu başarılı ise sarı renge çevir
                    )
                )

            } else {
                // Eğer favorilere eklenmemişse, butonu varsayılan beyaz renge döndür
                println("Favori durumu sorgulandı: ${isFavoritedState.success}")

                binding.favoriteButton.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white // Favori değilse beyaz renk kullan
                    )
                )
            }

            // Eğer bir hata oluşmuşsa, hatayı logla
            if (isFavoritedState.errorMessage != null) {
                println("Favori durumu sorgulanırken hata oluştu: ${isFavoritedState.errorMessage}")
            }
        }
    }

    /**
     * Kullanıcının bir oyunu favorilere eklemesi için gerekli oyun detaylarını alır.
     *
     * @return Game objesi, favorilere eklenmek için hazırlanır.
     */
    private fun getGameDetailsForFavorite(): Game {
        return Game(
            id = gameidd, // Oyun ID'si
            name = binding.gameTitle.text.toString(), // Oyun adı
            released = binding.releaseDate.text.toString(), // Çıkış tarihi
            backgroundImage = imageUrl, // Oyun görsel URL'si
            rating = binding.ratingText.text.toString().toFloatOrNull(), // Oyun puanı (null olabilir)
            slug = gameSlugg, // Oyun Slug'ı (URL dostu adı)
            genres = binding.genre.text.toString().split(",").map { genreName ->
                Genres(
                    name = genreName.trim() // Türleri parçala ve nesneye dönüştür
                )
            }
        )
    }




}









