package com.example.kinoship.retrofit

import com.example.kinoship.models.Movie
import com.example.kinoship.utils.Constans
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonHelper {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey:String=Constans.API_KEY,
        @Query("page") page:Int
    ):Response<Movie>

    @GET("movie/now_playing")
    suspend fun getLatestMovies(
        @Query("api_key") apiKey:String=Constans.API_KEY,
        @Query("page") page: Int
    ):Response<Movie>

    @GET("search/movie")
    suspend fun getSearchMovie(
        @Query("api_key") apiKey:String=Constans.API_KEY,
        @Query("query") query:String,
        @Query("include_adult") adult:Boolean=true,
        @Query("page") page:Int):Response<Movie>



}