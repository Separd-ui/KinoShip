package com.example.kinoship.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinoship.R
import com.example.kinoship.adapters.MovieAdapter
import com.example.kinoship.databinding.FragmentLatestMovieBinding
import com.example.kinoship.models.Result
import com.example.kinoship.utils.Constans
import com.example.kinoship.utils.Resource
import com.example.kinoship.ui.viewmodels.LatestMovieViewModel
import com.google.android.material.snackbar.Snackbar


class LatestMovieFragment : Fragment(),MovieAdapter.OnMovieListener {
    private lateinit var binding: FragmentLatestMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel by lazy {
        ViewModelProvider(this).get(LatestMovieViewModel::class.java)
    }
    private var isLoading=false
    private var isLastPage=false
    private var isScrolling=false

    private val scrollListenerForRecView = object : RecyclerView.OnScrollListener(){

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPage=!isLoading && !isLastPage
            val isAtLastItem=(firstVisibleItemPosition+visibleItemCount) >= totalItemCount
            val isNotAtBeginning= firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible= totalItemCount >= Constans.QUERY_PAGE_SIZE

            val shouldPaginate= isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.maxPages?.let {
                    if(it>viewModel.moviePage){
                        viewModel.getLatestMovies()
                    }
                }
                isScrolling=false
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_latest_movie, container, false)
        binding= FragmentLatestMovieBinding.bind(view)


        binding.recViewLatest.apply {
            layoutManager=LinearLayoutManager(context)
            movieAdapter=MovieAdapter(context,this@LatestMovieFragment)
            adapter=movieAdapter
            addOnScrollListener(scrollListenerForRecView)
        }

        viewModel.getLatestMovies()

        viewModel.latestMovies.observe(viewLifecycleOwner){
                resourceResponse->
            when(resourceResponse){
                is Resource.Loading ->{
                    isLoading=false
                    binding.latestProgressBar.isVisible=true
                }
                is Resource.Success ->{
                    binding.latestProgressBar.isVisible=false
                    isLoading=false
                    resourceResponse.data?.let {
                            movie-> movieAdapter.differ.submitList(movie.results.toList())
                    }
                }
                is Resource.Error ->{
                    binding.latestProgressBar.isVisible=false
                    isLoading=false
                    Snackbar.make(view,resourceResponse.message!!,Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onMovieClick(movie: Result) {
        val bundle=Bundle().apply {
            putSerializable("movie",movie)
            putBoolean("isSaved",false)
        }
        findNavController().navigate(R.id.action_latestMovieFragment_to_movieDetailFragment,bundle)
    }


}