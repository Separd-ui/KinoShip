package com.example.kinoship.db

import androidx.room.*
import com.example.kinoship.models.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_list")
    fun getAllMovies():Flow<List<Result>>

    @Query("SELECT * FROM movie_list ORDER BY dateAdded DESC")
    fun getMoviesSortedByDate():Flow<List<Result>>

    @Query("SELECT * FROM movie_list ORDER BY vote_average DESC")
    fun getMoviesSortedByRating():Flow<List<Result>>

    @Query("SELECT * FROM movie_list ORDER BY title DESC")
    fun getMoviesSortedByTitle():Flow<List<Result>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(result: Result)

    @Delete
    suspend fun deleteMovie(result: Result)
}