package com.app.freshworkstudio.utils.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
                    if (viewModel.searchQuery.isNotEmpty() && (viewModel.lastPageNumber == item /*|| viewModel.lastPageNumber == loading*/)) {
                        adapter?.clear()
                    }
                    // make clear when search is removed
                    if (viewModel.searchQuery.isEmpty() && (viewModel.lastPageNumber == item /*|| viewModel.lastPageNumber == loading*/)) {
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


        view.addOnScrollListener(object :
            PaginationScrollListener(view.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {

                // apply load more logic over here
                val itemCount = view.adapter?.itemCount!!
                if (itemCount > loading) {

                    val ioTask = viewModel.gifList
                    if (ioTask is IOTaskResult.OnSuccess<*>) {
                        if (ioTask.data is GiphyResponseModel) {
                            if (viewModel.lastPageNumber < viewModel.possibleTotalPage) {
                                viewModel.lastPageNumber += loading
                                viewModel.loadGifPage(viewModel.lastPageNumber)
                            } else {
                                viewModel.isLastPage = true
                            }
                        }
                    } else {
                        if (FreshWorkApp.isInternetAvailable()) {
                            viewModel.loadGifPage(viewModel.lastPageNumber.minus(loading))
                        }
                    }
                }
            }

            override fun getTotalPageCount(): Int {
                return viewModel.possibleTotalPage
            }

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading
            }
        })

        view.addItemDecoration(
            DividerItemDecoration(
                view.context,
                DividerItemDecoration.VERTICAL
            )
        )


        /*//TODO replace this pagination with our custom one
        RecyclerViewPaginator(
            recyclerView = view,
            isLoading = {
                viewModel.isLoading
            },
            loadMore = {
                val itemCount = view.adapter?.itemCount!!
                if (itemCount > loading) {

                    val ioTask = viewModel.gifList
                    if (ioTask is IOTaskResult.OnSuccess<*>) {
                        if (ioTask.data is GiphyResponseModel) {
                            //val modelData = ioTask.data as GiphyResponseModel
                            if (viewModel.lastPageNumber < viewModel.possibleTotalPage) {
                                viewModel.lastPageNumber += loading
                                viewModel.loadGifPage(viewModel.lastPageNumber)
                            } else {
                                viewModel.isLastPage = true
                            }
                        }
                    } else {
                        if (FreshWorkApp.isInternetAvailable()) {
                            viewModel.loadGifPage(viewModel.lastPageNumber.minus(loading))
                        }
                    }
                }
            },
            onLast = { viewModel.isLastPage }
        ).run {
            threshold = 1
            currentPage = 1
        }*/
    }


}