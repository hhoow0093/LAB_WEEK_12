package com.example.lab_week_12

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class MovieViewModel(private val movieRepository: MovieRepository) :
    ViewModel() {
    private val _popularMovies = MutableStateFlow(
        emptyList<Movie>()
    )
    val popularMovies = _popularMovies
        .map { movies ->
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

            movies
                .filter { it.releaseDate.startsWith(currentYear) }
                .sortedByDescending { it.popularity }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error
    init {
        fetchPopularMovies()
    }
    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies().catch {
                _error.value = "An exception occurred: ${it.message}"
            }.collect {
                        _popularMovies.value = it
            }
        }
    }

}