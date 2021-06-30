package com.example.kinoship.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinoship.R
import com.example.kinoship.adapters.MovieAdapter
import com.example.kinoship.databinding.FragmentSearchMovieBinding
import com.example.kinoship.models.Result
import com.example.kinoship.utils.Constans
import com.example.kinoship.utils.Resource
import com.example.kinoship.ui.viewmodels.SearchMovieViewModel
import com.google.android.material.snackbar.Snackbar


class SearchMovieFragment : Fragment(),MovieAdapter.OnMovieListener {
    private lateinit var binding:FragmentSearchMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel:SearchMovieViewModel by viewModels()
    private var oldQuery:String?=null
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
                    if(it>viewModel.currentPage ){
                        viewModel.getSearchMovies(oldQuery!!,false)
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
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_search_movie, container, false)

        binding= FragmentSearchMovieBinding.bind(view)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recViewSearch.apply{
            layoutManager=LinearLayoutManager(context)
            movieAdapter= MovieAdapter(context,this@SearchMovieFragment)
            adapter=movieAdapter
            addOnScrollListener(scrollListenerForRecView)
        }


        viewModel.movieResponse.observe(viewLifecycleOwner){
                resource->
            when(resource){
                is Resource.Loading->{
                    binding.searchProgressBar.isVisible=true
                    isLoading=true
                }
                is Resource.Success->{
                    binding.searchProgressBar.isVisible=false
                    isLoading=false
                    resource.data?.let {
                            movie->
                        movieAdapter.differ.submitList(movie.results.toList())
                    }
                }
                is Resource.Error->{
                    binding.searchProgressBar.isVisible=false
                    isLoading=false
                    Snackbar.make(view,resource.message!!,Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() =with(binding){
        super.onStart()
        edSearch.doOnTextChanged { text, _, _, _ ->
            if(text!!.length>30)
                inputLayout.error="Max length is 30 symbols"
            else if(text.length<30)
                inputLayout.error=null
        }
        inputLayout.setEndIconOnClickListener {
            val query=edSearch.text.toString()
            when {
                query.length>30 -> Toast.makeText(context,"Max length is 30 symbols",Toast.LENGTH_SHORT).show()
                query.isEmpty() -> {
                    Toast.makeText(context,"Enter the name of the movie",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    edSearch.setText("")
                    oldQuery=query
                    viewModel.getSearchMovies(query,true)
                }
            }
        }
    }

    override fun onMovieClick(movie: Result) {
        val bundle=Bundle().apply {
            putSerializable("movie",movie)
            putBoolean("isSaved",false)
        }
        findNavController().navigate(R.id.action_searchMovieFragment_to_movieDetailFragment,bundle)
    }

}