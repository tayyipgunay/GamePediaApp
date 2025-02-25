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


    class GameViewHolder(val favoriteRecyclerRow: FavoriteRecyclerRowBinding) : RecyclerView.ViewHolder(favoriteRecyclerRow.root){

    }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = FavoriteRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]
        holder.favoriteRecyclerRow.gameTitleTextView.text=game.name
        holder.favoriteRecyclerRow.gameRatingTextView.text=game.rating.toString()
       holder.favoriteRecyclerRow.gameReleaseDateTextView.text=game.released


        val genresText = game.genres.joinToString(", ") { genre->
            genre.name
        } // Türleri ", " ile ayırarak bir metin oluşturur
        holder.favoriteRecyclerRow.gameGenreTextView.text = genresText


        val requestOptions = RequestOptions()
            .placeholder(placeHolderProgressBar(holder.itemView.context)) // Dönen GIF
            //  .error(R.drawable.error_image) // Hata resmi
            .centerCrop()




       // holder.favoriteRecyclerRow.gameGenreTextView.text = game.genres
           println("favorite adapter içindeyiz "+game.backgroundImage)


        Glide.with(holder.itemView.context).load(game.backgroundImage).apply(requestOptions).
        into(holder.favoriteRecyclerRow.gameImageView)





        holder.itemView.setOnClickListener {
            // Öğe tıklandığında yapılacak işlemler
            println(game.id)
            println(game.slug)
            val action =FavoritesFragmentDirections.actionFavoritesFragmentToGameDetailsFragment(game.id,game.slug)
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

        return gameList[position]

    }

    fun updateGameList(newGameList: List<Game>) {
        gameList.clear()
        gameList.addAll(newGameList)
        notifyDataSetChanged()

    }
    fun removeGame(position: Int) {
        gameList.removeAt(position) // Listeden öğeyi kaldır
        notifyItemRemoved(position) // RecyclerView'i güncelle
    }

    fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
        // CircularProgressDrawable nesnesi oluşturuluyor
        return CircularProgressDrawable(context).apply {
            strokeWidth = 8f    // Döner çubuğun kalınlığı (çizgi genişliği)
            centerRadius = 40f  // Döner çubuğun merkezi yarıçapı

            start()             // Animasyonu başlat
        }
    }
}
