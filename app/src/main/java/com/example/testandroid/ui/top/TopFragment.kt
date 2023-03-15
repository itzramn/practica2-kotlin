package com.example.testandroid.ui.top

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
import com.example.testandroid.databinding.FragmentTopBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class TopFragment : Fragment(), TopMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentTopBinding? = null

    private val binding get() = _binding!!

    private val viewModel: TopViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private val visibleThreshold = 5 // Cantidad de elementos restantes antes de llegar al final de la lista para solicitar más elementos
    private var isLoading = false
    private var isLoadingMore = false

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

        viewModel.topMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchTopMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    Log.e("fetchTopMovies", "Success")
                    topMovieItemAdapter = TopMovieItemAdapter(it.data!!, this@TopFragment)
                    binding.rvMovies.adapter = topMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchTopMovies", "Failure: ${it.message} ")
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
                        viewModel.fetchTopMovies()
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
        val action = TopFragmentDirections.actionTopFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }
}