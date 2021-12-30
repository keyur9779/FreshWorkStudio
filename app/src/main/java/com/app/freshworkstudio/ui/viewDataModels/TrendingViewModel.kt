package com.app.freshworkstudio.ui.viewDataModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.app.freshworkstudio.data.repository.GiphyTrendingRepository
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils
import com.skydoves.bindables.asBindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * class is used to fetch and save data related to trending and searched gif.
 *
 * Note: BindingViewModel - base view model class derived from skydoves-bindables library which makes easier to maintain the view model
 *
 * @param giphyTrendingRepository = repository class of model, used to fetch data from network or room based on call stack
 * */
@HiltViewModel
class TrendingViewModel @Inject constructor(private val giphyTrendingRepository: GiphyTrendingRepository) :
    BaseViewModel() {


    // gif mutable state flow to fetch data in pagination
    private val gifPageStateFlow: MutableStateFlow<Int> = MutableStateFlow(DataUtils.item)
    private val gifListFlow =
        gifPageStateFlow.flatMapLatest {
            isLoading = true
            giphyTrendingRepository.loadTrendingGif(lastPageNumber, searchQuery) {
                isLoading = false
            }
        }

    // bindable list property to bind fetched list to recycler view using binding-adapter property
    @get:Bindable
    val gifList: IOTaskResult<Any> by gifListFlow.asBindingProperty(
        viewModelScope,
        IOTaskResult.OnSuccess(Any())
    )

    override fun getGifItemList() = gifList


    override fun loadGifPage(data: Any) {
        gifPageStateFlow.tryEmit(data as Int)
    }

    override fun getCurrentPage(): Int = gifPageStateFlow.value

    override suspend fun getGifByID(id: String) = giphyTrendingRepository.getGifByID(id)

    override suspend fun insertFav(gifFavourite: GifFavourite) {
        giphyTrendingRepository.insertFav(gifFavourite)
    }


}