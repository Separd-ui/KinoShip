package com.example.kinoship.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "movie_list")
data class Result(
    @PrimaryKey(autoGenerate = true)
    var id: Int?=null,
    val adult: Boolean,
    val backdrop_path: String?=null,
    val genre_ids: List<Int>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    var dateAdded:Long?=null
):Serializable