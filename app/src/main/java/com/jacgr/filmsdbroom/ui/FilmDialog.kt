package com.jacgr.filmsdbroom.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jacgr.filmsdbroom.R
import com.jacgr.filmsdbroom.application.FilmsDBApp
import com.jacgr.filmsdbroom.data.FilmRepository
import com.jacgr.filmsdbroom.data.db.model.FilmEntity
import com.jacgr.filmsdbroom.databinding.FilmDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

class FilmDialog(
    private val newFilm: Boolean = true,
    private var film: FilmEntity = FilmEntity(
        title = "",
        genre = 0,
        year = "",
        stars = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment() {
    private var _binding: FilmDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: FilmRepository

    //Se configura el dialogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FilmDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as FilmsDBApp).repository

        builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)

        binding.apply {
            tietTitle.setText(film.title)
            tietGenre.setText(film.genre.toString())
            tietYear.setText(film.year)
            tietStars.setText(film.stars)
        }

        dialog = if(newFilm){
            buildDialog("Guardar", "Cancelar", {
                //Create (Guardar)
                film.title = binding.tietTitle.text.toString()
                film.genre = binding.tietGenre.text.toString().toInt()
                film.year = binding.tietYear.text.toString()
                film.stars = binding.tietStars.text.toString()

                try {
                    lifecycleScope.launch {
                        repository.insertFilm(film)
                    }
                    message("Juego guardado exitosamente")

                    //Actualizar la UI
                    updateUI()

                }catch (e: IOException) {
                    message("Error al guardar el juego")
                    e.printStackTrace()
                }
            }, {
                //Cancelar
            })
        }else{
            buildDialog("Actualizar", "Borrar", {
                //Update
                film.title = binding.tietTitle.text.toString()
                film.genre = binding.tietGenre.text.toString().toInt()
                film.year = binding.tietYear.text.toString()
                film.stars = binding.tietStars.text.toString()

                try {
                    lifecycleScope.launch {
                        repository.updateFilm(film)
                    }
                    message("Juego actualizado exitosamente")

                    //Actualizar la UI
                    updateUI()

                }catch (e: IOException) {
                    e.printStackTrace()
                    message("Error al actualizar el juego")
                }

            }, {
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmacion")
                    .setMessage("Realmente deseas eliminar el juego ${film.title}?")
                    .setPositiveButton("Aceptar"){ _, _ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteFilm(film)
                            }
                            message("Juego eliminado exitosamente")

                            //Actualizar la UI
                            updateUI()

                        }catch (e: IOException) {
                            e.printStackTrace()
                            message("Error al eliminar el juego")
                        }
                    }
                    .setNegativeButton("Cancelar"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            })
        }

        return dialog
    }

    //Cuando se destruye el fragment
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se llama despues de que se muestra el dialogo en pantalla
    override fun onStart() {
        super.onStart()

        val alertDialog = dialog as AlertDialog//Lo usamos para poder emplear el metodo getButton (no lo tiene el dialogo)
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietGenre.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietYear.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietStars.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

    }

    private fun validateFields(): Boolean {
        return (binding.tietTitle.text.toString().isNotEmpty() && binding.tietGenre.text.toString().isNotEmpty()
                && binding.tietYear.text.toString().isNotEmpty() && binding.tietStars.text.toString().isNotEmpty())
    }

    private fun buildDialog(btn1Text: String, btn2Text: String, positiveButton: () -> Unit, negativeButton: () -> Unit): Dialog =
        builder.setView(binding.root)
            .setTitle("Film")//TODO quitar el hardcoding
            .setPositiveButton(btn1Text){ dialog, _ ->
                //Accion para el boton positivo
                positiveButton()
            }
            .setNegativeButton(btn2Text){ dialog, _ ->
                //Accion para el boton negativo
                negativeButton()
            }
            .create()


}