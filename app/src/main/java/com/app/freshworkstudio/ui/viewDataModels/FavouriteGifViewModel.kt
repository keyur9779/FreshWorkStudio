package com.app.freshworkstudio.ui.viewDataModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.app.freshworkstudio.data.repository.GiFavouriteRepository
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * class is used to fetch and save data related to trending and searched gif.
 *
 * @param giphyTrendingRepository = repository class of model, used to fetch data from network or room based on call stack
 * */
@HiltViewModel
class FavouriteGifViewModel @Inject constructor(private val giFavouriteRepository: GiFavouriteRepository) :
    BindingViewModel() {


    // bindable property for loading while fetching data
    @get:Bindable
    var isLoading: Boolean by bindingProperty(false)
        private set

    // gif mutable state flow to fetch data in pagination
    private val localGif: MutableStateFlow<Int> = MutableStateFlow(DataUtils.item)
    private val gifListFlow = localGif.flatMapLatest {
        isLoading = true
        giFavouriteRepository.loadFavGif {
            isLoading = false
        }
    }

    // bindable list property to bind fetched list to recycler view using binding-adapter property
    @get:Bindable
    val gifList: List<GifFavourite> by gifListFlow.asBindingProperty(viewModelScope, emptyList())

    //delete fav to database
    fun deleteItem(fav: GifFavourite) {
        viewModelScope.launch(Dispatchers.IO) {
            giFavouriteRepository.delete(fav)
        }
    }
}