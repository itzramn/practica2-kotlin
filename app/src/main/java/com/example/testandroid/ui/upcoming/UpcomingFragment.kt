package com.example.testandroid.ui.upcoming

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.RecyclerView
import com.example.testandroid.R
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentUpcomingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UpcomingFragment : Fragment(), UpcomingMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentUpcomingBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var upcomingMovieItemAdapter: UpcomingMovieItemAdapter

    private val visibleThreshold = 5 // Cantidad de elementos restantes antes de llegar al final de la lista para solicitar más elementos
    private var isLoading = false
    private var isLoadingMore = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMovies.layoutManager = LinearLayoutManager(context)

        viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchUpcomingMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    Log.e("fetchUpcomingMovies", "Success")
                    upcomingMovieItemAdapter = UpcomingMovieItemAdapter(it.data!!, this@UpcomingFragment)
                    binding.rvMovies.adapter = upcomingMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchUpcomingMovies", "Failure: ${it.message} ")
                    Toast.makeText(requireContext(), "Failure: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val handler = Handler(Looper.getMainLooper())

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading && !isLoadingMore) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount <= lastVisiblePosition + visibleThreshold) {
                        Log.e("Voy a cargar mas items", "Confirmo")
                        isLoadingMore = true
                        viewModel.fetchUpcomingMovies()
                        isLoadingMore = false
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = UpcomingFragmentDirections.actionUpcomingFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }
}