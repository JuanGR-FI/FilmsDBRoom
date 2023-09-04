package com.jacgr.filmsdbroom.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
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
    private val context: Context,
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

    private var spinnerFlag = false
    private var selectedGenre = 0

    //Se configura el dialogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FilmDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as FilmsDBApp).repository

        builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)

        val datos = arrayListOf(getString(R.string.choose_genre),
                                getString(R.string.terror_genre),
                                getString(R.string.comedy_genre),
                                getString(R.string.animation_genre),
                                getString(R.string.romance_genre),
                                getString(R.string.science_fiction_genre))

        val adapter = ArrayAdapter(requireContext(), R.layout.style_spinner_elements, datos)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.apply {
            tietTitle.setText(film.title)
            tietYear.setText(film.year)
            tietStars.setText(film.stars)

            spinner.setSelection(film.genre)

            selectedGenre = film.genre
        }

        dialog = if(newFilm){
            buildDialog(getString(R.string.save_button_text), getString(R.string.cancel_button_text), {
                //Create (Guardar)
                film.title = binding.tietTitle.text.toString()
                film.year = binding.tietYear.text.toString()
                film.stars = binding.tietStars.text.toString()

                film.genre = binding.spinner.selectedItemPosition

                try {
                    lifecycleScope.launch {
                        repository.insertFilm(film)
                    }
                    message(getString(R.string.film_saved))

                    //Actualizar la UI
                    updateUI()

                }catch (e: IOException) {
                    message(getString(R.string.error_film_saved))
                    e.printStackTrace()
                }
            }, {
                //Cancelar
            })
        }else{
            buildDialog(getString(R.string.update_button_text), getString(R.string.delete_button_text), {
                //Update
                film.title = binding.tietTitle.text.toString()
                film.year = binding.tietYear.text.toString()
                film.stars = binding.tietStars.text.toString()

                film.genre = binding.spinner.selectedItemPosition

                try {
                    lifecycleScope.launch {
                        repository.updateFilm(film)
                    }
                    message(getString(R.string.film_updated))

                    //Actualizar la UI
                    updateUI()

                }catch (e: IOException) {
                    e.printStackTrace()
                    message(getString(R.string.error_film_updated))
                }

            }, {
                //Delete

                AlertDialog.Builder(requireContext(), android.R.style.Theme_DeviceDefault_Dialog_Alert)
                    .setTitle(getString(R.string.confirmation_dialog_title))
                    .setMessage(getString(R.string.alert_dialog_message, film.title))
                    .setPositiveButton(getString(R.string.accept_button_text)){ _, _ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteFilm(film)
                            }
                            message(context.getString(R.string.film_deleted))

                            //Actualizar la UI
                            updateUI()

                        }catch (e: IOException) {
                            e.printStackTrace()
                            message(context.getString(R.string.error_film_deleted))
                        }
                    }
                    .setNegativeButton(getString(R.string.cancel_button_text)){ dialog, _ ->
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
                if(!newFilm){
                    spinnerFlag = true
                }
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.spinner.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != 0 && selectedGenre != position){
                    spinnerFlag = true
                    saveButton?.isEnabled = validateFields()
                }else{
                    spinnerFlag = false
                    saveButton?.isEnabled = validateFields()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinnerFlag = false
                saveButton?.isEnabled = validateFields()
            }

        }

        binding.tietYear.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!newFilm){
                    spinnerFlag = true
                }
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietStars.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!newFilm){
                    spinnerFlag = true
                }
                saveButton?.isEnabled = validateFields()
            }

        })

    }

    private fun validateFields(): Boolean {
        return (binding.tietTitle.text.toString().isNotEmpty()
                && binding.tietYear.text.toString().isNotEmpty()
                && binding.tietStars.text.toString().isNotEmpty() && spinnerFlag)
    }

    private fun buildDialog(btn1Text: String, btn2Text: String, positiveButton: () -> Unit, negativeButton: () -> Unit): Dialog {

        return builder.setView(binding.root)
            .setTitle(getString(R.string.dialog_title))
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



}