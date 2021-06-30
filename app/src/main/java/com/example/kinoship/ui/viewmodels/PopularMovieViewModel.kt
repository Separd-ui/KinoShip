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

class PopularMovieViewModel : ViewModel(){
    private val movieRepository= MovieRepository.getRepo()
    private val _popularMovie=MutableLiveData<Resource<Movie>>()
    val popularMovie:LiveData<Resource<Movie>> =_popularMovie
    private var popularMovieResponse:Movie?=null
    var currentPage=1
    var maxPages:Int?=null

    fun getPopularMovies()= viewModelScope.launch(Dispatchers.IO){
        _popularMovie.postValue(Resource.Loading())
        val response=movieRepository.getPopularMovies(currentPage)
        _popularMovie.postValue(handleResponse(response))
    }

    private fun handleResponse(response: Response<Movie>): Resource<Movie> {
        if(response.isSuccessful)
        {
            response.body()?.let{
                movieResponse->
                currentPage++
                if(maxPages==null)
                    maxPages=movieResponse.total_pages
                if(popularMovieResponse==null)
                    popularMovieResponse=movieResponse
                else{
                    val oldMovie=popularMovieResponse?.results
                    val newMovie=movieResponse.results
                    oldMovie?.addAll(newMovie)
                }
                return Resource.Success(popularMovieResponse?:movieResponse)
            }
        }

        return Resource.Error(message = response.message())
    }
}