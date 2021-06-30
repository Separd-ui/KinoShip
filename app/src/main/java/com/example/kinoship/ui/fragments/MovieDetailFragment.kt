package com.example.kinoship.ui.fragments

import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kinoship.R
import com.example.kinoship.databinding.FragmentMovieDetailBinding
import com.example.kinoship.models.Result
import com.example.kinoship.utils.Constans
import com.example.kinoship.utils.getGenre
import com.example.kinoship.utils.getRating
import com.example.kinoship.utils.startCustomAnim
import com.example.kinoship.ui.viewmodels.MovieDetailViewModel
import com.google.android.material.snackbar.Snackbar

class MovieDetailFragment : Fragment() {
    private lateinit var binding:FragmentMovieDetailBinding
    private val args:MovieDetailFragmentArgs by navArgs()
    private val viewModel:MovieDetailViewModel by viewModels()
    private lateinit var movie: Result
    private lateinit var animationExplode:Animation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_movie_detail, container, false)
        binding= FragmentMovieDetailBinding.bind(view)

        movie=args.movie

        animationExplode=AnimationUtils.loadAnimation(context,R.anim.explosion_anim).apply {
            interpolator=AccelerateDecelerateInterpolator()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailToolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            title=movie.title
        }
        binding.fbAdd.apply {
            isVisible= !args.isSaved
            setOnClickListener {
                movie.dateAdded=System.currentTimeMillis()
                viewModel.addToDb(movie)
                isVisible=false
                binding.explosion.isVisible=true
                binding.explosion.startCustomAnim(animationExplode){
                    binding.explosion.isVisible=false
                }
                Snackbar.make(requireView(),"Movie was successfully added",Snackbar.LENGTH_SHORT).show()
            }
        }

        setValues(movie)
    }
    private fun setValues(movie:Result)=with(binding){
        detAdult.visibility=if(movie.adult)
            View.VISIBLE
        else
            View.GONE
        detDate.text=movie.release_date
        Glide.with(this@MovieDetailFragment).load(Constans.URL_IMAGE+movie.poster_path).into(detImage)
        detRatingBar.rating= getRating(movie.vote_average)
        detDesc.text=if(movie.overview.length>270)
            movie.overview.substring(0,270)+"..."
        else
            movie.overview
        detRatingNum.text=movie.vote_average.toString()
        detTitle.text=movie.title
        detGenre.text= getGenre(movie.genre_ids)
    }



}