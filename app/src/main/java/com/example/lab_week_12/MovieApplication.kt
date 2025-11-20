package com.example.lab_week_12

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.example.lab_week_12.api.MovieService


class MovieApplication : Application() {
    lateinit var movieRepository: MovieRepository
    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val movieService = retrofit.create(
            MovieService::class.java
        )
        movieRepository = MovieRepository(movieService)
    }
}