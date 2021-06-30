package com.example.kinoship.ui.viewmodels

import androidx.lifecycle.*
import com.example.kinoship.ui.other.MovieRepository
import com.example.kinoship.models.Movie
import com.example.kinoship.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LatestMovieViewModel :ViewModel(){
    private val repository= MovieRepository.getRepo()
    private val _latestMovies=MutableLiveData<Resource<Movie>>()
    var moviePage=1
    var maxPages:Int?=null
    private var latestMovieResponse:Movie?=null
    val latestMovies:LiveData<Resource<Movie>> = _latestMovies


    fun getLatestMovies() = viewModelScope.launch(Dispatchers.IO){
        _latestMovies.postValue(Resource.Loading())
        val response=repository.getLatestMovies(moviePage)
        _latestMovies.postValue(handleLatestMoviesResponse(response))
    }

    private fun handleLatestMoviesResponse(response:Response<Movie>):Resource<Movie> {
        if(response.isSuccessful){
            response.body()?.let { movieBody->
                if(maxPages==null)
                    maxPages=movieBody.total_pages
                moviePage++
                if(latestMovieResponse==null){
                    latestMovieResponse=movieBody
                }
                else{
                    val oldResult=latestMovieResponse?.results
                    val newResults=movieBody.results
                    oldResult?.addAll(newResults)
                }
                return Resource.Success(latestMovieResponse?:movieBody)
            }
        }
        return Resource.Error("An unknown error:".plus(response.message()))
    }
}