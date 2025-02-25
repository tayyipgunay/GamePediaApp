package com.tayyipgunay.gamepedia.data.local
import androidx.lifecycle.LiveData
import androidx.room.*
import com.tayyipgunay.gamepedia.util.Resource


@Dao
interface GameDao {

    @Query("SELECT * FROM games")
    fun getAllGames(): List<GameEntity>


 /*   @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity)*/


 @Insert(onConflict = OnConflictStrategy.ABORT)
 suspend fun insertGame(game: GameEntity)





    @Query("DELETE FROM games WHERE game_id = :gameId")
    suspend fun deleteGameById(gameId: Int) // Sadece @Query ile silme




    @Query("SELECT COUNT(*) > 0 FROM games WHERE game_id = :gameId")
    suspend fun isGameFavorited(gameId: Int): Boolean


}




