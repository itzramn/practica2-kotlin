package com.example.testandroid.ui.top

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testandroid.R
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentTopBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class TopFragment : Fragment(), TopMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentTopBinding? = null

    private val binding get() = _binding!!

    private val viewModel: TopViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var topMovieItemAdapter: TopMovieItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTopBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMovies.layoutManager = LinearLayoutManager(context)

        viewModel.fetchTopMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchPopularMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    Log.e("fetchPopularMovies", "Success")
                    topMovieItemAdapter = TopMovieItemAdapter(it.data!!, this@TopFragment)
                    binding.rvMovies.adapter = topMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchPopularMovies", "Failure: ${it.message} ")
                    Toast.makeText(requireContext(), "Failure: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = TopFragmentDirections.actionTopFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }
}