package com.jacgr.filmsdbroom.data

import com.jacgr.filmsdbroom.data.db.FilmDao
import com.jacgr.filmsdbroom.data.db.model.FilmEntity

class FilmRepository(private val filmDao: FilmDao) {

    suspend fun insertFilm(game: FilmEntity) {
        filmDao.insertFilm(game)
    }

    suspend fun getAllFilms(): List<FilmEntity> = filmDao.getAllFilms()

    suspend fun updateFilm(game: FilmEntity) {
        filmDao.updateFilm(game)
    }

    suspend fun deleteFilm(game: FilmEntity) {
        filmDao.deleteFilm(game)
    }

}