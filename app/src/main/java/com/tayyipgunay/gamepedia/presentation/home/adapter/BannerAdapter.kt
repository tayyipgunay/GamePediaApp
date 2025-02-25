package com.tayyipgunay.gamepedia.presentation.home.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tayyipgunay.gamepedia.R
import com.tayyipgunay.gamepedia.databinding.BannerRecyclerRowBinding
import com.tayyipgunay.gamepedia.domain.model.Banner
import javax.inject.Inject


class BannerAdapter @Inject constructor(private var bannerImages: ArrayList<Banner>) : RecyclerView.Adapter<BannerAdapter.BannerHolder>() {
    class BannerHolder(val bannerRecyclerRowBinding: BannerRecyclerRowBinding) : RecyclerView.ViewHolder(bannerRecyclerRowBinding.root) {

    }
    private var error: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerHolder {

        val binding = BannerRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerHolder(binding)

    }


    override fun getItemCount(): Int {

        return if (error) {
            1 // Tek item ->// Hata durumunda sadece bir öğe göster
        } else {
            bannerImages.size
        }
    }








    override fun onBindViewHolder(holder: BannerHolder, position: Int) {
        val requestOptions = RequestOptions()
            .placeholder(placeHolderProgressBar(holder.itemView.context)) // Dönen GIF
            .error(R.drawable.circularerrorimage) // Hata resmi

            .centerCrop()
            .fitCenter()

        var imageUrl: String? = null

        if (error) {//bannerImages.isNullOrEmpty() de olabilir.
            imageUrl = null
        } else {
            imageUrl = bannerImages[position].backgroundImage
        }


        Glide.with(holder.itemView.context).load(imageUrl).apply(requestOptions)
            .into(holder.bannerRecyclerRowBinding.bannerImageView)
    }
















   /* fun updateBannerImages(newBannerImages: List<Banner>?) {

       bannerImages?.clear()
        bannerImages?.addAll(newBannerImages)
        notifyDataSetChanged()
    }*/

       fun updateBannerImages(newBannerImages: List<Banner>?) {
           // null ise boş liste kullan
           val safeList = newBannerImages ?: emptyList()

           // Ardından enjekte edilen bannerImages.clear() ve addAll(safeList) yaparsınız
           bannerImages.clear()
           bannerImages.addAll(safeList)
         notifyDataSetChanged()
       }


    fun updateError(Error: Boolean){
        error=Error

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


