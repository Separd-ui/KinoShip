package com.example.kinoship.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinoship.R
import com.example.kinoship.adapters.MovieAdapter
import com.example.kinoship.databinding.FragmentSavedMovieBinding
import com.example.kinoship.models.Result
import com.example.kinoship.utils.SortType
import com.example.kinoship.ui.viewmodels.SavedMovieViewModel
import com.example.kinoship.utils.CustomSwipe
import com.google.android.material.snackbar.Snackbar

class SavedMovieFragment :Fragment(),MovieAdapter.OnMovieListener,CustomSwipe.OnSwiped {

    private lateinit var binding:FragmentSavedMovieBinding
    private val viewModel:SavedMovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_saved_movie, container, false)
        binding= FragmentSavedMovieBinding.bind(view)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recViewSaved.apply {
            layoutManager= LinearLayoutManager(context)
            movieAdapter=MovieAdapter(context,this@SavedMovieFragment)
            adapter=movieAdapter
        }.also {
            ItemTouchHelper(CustomSwipe(requireContext(),this)).attachToRecyclerView(it)
        }

        when(viewModel.sortType){
            SortType.TITLE->binding.chipName.isChecked=true
            SortType.RATING->binding.chipRating.isChecked=true
            SortType.DATE->binding.chipDate.isChecked=true
            SortType.DEFAULT->Unit
        }

        viewModel.movies.observe(viewLifecycleOwner){
                movies->
            movieAdapter.differ.submitList(movies)
        }
    }

    override fun onStart()=with(binding) {
        super.onStart()
        chipDate.setOnClickListener {
            viewModel.sortMovies(SortType.DATE)
        }
        chipName.setOnClickListener {
            viewModel.sortMovies(SortType.TITLE)
        }
        chipRating.setOnClickListener {
            viewModel.sortMovies(SortType.RATING)
        }
    }

    override fun onMovieClick(movie: Result) {
        val bundle=Bundle().apply {
            putSerializable("movie",movie)
            putBoolean("isSaved",true)
        }
        findNavController().navigate(R.id.action_savedMovieFragment_to_movieDetailFragment,bundle)
    }

    override fun swipedToDelete(pos: Int) {
        val movie= movieAdapter.differ.currentList[pos]
        viewModel.deleteMovie(movie)
        Snackbar.make(requireView(),"Successfully deleted", Snackbar.LENGTH_LONG).apply {
            setAction("UNDO"){
                viewModel.addMovie(movie)
            }
            show()
        }
    }


}