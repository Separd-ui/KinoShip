package com.example.kinoship.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinoship.ui.other.MovieRepository
import com.example.kinoship.models.Result
import com.example.kinoship.utils.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedMovieViewModel :ViewModel(){
    private val movieRepository= MovieRepository.getRepo()

    private val savedMovies = movieRepository.getSavedMovies().asLiveData()
    private val sortedByDate = movieRepository.getMoviesSortedByDate().asLiveData()
    private val sortedByTitle = movieRepository.getMoviesSortedByTitle().asLiveData()
    private val sortedByRating = movieRepository.getMoviesSortedByRating().asLiveData()

    fun deleteMovie(movie:Result) = viewModelScope.launch(Dispatchers.IO){
        movieRepository.deleteMovie(movie)
    }

    fun addMovie(movie: Result) = viewModelScope.launch(Dispatchers.IO){
        movieRepository.addMovie(movie)
    }

    val movies=MediatorLiveData<List<Result>>()

    var sortType=SortType.DEFAULT

    init {
        movies.addSource(sortedByDate){
                result->
            if(SortType.DATE==sortType){
                result.let {

                    movies.postValue(it)
                }
            }
        }
        movies.addSource(sortedByRating){
                result->
            if(SortType.RATING==sortType){
                result.let {

                    movies.postValue(it)
                }
            }
        }
        movies.addSource(sortedByTitle){
                result->
            if(SortType.TITLE==sortType){
                result.let {

                    movies.postValue(it)
                }
            }
        }
        movies.addSource(savedMovies){
                result->
            if(SortType.DEFAULT==sortType){
                result.let {
                    movies.postValue(it)
                }
            }
        }
    }

    fun sortMovies(sortType: SortType) = when(sortType){
        SortType.DATE -> sortedByDate.value?.let { movies.postValue(it) }
        SortType.RATING -> sortedByRating.value?.let { movies.postValue(it) }
        SortType.TITLE -> sortedByTitle.value?.let { movies.postValue(it) }
        SortType.DEFAULT -> savedMovies.value?.let { movies.postValue(it) }
    }.also {
        this.sortType=sortType
    }


}