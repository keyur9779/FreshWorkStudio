
package com.app.freshworkstudio.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ItemGifDetailsBinding
import com.app.freshworkstudio.databinding.ItemLoadingBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.ui.adapter.viewHolders.GifListViewHolder
import com.app.freshworkstudio.ui.adapter.viewHolders.LoadingVH
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.DataUtils.loading
import com.skydoves.bindables.binding


/**
 * gif list adapter for searched and trending gifs
 *
 * @param onAdapterPositionClicked = higher order function return clicked item model to save it in local db
 * @param onRetry = retry download same page on error
 * */
class GifSearchListAdapter(
    private val onAdapterPositionClicked: (GifData) -> Unit,
    private val onRetry: (Int) -> Unit
) :
    BaseGifAdapter<RecyclerView.ViewHolder>() {

    val items: MutableList<GifData> = arrayListOf()
    private var retryPageLoad: Boolean = false
    private var errorMsg: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == item) {
            val binding = parent.binding<ItemGifDetailsBinding>(R.layout.item_gif_details)
            GifListViewHolder(binding, onAdapterPositionClicked).apply {
                binding.root.setOnClickListener() {
                    val item = items[absoluteAdapterPosition]
                    onAdapterPositionClicked(item)
                }
            }
        } else {
            val binding = parent.binding<ItemLoadingBinding>(R.layout.item_loading)
            LoadingVH(binding, onRetry)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == item) {
            with((holder as GifListViewHolder).binding as ItemGifDetailsBinding) {
                gifData = items[position]
            }
        } else {
            (holder as LoadingVH).bind(errorMsg)
        }
    }

    private fun removeLoadingFooter() {
        notifyItemRemoved(itemCount - item)
    }
    @Suppress("UNCHECKED_CAST")
    override fun addGif(dList: Any) {
        if (retryPageLoad) {
            retryPageLoad = false
            errorMsg = ""
            removeLoadingFooter()
        }
        val previousItemSize = items.size
        items.addAll(dList as List<GifData>)
        val newSize = items.size
        /*if (newSize == pageCount) {
            notifyDataSetChanged()
        } else {*/

        notifyItemRangeInserted(previousItemSize, newSize)
        //}
    }


    override fun getItemViewType(position: Int): Int {
        val itemSize = items.size
        val size = if (itemSize >= loading) itemSize - loading else item
        return if (position == size && retryPageLoad) {
            loading
        } else {
            item
        }
    }


    override fun getItemCount(): Int {
        val itemSize = items.size
        return if (items.size > item) {
            itemSize
        } else {
            if (retryPageLoad) {
                loading
            } else {
                item
            }
        }
    }

    override fun showErrorPage(message: String) {
        retryPageLoad = true
        val itemSize = items.size
        this.errorMsg = message
        val size = if (itemSize >= loading) itemSize - loading else item
        notifyItemChanged(size)
    }

    override fun clear() {

        if (items.isEmpty() && !retryPageLoad) {
            return
        }
        retryPageLoad = false
        items.clear()
        notifyDataSetChanged()
    }

}
