package com.example.kinoship.ui.fragments

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinoship.R
import com.example.kinoship.adapters.MovieAdapter
import com.example.kinoship.databinding.FragmentSavedMovieBinding
import com.example.kinoship.models.Result
import com.example.kinoship.utils.SortType
import com.example.kinoship.ui.viewmodels.SavedMovieViewModel
import com.google.android.material.snackbar.Snackbar

class SavedMovieFragment : Fragment(),MovieAdapter.OnMovieListener {
    private lateinit var binding:FragmentSavedMovieBinding
    private val viewModel:SavedMovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    private val itemTouchHelper = object:ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
        ItemTouchHelper.LEFT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position=viewHolder.adapterPosition
            val movie= movieAdapter.differ.currentList[position]
            viewModel.deleteMovie(movie)
            Snackbar.make(requireView(),"Successfully deleted",Snackbar.LENGTH_LONG).apply {
                setAction("UNDO"){
                    viewModel.addMovie(movie)
                }
                show()
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )

            val background=ColorDrawable(Color.RED)
            val icon=ContextCompat.getDrawable(requireContext(),R.drawable.ic_delete_v2)
            val itemView = viewHolder.itemView
            val cornerOffset = 20
            val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
            val iconTop= itemView.top + (itemView.height - icon.intrinsicHeight) / 2
            val iconBot = iconTop + icon.intrinsicHeight


            if (dX < 0) {
                val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBot)
                background.setBounds(
                    itemView.right + dX.toInt() - cornerOffset,
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            background.draw(c)
            icon.draw(c)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_saved_movie, container, false)
        binding= FragmentSavedMovieBinding.bind(view)


        binding.recViewSaved.apply {
            layoutManager=LinearLayoutManager(context)
            movieAdapter=MovieAdapter(context,this@SavedMovieFragment)
            adapter=movieAdapter
        }


        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recViewSaved)

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

        return view
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


}