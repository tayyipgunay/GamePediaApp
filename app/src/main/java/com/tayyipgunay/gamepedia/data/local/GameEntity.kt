package com.tayyipgunay.gamepedia.data.local
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.TypeConverters
import com.tayyipgunay.gamepedia.data.dto.GameDto
import com.tayyipgunay.gamepedia.domain.model.Game
import com.tayyipgunay.gamepedia.domain.model.Genres
import com.tayyipgunay.gamepedia.data.local.GenresConverter

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    @ColumnInfo(name = "game_id") // Oyunun benzersiz kimliği
    val id: Int,

    @ColumnInfo(name = "game_name") // Oyunun adı
    val name: String,

    @ColumnInfo(name = "release_date") // Çıkış tarihi
    val released: String?,

    @ColumnInfo(name = "background_image")
    val backgroundImage: String?, // String olarak saklanacak

    @ColumnInfo(name = "game_rating") // Oyunun değerlendirme puanı
    val rating: Float?,
    @ColumnInfo(name = "slug") // Oyunun URL dostu kısa adı
    val slug: String,

    @TypeConverters(GenresConverter::class)
    @ColumnInfo(name = "genres")
    val genres: List<Genres>




// Oyunun türlerini JSON formatında saklar
   // val genres: String


    //@TypeConverters(GenresConverter::class)
)






fun GameEntity.gameEntitytoDomain(): Game {
    return Game(
        id = this.id,
        name = this.name,
        released = this.released,
        backgroundImage = this.backgroundImage,
        rating = this.rating,
        slug = this.slug,
        genres = this.genres.map { genres ->
            Genres(
                name = genres.name
            )
        }
    )
}

        /*genres =this.genres.split(",").map{
         //   println(it)
            Genres(it)
                 }
    )
}
*/



