package com.app.freshworkstudio.ui.viewDataModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.app.freshworkstudio.data.repository.GiphyTrendingRepository
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils
import com.app.freshworkstudio.utils.DataUtils.delay
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    BindingViewModel() {

    // bindable property to save possible total page of current GIF category
    @get:Bindable
    var gifFavID: String by bindingProperty("")


    // bindable property to save possible total page of current GIF category
    @get:Bindable
    var possibleTotalPage: Int by bindingProperty(DataUtils.item)

    // bindable property to store query text
    @get:Bindable
    var searchQuery: String by bindingProperty(DataUtils.condition)

    // bindable property to get info last fetched page from network
    @get:Bindable
    var lastPageNumber: Int by bindingProperty(DataUtils.item)

    // bindable property for loading while fetching data
    @get:Bindable
    var isLoading: Boolean by bindingProperty(false)

    // bindable property to stop endless gif adapter
    @get:Bindable
    var isLastPage: Boolean by bindingProperty(false)

    // gif mutable state flow to fetch data in pagination
    private val gifPageStateFlow: MutableStateFlow<Int> = MutableStateFlow(DataUtils.item)
    private val gifListFlow =
        gifPageStateFlow.filter {
            return@filter searchQuery.isEmpty()
        }.flatMapLatest {
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

    // load gif in pagination
    fun loadGifPage(page: Int) = gifPageStateFlow.tryEmit(page)

    //insert fav to database

    fun insertItem(fav: GifFavourite) {

        viewModelScope.launch(Dispatchers.IO) {
            giphyTrendingRepository.insertFav(fav)
        }
    }

    //fetch gif is marked as favourite or not

    private val gifFavouriteStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val gifFavFlow = gifFavouriteStateFlow.flatMapLatest {
        giphyTrendingRepository.getGifByID(gifFavID)
    }

    @get:Bindable
    val gifFav: List<GifFavourite> by gifFavFlow.asBindingProperty(
        viewModelScope, emptyList<GifFavourite>()
    )

    fun fetchGifFavMarket(id: String) = gifFavouriteStateFlow.tryEmit(id)

    fun getCurrentPage(): Int = gifPageStateFlow.value


    val query = MutableStateFlow("")

    // search gif with delay in typing to avoid multiple query to server
    private val queryFlow =
        query.debounce(delay.toLong()).filter {
            return@filter it.isNotEmpty()
        }.distinctUntilChanged().flatMapLatest {
            isLoading = true
            giphyTrendingRepository.loadTrendingGif(lastPageNumber, searchQuery) {
                isLoading = false
            }
        }

    // download searched gif pages in pagination
    fun loadSearchPages(id: String) = query.tryEmit(id)

    // bind searched Gif list
    @get:Bindable
    val gifSearched: IOTaskResult<Any> by queryFlow.asBindingProperty(
        viewModelScope,
        IOTaskResult.OnSuccess(Any())
    )

}