package com.tayyipgunay.gamepedia.data.local
import androidx.lifecycle.LiveData
import androidx.room.*
import com.tayyipgunay.gamepedia.util.Resource


@Dao
interface GameDao {

    // Tüm oyunları veritabanından getirir
    @Query("SELECT * FROM games")
    fun getAllGames(): List<GameEntity>

    /*
       Yeni bir oyunu veritabanına ekler.
       Eğer eklenmek istenen oyun zaten varsa, işlem başarısız olur (ABORT).
    */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertGame(game: GameEntity)

    // Belirtilen game_id değerine sahip oyunu veritabanından siler
    @Query("DELETE FROM games WHERE game_id = :gameId")
    suspend fun deleteGameById(gameId: Int) // Sadece @Query ile silme işlemi yapılır

    // Belirtilen game_id değerine sahip oyunun favorilere eklenip eklenmediğini kontrol eder
    @Query("SELECT COUNT(*) > 0 FROM games WHERE game_id = :gameId")
    suspend fun isGameFavorited(gameId: Int): Boolean
}



