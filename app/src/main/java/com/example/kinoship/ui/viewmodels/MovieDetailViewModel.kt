package com.example.kinoship.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinoship.ui.other.MovieRepository
import com.example.kinoship.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel :ViewModel(){
    private val movieRepository= MovieRepository.getRepo()


    fun addToDb(movie:Result) = viewModelScope.launch(Dispatchers.IO){
        movieRepository.addMovie(movie)
    }
}