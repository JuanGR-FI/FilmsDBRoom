package com.jacgr.filmsdbroom.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jacgr.filmsdbroom.R
import com.jacgr.filmsdbroom.data.db.model.FilmEntity
import com.jacgr.filmsdbroom.databinding.FilmElementBinding

class FilmAdapter(private val onFilmClicked: (FilmEntity) -> Unit): RecyclerView.Adapter<FilmAdapter.ViewHolder>() {

    private var films: List<FilmEntity> = emptyList()

    class ViewHolder(private val binding: FilmElementBinding): RecyclerView.ViewHolder(binding.root) {

        val rivIcon = binding.ivIcon

        fun bind(film: FilmEntity) {
            binding.apply {
                tvTitle.text = film.title
                tvYear.text = film.year
                tvStars.text = film.stars
                tvGenre.text = when(film.genre){
                    0 -> "Terror"
                    1 -> "Comedy"
                    2-> "Animation"
                    3 -> "Romance"
                    4 -> "Science Fiction"
                    else -> "Not found"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmAdapter.ViewHolder {
        val binding = FilmElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FilmAdapter.ViewHolder, position: Int) {
        val film = films[position]

        holder.bind(film)

        when(film.genre) {
            0 -> holder.rivIcon.setImageResource(R.drawable.ghost)
            1 -> holder.rivIcon.setImageResource(R.drawable.comedy)
            2 -> holder.rivIcon.setImageResource(R.drawable.animation)
            3 -> holder.rivIcon.setImageResource(R.drawable.heart)
            4 -> holder.rivIcon.setImageResource(R.drawable.science_fiction)
        }

        holder.itemView.setOnClickListener {
            onFilmClicked(films[position])
        }

    }

    override fun getItemCount(): Int  = films.size

    fun updateList(list: List<FilmEntity>) {
        films = list
        notifyDataSetChanged()
    }

}