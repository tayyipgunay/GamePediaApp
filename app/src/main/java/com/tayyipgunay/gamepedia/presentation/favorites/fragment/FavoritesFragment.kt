package com.tayyipgunay.gamepedia.presentation.favorites.fragment

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tayyipgunay.gamepedia.databinding.FragmentFavoritesBinding
import com.tayyipgunay.gamepedia.presentation.favorites.adapter.FavoriteAdapter
import com.tayyipgunay.gamepedia.presentation.favorites.event.FavoriteEvent
import com.tayyipgunay.gamepedia.presentation.favorites.viewModel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    // ViewBinding değişkeni
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    // ViewModel tanımlaması (Hilt tarafından sağlanıyor)
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    // Favori oyunları göstermek için RecyclerView adaptörü
    private val favoriteAdapter = FavoriteAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Binding'i başlat
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView için adaptör ve layout manager ayarlanıyor
        binding.favoritesRecyclerView.adapter = favoriteAdapter
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView'de kaydırarak silme işlemini etkinleştir
        recyclerSwipe()

        // ViewModel ile favori oyunları getir
        favoritesViewModel.onEvent(FavoriteEvent.getFavoritesGames)

        // LiveData gözlemcileri başlat
        observe()
        observeDelete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Binding'i temizle (hafıza sızıntısını önlemek için)
    }

    // Favori oyunları gözlemleyen fonksiyon
    fun observe() {
        favoritesViewModel.getFavoriteState.observe(viewLifecycleOwner) { getFavoriteState ->
            if (getFavoriteState.isLoading) {
                binding.loadingProgressBar.visibility = View.VISIBLE
                println("Favorites is loading")
            }

            getFavoriteState.games?.let {
                println("Favorites games var, let içinde çalışıyor")

                // Yeni oyun listesini adaptöre yükle
                favoriteAdapter.updateGameList(getFavoriteState.games)
                println(getFavoriteState.games.get(0).name)

                binding.loadingProgressBar.visibility = View.GONE
            }

            getFavoriteState.errorMessage?.let {
                println("Favorites error var")
            }
        }
    }

    // Silme işlemlerini gözlemleyen fonksiyon
    fun observeDelete() {
        favoritesViewModel.deleteFavoriteState.observe(viewLifecycleOwner) { deleteFavoriteState ->
            if (deleteFavoriteState.isLoading) {
                println("Favorites delete is loading")
            }
            if (deleteFavoriteState.isSuccess) {
                println("Favorite deleted successfully")
                Toast.makeText(requireContext(), "Game deleted", Toast.LENGTH_SHORT).show()

                // Silme işleminden sonra favori oyunları güncelle
                favoritesViewModel.onEvent(FavoriteEvent.getFavoritesGames)
            }
            deleteFavoriteState.errorMessage?.let {
                println("Favorites delete error: ${deleteFavoriteState.errorMessage}")
            }
        }
    }

    // RecyclerView'de kaydırarak silme işlemini yönetir
    fun recyclerSwipe() {
        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                    // Daha küçük bir kaydırma mesafesi (örneğin %10 yeterli)
                    return 0.1f
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    val game = favoriteAdapter.getGameAt(position)

                    // AlertDialog oluştur
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Silme İşlemi")
                    builder.setMessage("${game.name} adlı oyunu silmek istediğinize emin misiniz?")

                    // "Evet" butonu
                    builder.setPositiveButton("Evet") { dialog, _ ->
                        // ViewModel üzerinden favorilerden oyunu sil
                        favoritesViewModel.onEvent(FavoriteEvent.DeleteFavorite(game.id))

                        // Adaptörden silme işlemi
                        favoriteAdapter.removeGame(position)

                        dialog.dismiss()
                    }

                    // "Hayır" butonu
                    builder.setNegativeButton("Hayır") { dialog, _ ->
                        // İşlemi iptal et ve öğeyi geri yükle
                        favoriteAdapter.notifyItemChanged(position)
                        dialog.dismiss()
                    }

                    // Dialogu göster
                    builder.create().show()
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        val itemView = viewHolder.itemView
                        val paint = Paint()
                        paint.color = Color.RED

                        // Kırmızı arka planı kaydırma mesafesine göre çiz
                        c.drawRect(
                            itemView.right + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            paint
                        )
                    }

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            })

        // RecyclerView'e kaydırma işlevini bağla
        itemTouchHelper.attachToRecyclerView(binding.favoritesRecyclerView)
    }
}


    /* fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.getFavoritesGames -> {
                favoritesViewModel.getFavoritesGames()

            }
            is FavoriteEvent.DeleteFavorite -> {
                (event.gameId)
            }
        }
    }*/









