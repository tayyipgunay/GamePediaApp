package com.tayyipgunay.gamepedia.data.repository.localgamerepositoryimpl

import android.database.sqlite.SQLiteConstraintException
import com.tayyipgunay.gamepedia.data.local.GameDao
import com.tayyipgunay.gamepedia.data.local.GameEntity
import com.tayyipgunay.gamepedia.domain.repository.localgamerepository.LocalGameRepository
import com.tayyipgunay.gamepedia.util.Resource
import javax.inject.Inject

class LocalGameRepositoryImpl @Inject constructor(private val gameDao: GameDao) :
    LocalGameRepository {

    // Tüm oyunları veritabanından getirir ve sonucu Resource olarak döndürür
    override suspend fun getAllGames(): Resource<List<GameEntity>> {
        return try {
            val games = gameDao.getAllGames()
            if (games.isEmpty()) {
                println("veritabanında oyun yok")
                Resource.Error("No games found.") // Eğer veritabanında oyun yoksa hata mesajı döndürülür
            } else {
                Resource.Success(games) // Oyunlar bulunduysa başarılı olarak döndürülür
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred while fetching games: ${e.localizedMessage}") // Hata yönetimi
        }
    }

    // Yeni bir oyunu veritabanına ekler ve sonucu Resource olarak döndürür
    override suspend fun insertGame(game: GameEntity): Resource<Unit> {
        return try {
            println(game.name) // Oyun adını log olarak yazdırır
            gameDao.insertGame(game)
            Resource.Success(Unit) // Başarıyla eklendiğinde Unit döndürülür
        } catch (e: SQLiteConstraintException) {
            Resource.Error("Bu oyun zaten favorilerde.") // Eğer oyun zaten ekliyse hata mesajı döndürülür
        } catch (e: Exception) {
            Resource.Error("An error occurred while inserting the game: ${e.localizedMessage}") // Hata yönetimi
        }
    }

    // Belirtilen ID'ye sahip oyunu veritabanından siler ve sonucu Resource olarak döndürür
    override suspend fun deleteGameById(gameId: Int): Resource<Unit> {
        return try {
            gameDao.deleteGameById(gameId)
            Resource.Success(Unit) // Başarıyla silindiğinde Unit döndürülür
        } catch (e: Exception) {
            Resource.Error("An error occurred while deleting the game: ${e.localizedMessage}") // Hata yönetimi
        }
    }

    // Oyunun favorilere eklenip eklenmediğini kontrol eder ve sonucu Resource olarak döndürür
    override suspend fun isGameFavorited(gameId: Int): Resource<Boolean> {
        return try {
            val isFavorited = gameDao.isGameFavorited(gameId)
            Resource.Success(isFavorited) // Favori olup olmadığı sonucunu döndürür
        } catch (e: Exception) {
            Resource.Error("An error occurred while checking if the game is favorited: ${e.localizedMessage}") // Hata yönetimi
        }
    }
}

