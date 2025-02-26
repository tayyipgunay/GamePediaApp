package com.tayyipgunay.gamepedia.presentation.favorites.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tayyipgunay.gamepedia.R
import com.tayyipgunay.gamepedia.databinding.FavoriteRecyclerRowBinding
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.presentation.favorites.fragment.FavoritesFragmentDirections

class FavoriteAdapter  (private val gameList: ArrayList<Game>) : RecyclerView.Adapter<FavoriteAdapter.GameViewHolder>() {


    // GameViewHolder, RecyclerView'de her bir öğenin görünümünü tutar
    class GameViewHolder(val favoriteRecyclerRow: FavoriteRecyclerRowBinding) :
        RecyclerView.ViewHolder(favoriteRecyclerRow.root)

    // onCreateViewHolder: ViewHolder nesnesini oluşturur ve layout'u bağlar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding =
            FavoriteRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    // getItemCount: Listedeki toplam öğe sayısını döndürür
    override fun getItemCount(): Int {
        return gameList.size
    }

    // onBindViewHolder: Veriyi ViewHolder ile bağlar ve UI'yi günceller
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position] // Liste içinden ilgili konumdaki oyunu alır

        // Oyun başlığı, puanı ve çıkış tarihini ilgili TextView'lere atar
        holder.favoriteRecyclerRow.gameTitleTextView.text = game.name
        holder.favoriteRecyclerRow.gameRatingTextView.text = game.rating.toString()
        holder.favoriteRecyclerRow.gameReleaseDateTextView.text = game.released

        // Türleri birleştirerek ", " ile ayrılmış bir metin oluşturur
        val genresText = game.genres.joinToString(", ") { genre ->
            genre.name
        }
        holder.favoriteRecyclerRow.gameGenreTextView.text = genresText

        // Glide için resim yükleme ayarları
        val requestOptions = RequestOptions()
            .placeholder(placeHolderProgressBar(holder.itemView.context)) // Dönen yükleme göstergesi
            // .error(R.drawable.error_image) // Hata resmi (eğer hata resminiz varsa ekleyebilirsiniz)
            .centerCrop()

        // Oyun arka plan görselini Glide ile yükler
        println("Favorite adapter içindeyiz " + game.backgroundImage)

        Glide.with(holder.itemView.context)
            .load(game.backgroundImage)
            .apply(requestOptions)
            .into(holder.favoriteRecyclerRow.gameImageView)

        // Oyun öğesine tıklanınca detay sayfasına yönlendirme yapar
        holder.itemView.setOnClickListener {
            println(game.id)
            println(game.slug)
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToGameDetailsFragment(
                game.id,
                game.slug
            )
            Navigation.findNavController(it).navigate(action)
        }



    /* holder.favoriteRecyclerRow.deleteButton.setOnClickListener{
        val clickId=game.id
        println(clickId)
        println(game.id)





    }*/


    }
    // getGameAt metodu


    fun getGameAt(position: Int): Game {
        // Belirtilen konumdaki oyunu listeden döndürür
        return gameList[position]
    }

    fun updateGameList(newGameList: List<Game>) {
        // Mevcut listeyi temizler ve yeni oyun listesini ekler
        gameList.clear()
        gameList.addAll(newGameList)
        notifyDataSetChanged() // RecyclerView'in güncellenmesi için bildirim gönderir
    }

    fun removeGame(position: Int) {
        // Belirtilen konumdaki oyunu listeden kaldırır
        gameList.removeAt(position)
        notifyItemRemoved(position) // RecyclerView'de kaldırılan öğeyi günceller
    }

    fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
        // CircularProgressDrawable nesnesi oluşturuluyor (Yüklenme göstergesi)
        return CircularProgressDrawable(context).apply {
            strokeWidth = 8f    // Döner çubuğun kalınlığı (çizgi genişliği)
            centerRadius = 40f  // Döner çubuğun merkezi yarıçapı
            start()             // Animasyonu başlat
        }
    }
}
