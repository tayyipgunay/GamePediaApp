package com.tayyipgunay.gamepedia.data.repository.localgamerepositoryimpl

import android.database.sqlite.SQLiteConstraintException
import com.tayyipgunay.gamepedia.data.local.GameDao
import com.tayyipgunay.gamepedia.data.local.GameEntity
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class LocalGameRepositoryImpl @Inject constructor(private val gameDao: GameDao) :
    LocalGameRepository {
    override suspend fun getAllGames(): Resource<List<GameEntity>> {
        return try {
            val games = gameDao.getAllGames()
            if (games.isEmpty()) {
                println("veritanıdan oyunn yokkk")
                Resource.Error("No games found.")
            } else {
                Resource.Success(games)
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred while fetching games: ${e.localizedMessage}")
        }
    }

    override suspend fun insertGame(game: GameEntity): Resource<Unit> {
        return try {
            println(game.name)
            gameDao.insertGame(game)
            Resource.Success(Unit) // Bir işlem tamamlandığında Unit döndürür
        }
     catch (e: SQLiteConstraintException)
    {
        Resource.Error("Bu oyun zaten favorilerde.")
    }
        catch (e: Exception) {
            Resource.Error("An error occurred while inserting the game: ${e.localizedMessage}")
        }
    }

    override suspend fun deleteGameById(gameId: Int): Resource<Unit> {
        return try {
            gameDao.deleteGameById(gameId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("An error occurred while deleting the game: ${e.localizedMessage}")
        }
    }

    override suspend fun isGameFavorited(gameId: Int): Resource<Boolean> {
        return try {
            val isFavorited = gameDao.isGameFavorited(gameId)
            Resource.Success(isFavorited)

    }
        catch (e:Exception){
            Resource.Error("An error occurred while checking if the game is favorited: ${e.localizedMessage}")

        }
    }
}


