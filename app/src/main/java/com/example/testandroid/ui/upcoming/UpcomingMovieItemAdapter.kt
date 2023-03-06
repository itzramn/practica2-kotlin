package com.example.testandroid.ui.upcoming


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class UpcomingMovieItemAdapter (
    private val moviesList: List<MovieEntity>,
    private val itemClickListener: OnMovieClickListener
) : RecyclerView.Adapter<UpcomingMovieItemAdapter.UpcomingViewHolder>()  {

    interface OnMovieClickListener {
        fun onMovieClick(movieEntity: MovieEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        val binding  = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingViewHolder(binding)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        with(holder){
            with(moviesList[position]) {
                binding.titleMovieText.text = title
                binding.overviewMovieText.text = overview
                binding.percentageMovieText.text = voteAverage.toString()
                binding.releaseMovieText.text = releaseDate
                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + (posterPath ?: ""))
                    .into(binding.posterMovieImage)

                holder.itemView.setOnClickListener {
                    itemClickListener.onMovieClick(this)
                }
            }
        }
    }

    inner class UpcomingViewHolder(val binding: ItemMovieBinding)
        :RecyclerView.ViewHolder(binding.root)

}