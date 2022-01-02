package com.app.freshworkstudio.ui.viewDataModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.app.freshworkstudio.data.repository.GiphyTrendingRepositoryImpl
import com.app.freshworkstudio.data.repository.repositoryService.GiphyTrendingRepository
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils.delay
import com.skydoves.bindables.asBindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * class is used to fetch and save data related to searched gif.
 *
 * Note: BindingViewModel - base view model class derived from skydoves-bindables library which makes easier to maintain the view model
 *
 * @param giphyTrendingRepository = repository class of model, used to fetch data from network or room based on call stack
 * */
@HiltViewModel
class SearchViewModel @Inject constructor(private val giphyTrendingRepository: GiphyTrendingRepository) :
    BaseViewModel() {

    internal val query = MutableStateFlow("")

    // search gif with delay in typing to avoid multiple query to server
    private val queryFlow =
        query.debounce(delay.toLong()).filter {
            return@filter it.isNotEmpty()
        }.flatMapLatest {
            isLoading = true
            giphyTrendingRepository.loadTrendingGif(lastPageNumber, searchQuery) {
                isLoading = false
            }
        }

    // bind searched Gif list
    @get:Bindable
    val gifSearched: IOTaskResult<Any> by queryFlow.asBindingProperty(
        viewModelScope,
        IOTaskResult.OnSuccess(Any())
    )

    // load gif in pages
    override fun loadGifPage(data: Any) {
        query.tryEmit(data.toString())
    }

    override fun getCurrentPage(): Int = lastPageNumber
    override fun getGifItemList(): IOTaskResult<Any> = gifSearched

    override suspend fun getGifByID(id: String) = giphyTrendingRepository.getGifByID(id)

    override suspend fun insertFav(gifFavourite: GifFavourite) {
        giphyTrendingRepository.insertFav(gifFavourite)
    }
}