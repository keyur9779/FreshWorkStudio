package com.app.freshworkstudio.ui.viewDataModels

import androidx.databinding.Bindable
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.freshworkstudio.data.repository.GiFavouriteRepository
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils
import com.skydoves.bindables.BindingViewModel
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
 * @param giFavouriteRepository = repository class of model, used to fetch data from network or room based on call stack
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
    private val localGifListFlow = localGif.flatMapLatest {
        isLoading = true
        giFavouriteRepository.loadFavGif {
            isLoading = false
        }
    }

    // we used to to make sure we fetch latest changes from room db and notify when data is changed
    val localGifList = localGifListFlow.asLiveData()

    //delete fav to database
    fun deleteItem(fav: GifFavourite) {
        viewModelScope.launch(Dispatchers.IO) {
            giFavouriteRepository.delete(fav)
        }

    }
}