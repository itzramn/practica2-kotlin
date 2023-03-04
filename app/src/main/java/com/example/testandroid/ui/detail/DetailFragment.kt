package com.example.testandroid.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.testandroid.R
import com.example.testandroid.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {


    private val args by navArgs<DetailFragmentArgs>()
    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtTitle.text = args.movie.title
        binding.txtDescription.text = args.movie.overview
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + args.movie.posterPath)
            .into(binding.imgProfile)

        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + args.movie.backdropPath)
            .into(binding.imgBg)

        binding.txtRates.text = String.format(getString(R.string.vote_average), args.movie.voteAverage, args.movie.voteCount )
        binding.releaseMovieText.text = String.format(getString(R.string.release_date), args.movie.releaseDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}