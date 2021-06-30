package com.example.kinoship.ui.other

import com.example.kinoship.db.MovieDao
import com.example.kinoship.models.Result
import com.example.kinoship.retrofit.RetrofitInstance
import java.lang.IllegalStateException

class MovieRepository(private val movieDao: MovieDao) {

    private val jsonHelper=RetrofitInstance.jsonHelper

    suspend fun getPopularMovies(page:Int) = jsonHelper.getPopularMovies(page = page)
    suspend fun getLatestMovies(page: Int) = jsonHelper.getLatestMovies(page = page)
    suspend fun getSearchMovie(query:String,page:Int) = jsonHelper.getSearchMovie(query = query,page = page)

    suspend fun addMovie(result: Result) = movieDao.addMovie(result)

    suspend fun deleteMovie(result: Result)= movieDao.deleteMovie(result)

    fun getMoviesSortedByRating() = movieDao.getMoviesSortedByRating()
    fun getMoviesSortedByTitle() = movieDao.getMoviesSortedByTitle()
    fun getMoviesSortedByDate() = movieDao.getMoviesSortedByDate()

    fun getSavedMovies() = movieDao.getAllMovies()

    companion object{
        private var INSTANCE: MovieRepository?=null

        fun initialize(movieDao: MovieDao){
            if(INSTANCE ==null){
                INSTANCE = MovieRepository(movieDao)
            }
        }

        fun getRepo() = INSTANCE ?:throw IllegalStateException("Repository must be initialized")
    }
}