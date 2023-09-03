package com.jacgr.filmsdbroom.application

import android.app.Application
import com.jacgr.filmsdbroom.data.FilmRepository
import com.jacgr.filmsdbroom.data.db.FilmDataBase

class FilmsDBApp(): Application() {
    private val database by lazy {
        FilmDataBase.getDatabase(this@FilmsDBApp)
    }

    val repository by lazy {
        FilmRepository(database.filmDao())
    }
}