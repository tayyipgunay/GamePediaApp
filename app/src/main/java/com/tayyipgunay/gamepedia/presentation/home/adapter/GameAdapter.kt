package com.tayyipgunay.gamepedia.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tayyipgunay.gamepedia.databinding.HomeRecyclerRowBinding
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.presentation.home.fragment.HomeFragmentDirections
import javax.inject.Inject


class GameAdapter @Inject constructor(private val gameList: ArrayList<Game>)
    : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {



    class GameViewHolder(val homeRecyclerRowBinding: HomeRecyclerRowBinding) : RecyclerView.ViewHolder(homeRecyclerRowBinding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = HomeRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
val game =gameList[position]
       holder.homeRecyclerRowBinding.gameTitleTextView.text=game.name
        holder.homeRecyclerRowBinding.gameRatingTextView.text=game.rating.toString()
        holder.homeRecyclerRowBinding.gameReleaseDateTextView.text=game.released


        val genresText = game.genres.joinToString(", ") {genre->
           genre.name
       } // Türleri ", " ile ayırarak bir metin oluşturur
        holder.homeRecyclerRowBinding.gameGenreTextView.text = genresText

        val requestOptions = RequestOptions()
            .placeholder(placeHolderProgressBar(holder.itemView.context)) // Dönen GIF
          //  .error(R.drawable.error_image) // Hata resmi
            .centerCrop()

        Glide.with(holder.itemView.context).load(game.backgroundImage).apply(requestOptions).into(holder.homeRecyclerRowBinding.gameImageView)



        holder.itemView.setOnClickListener {
            // Öğe tıklandığında yapılacak işlemler
            println(game.id)
            println(game.slug)
          val action=  HomeFragmentDirections.actionHomeFragmentToGameDetailsFragment(game.id,game.slug)
            Navigation.findNavController(it).navigate(action)
        }




    }
    fun updateGameList(newGameList: List<Game>) {
        gameList.clear()
        gameList.addAll(newGameList)
        notifyDataSetChanged()

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