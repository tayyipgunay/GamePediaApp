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

class ScreenShootsAdapter(private val screenshots: ArrayList<Screenshot>) :
    RecyclerView.Adapter<ScreenShootsAdapter.ScreenShootHolder>() {

    // ViewHolder sınıfı: Her bir öğenin görünümünü tutar
    class ScreenShootHolder(val screenShootHolder: ScreenShootsRowBinding) :
        RecyclerView.ViewHolder(screenShootHolder.root)

    // onCreateViewHolder: ViewHolder nesnesini oluşturur ve layout'u bağlar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenShootHolder {
        val binding =
            ScreenShootsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScreenShootHolder(binding)
    }

    // getItemCount: Listedeki toplam öğe sayısını döndürür
    override fun getItemCount(): Int {
        return screenshots.size
    }

    // onBindViewHolder: Veriyi ViewHolder ile bağlar ve UI'yi günceller
    override fun onBindViewHolder(holder: ScreenShootHolder, position: Int) {
        // Glide için resim yükleme ayarları
        val requestOptions = RequestOptions()
            .placeholder(placeHolderProgressBar(holder.itemView.context)) // Yükleme göstergesi
            // .error(R.drawable.error_image) // Hata resmi (isteğe bağlı olarak eklenebilir)
            .centerCrop()
            .fitCenter()

        // İlgili konumdaki ekran görüntüsünü alır
        val screenshot = screenshots[position]

        // Glide ile ekran görüntüsünü ImageView'e yükler
        Glide.with(holder.itemView.context)
            .load(screenshot.imageUrl)
            .apply(requestOptions)
            .into(holder.screenShootHolder.imageViewScreenshot)
    }

    // Yeni ekran görüntülerini adaptöre ekler ve günceller
    fun updateScreenshotList(newScreenshotList: List<Screenshot>) {
        screenshots.clear()
        screenshots.addAll(newScreenshotList)
        notifyDataSetChanged() // RecyclerView'i günceller
    }

    // CircularProgressDrawable: Yükleme sırasında gösterilecek animasyonlu ilerleme çubuğu
    fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 8f    // Döner çubuğun kalınlığı (çizgi genişliği)
            centerRadius = 40f  // Döner çubuğun merkezi yarıçapı
            start()             // Animasyonu başlat
        }
    }
}
