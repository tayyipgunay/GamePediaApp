package com.tayyipgunay.gamepedia.presentation.videoPlayer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.tayyipgunay.gamepedia.databinding.FragmentVideoPlayerBinding
import com.tayyipgunay.gamepedia.presentation.videoPlayer.fragment.VideoPlayerFragmentArgs

class VideoPlayerFragment : Fragment() {

    /**
     * VideoPlayerFragment, ExoPlayer kullanarak bir video oynatıcı sunar.
     * - Safe Args ile gelen video URL'ini alır.
     * - ExoPlayer'ı başlatır ve videoyu oynatır.
     * - Kullanıcı arayüzünü tam ekran moduna getirir.
     * - Fragment yaşam döngüsü içinde ExoPlayer yönetimini yapar.
     */

        // ViewBinding değişkeni (Hafıza sızıntısını önlemek için null yapılmalıdır)
        private var _binding: FragmentVideoPlayerBinding? = null
        private val binding get() = _binding!!

        // ExoPlayer oynatıcısı
        private lateinit var player: ExoPlayer

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            // Binding'i başlat
            _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            try {
                // ExoPlayer'ı oluştur ve PlayerView'e bağla
                player = ExoPlayer.Builder(requireContext()).build()
                binding.playerView.player = player

                // Safe Args kullanarak gelen video URL'sini al
                arguments?.let { bundle ->
                    val videoUrl = VideoPlayerFragmentArgs.fromBundle(bundle).videoUrl

                    // Eğer video URL geçerli ise medya oynatıcıya ekle ve başlat
                    if (!videoUrl.isNullOrEmpty()) {
                        val mediaItem = MediaItem.fromUri(videoUrl)
                        player.setMediaItem(mediaItem)
                        player.prepare() // Videoyu hazırla
                        player.playWhenReady = true // Otomatik oynatma başlasın
                    }
                }
            } catch (e: Exception) {
                println("ExoPlayer Hatası: ${e.message}") // Hata durumunda log yazdır
            }
        }

        /**
         * Fragment görünümü yok edilirken:
         * - Hafıza sızıntısını önlemek için binding null yapılır.
         * - ExoPlayer kaynaklarını serbest bırakır.
         */
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
            player.release()
        }

        /**
         * Kullanıcı uygulamadan ayrıldığında:
         * - Video duraklatılır.
         */
        override fun onPause() {
            super.onPause()
            player.playWhenReady = false
            player.pause()
        }

        /**
         * Kullanıcı geri döndüğünde:
         * - Video oynatılmaya devam eder.
         */
        override fun onResume() {
            super.onResume()
            player.playWhenReady = true
        }

        /**
         * Fragment başlatıldığında:
         * - Tam ekran modu aktif edilir.
         */
        override fun onStart() {
            super.onStart()
            requireActivity().window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }



