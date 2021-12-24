package com.app.freshworkstudio.ui.viewDataModels

import androidx.lifecycle.*
import com.app.freshworkstudio.data.repository.GiphyTrendingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(val giphyTrendingRepository: GiphyTrendingRepository) :
    ViewModel() {

    private var _topMoviesFirstPageResponse = MutableLiveData<Any>()
    private var _topMoviesNextPageResponse = MutableLiveData<Any>()

    fun requestTrendingGif(page: Int) {
        viewModelScope.launch {

            giphyTrendingRepository.loadTrendingGif(page).collectLatest {

            }
        }
    }

    fun requestFirstNextPageMovies(page: Int) {
        retrofitRepository!!.loadPage(_topMoviesNextPageResponse, page)
    }

    val topMoviesFirstPageResponse: LiveData<Any>
        get() = _topMoviesFirstPageResponse
    val topMoviesNextPageResponse: LiveData<Any>
        get() = _topMoviesNextPageResponse
}