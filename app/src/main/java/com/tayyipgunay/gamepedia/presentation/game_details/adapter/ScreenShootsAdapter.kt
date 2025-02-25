package com.tayyipgunay.gamepedia.presentation.game_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tayyipgunay.gamepedia.databinding.ScreenShootsRowBinding
import com.tayyipgunay.gamepedia.domain.model.Screenshot

class ScreenShootsAdapter(private val screenshots: ArrayList<Screenshot>):RecyclerView.Adapter<ScreenShootsAdapter.screenShootHolder>() {


    class screenShootHolder(val screenShootHolder: ScreenShootsRowBinding) :
        RecyclerView.ViewHolder(screenShootHolder.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): screenShootHolder {
        val binding =
            ScreenShootsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return screenShootHolder(binding)

    }

    override fun getItemCount(): Int {
        return screenshots.size

    }


    override fun onBindViewHolder(holder: screenShootHolder, position: Int) {

        val requestOptions = RequestOptions()
            .placeholder(placeHolderProgressBar(holder.itemView.context)) // Dönen GIF
            //  .error(R.drawable.error_image) // Hata resmi
            .centerCrop()
            .fitCenter()



        val screenshoot = screenshots[position]
        Glide.with(holder.itemView.context).load(screenshoot.imageUrl).apply(requestOptions)
            .into(holder.screenShootHolder.imageViewScreenshot)

    }

    fun updateScreenshotList(newScreenshotList: List<Screenshot>) {
        screenshots.clear()
        screenshots.addAll(newScreenshotList)
        notifyDataSetChanged()
    }

    fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 8f    // Döner çubuğun kalınlığı (çizgi genişliği)
            centerRadius = 40f  // Döner çubuğun merkezi yarıçapı
            start()             // Animasyonu başlat
        }


    }
}
