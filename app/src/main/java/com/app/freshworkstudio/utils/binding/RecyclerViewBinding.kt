package com.app.freshworkstudio.utils.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.R
import com.app.freshworkstudio.model.GiphyResponseModel
import com.app.freshworkstudio.model.IOTaskResult
import com.app.freshworkstudio.ui.adapter.BaseGifAdapter
import com.app.freshworkstudio.ui.viewDataModels.BaseViewModel
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
    fun bindAdapterGifList(view: RecyclerView, gif: IOTaskResult<*>, viewModel: BaseViewModel) {
        val adapter = view.adapter as? BaseGifAdapter
        gif.whatIfNotNull {

            val ioTask = gif as IOTaskResult<Any>

            if (ioTask is IOTaskResult.OnSuccess<*>) {
                if (ioTask.data is GiphyResponseModel) {
                    val modelData = ioTask.data

                    viewModel.possibleTotalPage = modelData.pagination!!.total_count / pageCount

                    if (modelData.data.isEmpty()) {
                        adapter?.showErrorPage(view.context.getString(R.string.no_result))
                    } else {
                        if (viewModel.lastPageNumber == item) {
                            adapter?.clear()
                        }
                        adapter?.addGif(modelData.data)
                    }
                } /*else {
                    if (viewModel.searchQuery.isEmpty())
                        adapter?.showErrorPage("")

                }*/
                return@whatIfNotNull
            }

            if (ioTask is IOTaskResult.OnFailed<*>) {
                if (ioTask.message is String) {
                    val modelData = ioTask.message
                    adapter?.showErrorPage("$modelData")
                }
            }
        } /*?: kotlin.run {
            if (viewModel.searchQuery.isEmpty())
                adapter?.showErrorPage("")
        }*/
    }

    @JvmStatic
    @BindingAdapter("paginationGifList")
    fun paginationGifList(view: RecyclerView, viewModel: BaseViewModel) {
        view.addOnScrollListener(object :
            PaginationScrollListener(view.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {

                val adapter = view.adapter as? BaseGifAdapter
                // apply load more logic over here
                val itemCount = adapter?.itemCount!!
                if (itemCount > loading) {

                    val ioTask = viewModel.getGifItemList()
                    if (ioTask is IOTaskResult.OnSuccess<*>) {
                        if (ioTask.data is GiphyResponseModel) {
                            viewModel.isLoading = true
                            if (viewModel.lastPageNumber < viewModel.possibleTotalPage) {
                                viewModel.lastPageNumber += loading
                                view.post {
                                    adapter.showErrorPage("")
                                }
                                viewModel.loadGifPage(viewModel.getCurrentPage().plus(loading))
                            } else {
                                viewModel.isLastPage = true
                            }
                        }
                    } else {
                        if (FreshWorkApp.isInternetAvailable()) {
                            adapter.showErrorPage("")
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

    }
}