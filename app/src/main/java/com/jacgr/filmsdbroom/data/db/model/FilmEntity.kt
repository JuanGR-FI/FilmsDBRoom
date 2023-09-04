package com.jacgr.filmsdbroom.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jacgr.filmsdbroom.util.Constants

@Entity(tableName = Constants.DATABASE_FILM_TABLE)
data class FilmEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "film_id")
    val id: Long = 0,

    @ColumnInfo(name = "film_title")
    var title: String,

    @ColumnInfo(name = "film_genre")
    var genre: Int,

    @ColumnInfo(name = "film_year")
    var year: String,

    @ColumnInfo(name = "film_stars")
    var stars: String,

)
