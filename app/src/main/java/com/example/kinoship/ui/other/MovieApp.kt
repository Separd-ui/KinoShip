package com.example.kinoship.ui.other

import android.app.Application
import com.example.kinoship.db.MovieDatabase

class MovieApp:Application() {
    override fun onCreate() {
        super.onCreate()
        val database by lazy {
            MovieDatabase.getDatabase(this)
        }
        MovieRepository.initialize(database.getDao())
    }
}