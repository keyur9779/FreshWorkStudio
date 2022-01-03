package com.app.freshworkstudio.ui.viewDataModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.DataUtils.loading
import com.app.freshworkstudio.utils.network.NetworkChangeReceiver
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Base view model class to simply the code usage
 * */
abstract class BaseViewModel() :
    BindingViewModel(), NetworkChangeReceiver.NetworkChangeListener {

    init {
        FreshWorkApp.networkChangeReceiver.addNetowrkListener(this)
    }

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

    //fetch gif is marked as favourite or not
    protected val gifFavouriteStateFlow: MutableStateFlow<String> = MutableStateFlow("")

    private val gifFavFlow =
        gifFavouriteStateFlow.filter { return@filter gifFavID.isNotEmpty() }.flatMapLatest {
            getGifByID(gifFavID)
        }

    // get fav gif by id
    fun fetchGifFavMarket(id: String) {
        gifFavouriteStateFlow.tryEmit(id)
    }

    // mark fav gif by id
    fun insertItem(fav: GifFavourite) {
        viewModelScope.launch(Dispatchers.IO) {
            insertFav(fav)
        }
    }

    // observe the gif changes
    @get:Bindable
    val gifFav: List<GifFavourite> by gifFavFlow.asBindingProperty(
        viewModelScope, emptyList<GifFavourite>()
    )

    override fun onNetworkChange(isNetworkAvailable: Boolean) {

        Log.d("keyur", "on network change")
        if (isNetworkAvailable) {
            if (getGifItemList() is IOTaskResult.OnFailed<*>) {
                loadGifPage(
                    if (getCurrentPage() > loading) {
                        item
                    } else {
                        loading
                    }
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        FreshWorkApp.networkChangeReceiver.removeNetowrkListener(this)
    }

    abstract fun loadGifPage(data: Any)
    abstract fun getCurrentPage(): Int
    abstract fun getGifItemList(): IOTaskResult<Any>
    abstract suspend fun insertFav(gifFavourite: GifFavourite)
    abstract suspend fun getGifByID(id: String): Flow<ArrayList<GifFavourite>>


}