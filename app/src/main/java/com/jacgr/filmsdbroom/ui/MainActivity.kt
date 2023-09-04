package com.jacgr.filmsdbroom.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jacgr.filmsdbroom.R
import com.jacgr.filmsdbroom.application.FilmsDBApp
import com.jacgr.filmsdbroom.data.FilmRepository
import com.jacgr.filmsdbroom.data.db.model.FilmEntity
import com.jacgr.filmsdbroom.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var films: List<FilmEntity> = emptyList()
    private lateinit var repository: FilmRepository

    private lateinit var filmAdapter: FilmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as FilmsDBApp).repository

        filmAdapter = FilmAdapter(){film ->
            filmClicked(film)
        }

        binding.rvFilms.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = filmAdapter
        }


        prepareUI()
        updateUI()
    }

    private fun prepareUI() {
        binding.btnAdd.setOnClickListener {
            val dialog = FilmDialog(updateUI =  {
                updateUI()
            }, message = {text ->
                message(text)
            })
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    private fun updateUI(){
        lifecycleScope.launch {
            films = repository.getAllFilms()

            if(films.isNotEmpty()){
                //Hay por lo menos un registro
                binding.tvSinRegistros.visibility = View.INVISIBLE
            }else{
                //No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            filmAdapter.updateList(films)
        }
    }

    private fun filmClicked(film: FilmEntity) {
        val dialog = FilmDialog(newFilm = false, film = film, updateUI = {
            updateUI()
        }, message = {text ->
            message(text)
        })
        dialog.show(supportFragmentManager, "dialog")
    }

    private fun message(text: String){
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }


}