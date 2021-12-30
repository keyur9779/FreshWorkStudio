package com.app.freshworkstudio.ui.viewDataModels

import com.app.freshworkstudio.utils.DataUtils
import com.skydoves.bindables.BindingViewModel
import kotlinx.coroutines.flow.*

open class BaseViewModel(T: Any) : BindingViewModel() {


    /*val data by lazy {
        val mutableStateFlow = if (T is String) {
            MutableStateFlow("")
        } else {
            MutableStateFlow(DataUtils.item)

        }
        mutableStateFlow
    }

    // search gif with delay in typing to avoid multiple query to server
    private val queryFlow by lazy {

        data.debounce(DataUtils.delay.toLong()).flatMapLatest {
            isLoading = true
            giphyTrendingRepository.loadTrendingGif(lastPageNumber, searchQuery) {
                isLoading = false
            }
        }
    }*/

}