package com.jacgr.filmsdbroom.data.db

import androidx.room.*
import com.jacgr.filmsdbroom.data.db.model.FilmEntity
import com.jacgr.filmsdbroom.util.Constants.DATABASE_FILM_TABLE

@Dao
interface FilmDao {
    //Create
    @Insert
    suspend fun insertFilm(film: FilmEntity)

    //Read
    @Query("SELECT * FROM ${DATABASE_FILM_TABLE}")
    suspend fun getAllFilms(): List<FilmEntity>

    //Update
    @Update
    suspend fun updateFilm(film: FilmEntity)

    //Delete
    @Delete
    suspend fun deleteFilm(film: FilmEntity)

}