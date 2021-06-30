package com.example.kinoship.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinoship.ui.other.MovieRepository
import com.example.kinoship.models.Movie
import com.example.kinoship.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchMovieViewModel :ViewModel() {
    private val movieRepository= MovieRepository.getRepo()
    private val _movieResponse=MutableLiveData<Resource<Movie>>()
    private var searchMovieResponse:Movie?=null
    val movieResponse:LiveData<Resource<Movie>> =_movieResponse
    var currentPage=1
    var maxPages:Int?=null

    fun getSearchMovies(query:String,isNewQuery:Boolean) = viewModelScope.launch(Dispatchers.IO){
        _movieResponse.postValue(Resource.Loading())
        if(isNewQuery){
            currentPage=1
            searchMovieResponse=null
        }
        val response=movieRepository.getSearchMovie(query,currentPage)
        _movieResponse.postValue(handleResponse(response))
    }
    private fun handleResponse(response:Response<Movie>):Resource<Movie>{
        if(response.isSuccessful){
            response.body()?.let { movie->
                currentPage++
                if(maxPages==null)
                    maxPages=movie.total_pages
                if(searchMovieResponse==null)
                    searchMovieResponse=movie
                else{
                    val oldMovies=searchMovieResponse?.results
                    val newMovies=movie.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(searchMovieResponse?:movie)
            }
        }
        return Resource.Error( message =response.message())
    }
}