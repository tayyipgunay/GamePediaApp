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

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var player: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Binding'i başlat
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {


        // ExoPlayer oluştur
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player // PlayerView ile ExoPlayer'ı bağla
        // Safe Args ile gelen argümanı al
         arguments?.let {bundle->
            val videoUrl= VideoPlayerFragmentArgs.fromBundle(bundle).videoUrl
             // Video URL'sini oynatıcıya ekle
             if (!videoUrl.isNullOrEmpty()) {
                 val mediaItem = MediaItem.fromUri(videoUrl)
                 player.setMediaItem(mediaItem)
                 player.prepare()
                 player.playWhenReady = true


             }
         }

        }
        catch (e:Exception){
println(e.message)

        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Hafıza sızıntısını önlemek için
        player.release() // ExoPlayer'ı serbest bırak
    }
    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
        player.pause()
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
    }
    override fun onStart() {
        super.onStart()
// WindowInsetsController ile tam ekran modu
        requireActivity().window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
    }



