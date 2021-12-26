package com.app.freshworkstudio.utils.binding

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.model.GiphyResponseModel
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.ui.adapter.GifListAdapter
import com.app.freshworkstudio.ui.viewDataModels.TrendingViewModel
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.DataUtils.loading
import com.app.freshworkstudio.utils.DataUtils.pageCount
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import com.skydoves.whatif.whatIfNotNull

object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(view: RecyclerView, baseAdapter: BaseAdapter) {

        view.adapter = baseAdapter
    }


    @JvmStatic
    @BindingAdapter("adapterGifList", "adapterGifModel")
    fun bindAdapterGifList(view: RecyclerView, gif: IOTaskResult<*>, viewModel: TrendingViewModel) {

        val adapter = (view.adapter as? GifListAdapter)
        gif.whatIfNotNull {

            val ioTask = gif as IOTaskResult<Any>

            if (ioTask is IOTaskResult.OnSuccess<*>) {
                if (ioTask.data is GiphyResponseModel) {
                    val modelData = ioTask.data

                    viewModel.possibleTotalPage = modelData.pagination!!.total_count / pageCount

                    // make clear when search is done
                    if (viewModel.searchQuery.isNotEmpty() && (viewModel.lastPageNumber == item || viewModel.lastPageNumber == loading)) {
                        adapter?.clear()
                    }
                    // make clear when search is removed
                    if (viewModel.searchQuery.isEmpty() && (viewModel.lastPageNumber == item || viewModel.lastPageNumber == loading)) {
                        adapter?.clear()
                    }
                    adapter?.addGif(modelData.data)
                }
                return@whatIfNotNull
            }

            if (ioTask is IOTaskResult.OnFailed<*>) {
                if (ioTask.message is String) {
                    val modelData = ioTask.message
                    adapter?.showErrorPage("Retry $modelData")
                }
            }

        } ?: kotlin.run {
            adapter?.showErrorPage("Something went Wrong")
        }
    }

    @JvmStatic
    @BindingAdapter("paginationGifList")
    fun paginationGifList(view: RecyclerView, viewModel: TrendingViewModel) {

        Log.d("keyur", "paginationMovieList")
        RecyclerViewPaginator(
            recyclerView = view,
            isLoading = {
                viewModel.isLoading
            },
            loadMore = {
                Log.d("keyur", "load more")
                val itemCount = view.adapter?.itemCount!!
                if (itemCount > loading) {

                    val ioTask = viewModel.gifList
                    if (ioTask is IOTaskResult.OnSuccess<*>) {
                        if (ioTask.data is GiphyResponseModel) {
                            //val modelData = ioTask.data as GiphyResponseModel
                            Log.d("keyur", "new page available now")
                            if (viewModel.lastPageNumber < viewModel.possibleTotalPage) {
                                viewModel.lastPageNumber += loading
                                viewModel.loadGifPage(viewModel.lastPageNumber)
                            } else {
                                viewModel.isLastPage = true
                            }
                        }
                    } else {
                        if (FreshWorkApp.isInternetAvailable()) {
                            Log.d("keyur", "try loading previous page")
                            viewModel.loadGifPage(viewModel.lastPageNumber.minus(loading))
                        }
                    }
                }
            },
            onLast = { viewModel.isLastPage }
        ).run {
            threshold = 4
            currentPage = 0
        }
    }


}