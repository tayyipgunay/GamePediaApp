package com.tayyipgunay.gamepedia.data.local
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tayyipgunay.gamepedia.domain.model.Genres

class GenresConverter {

    @TypeConverter
    fun fromGenresList(genres: List<Genres>): String {
        return Gson().toJson(genres) // JSON formatına dönüştür
    }

    @TypeConverter
    fun toGenresList(genresString: String): List<Genres> {
        return Gson().fromJson(genresString, object : TypeToken<List<Genres>>() {}.type)
    }
}

