package com.example.testandroid.ui.popular

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testandroid.R
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentPopularBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PopularFragment : Fragment(), PopularMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentPopularBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PopularViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var popularMovieItemAdapter: PopularMovieItemAdapter

    private val visibleThreshold = 5 // Cantidad de elementos restantes antes de llegar al final de la lista para solicitar más elementos
    private var isLoading = false
    private var isLoadingMore = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMovies.layoutManager = LinearLayoutManager(context)

        viewModel.popularMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchPopularMovies", "Loading")
                }
                ResourceStatus.SUCCESS -> {
                    Log.e("fetchPopularMovies", "Success")
                    popularMovieItemAdapter = PopularMovieItemAdapter(it.data!!, this@PopularFragment)
                    binding.rvMovies.adapter = popularMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchPopularMovies", "Failure: ${it.message} ")
                    Toast.makeText(requireContext(), "Failure: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        // Agregar el listener de scroll para el RecyclerView
        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val handler = Handler(Looper.getMainLooper())

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading && !isLoadingMore) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    Log.e("totalItemCount", totalItemCount.toString())
                    Log.e("lastVisiblePosition", lastVisiblePosition.toString())

                    if (totalItemCount <= lastVisiblePosition + visibleThreshold) {
                        Log.e("Voy a cargar mas items", "Confirmo")
                        isLoadingMore = true
                        viewModel.fetchPopularMovies()
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
        val action = PopularFragmentDirections.actionHomeFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }
}
