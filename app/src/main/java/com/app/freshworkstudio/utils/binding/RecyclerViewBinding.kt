package com.app.freshworkstudio.utils.binding

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.model.GiphyResponseModel
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.ui.adapter.GifListAdapter
import com.app.freshworkstudio.ui.viewDataModels.TrendingViewModel
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import com.skydoves.whatif.whatIfNotNull

object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(view: RecyclerView, baseAdapter: GifListAdapter) {

        view.adapter = baseAdapter
    }

    @JvmStatic
    @BindingAdapter("adapterGifList")
    fun bindAdapterMovieList(view: RecyclerView, gif: IOTaskResult<*>) {
        Log.d("keyur", "notofied yessss binding")

        val adapter = (view.adapter as? GifListAdapter)


        gif.whatIfNotNull {

            val ioTask = gif as IOTaskResult<Any>

            if (ioTask is IOTaskResult.OnSuccess<*>) {
                if (ioTask.data is GiphyResponseModel) {
                    val modelData = ioTask.data as GiphyResponseModel
                    Log.d("keyur", "notofied while binding")
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
    @BindingAdapter("paginationMovieList")
    fun paginationMovieList(view: RecyclerView, viewModel: TrendingViewModel) {

        Log.d("keyur", "paginationMovieList")
        RecyclerViewPaginator(
            recyclerView = view,
            isLoading = {

                viewModel.isLoading
            },
            loadMore = {
                Log.d("keyur", "load more")
                //val pageCount = viewModel.gifList.pagination?.total_count
                viewModel.postMoviePage(it)
            },
            onLast = { false }
        ).run {
            threshold = 4
            currentPage = 1
        }
    }


}