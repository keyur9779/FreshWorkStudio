package com.app.freshworkstudio.ui.viewDataModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.app.freshworkstudio.data.repository.GiphyTrendingRepository
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.utils.DataUtils
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(private val giphyTrendingRepository: GiphyTrendingRepository) :
    BindingViewModel() {


    @get:Bindable
    var possibleTotalPage: Int by bindingProperty(DataUtils.item)

    @get:Bindable
    var searchQuery: String by bindingProperty("")

    @get:Bindable
    var lastPageNumber: Int by bindingProperty(DataUtils.item)

    @get:Bindable
    var isLoading: Boolean by bindingProperty(false)
        private set

    @get:Bindable
    var isLastPage: Boolean by bindingProperty(false)

    private val gifPageStateFlow: MutableStateFlow<Int> = MutableStateFlow(DataUtils.item)
    private val gifListFlow = gifPageStateFlow.flatMapLatest {
        isLoading = true
        giphyTrendingRepository.loadTrendingGif(lastPageNumber, searchQuery) {
            isLoading = false
        }
    }

    @get:Bindable
    val gifList: IOTaskResult<Any> by gifListFlow.asBindingProperty(
        viewModelScope,
        IOTaskResult.OnSuccess(Any())
    )


    fun loadGifPage(page: Int) = gifPageStateFlow.tryEmit(page)

    /*// this is bug replace string with page
    private val submitEvent: MutableStateFlow<Int> = MutableStateFlow(DataUtils.item)

    private val searchGIfListFlow = submitEvent.flatMapLatest {
        isLoading = true
        giphyTrendingRepository.searchGif(searchQuery, it) {
            isLoading = false
        }
    }

    @get:Bindable
    val searchedGifList: IOTaskResult<Any> by searchGIfListFlow.asBindingProperty(
        viewModelScope,
        IOTaskResult.OnSuccess(Any())
    )
    fun searchedGifPage() = submitEvent.tryEmit(lastPageNumber)*/
}