/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.freshworkstudio.ui.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ItemGifBinding
import com.app.freshworkstudio.databinding.ItemLoadingBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.DataUtils.loading
import com.app.freshworkstudio.utils.DataUtils.pageCount
import com.skydoves.bindables.binding


// add lamba function as callback
class GifListAdapter(
    private val onAdapterPositionClicked: (Int) -> Unit,
    private val onRetry: (Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<GifData> = arrayListOf()
    private var retryPageLoad: Boolean = false
    private var errorMsg: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == item) {
            val binding = parent.binding<ItemGifBinding>(R.layout.item_gif)
            return GifListViewHolder(binding, onAdapterPositionClicked)
        } else {
            val binding = parent.binding<ItemLoadingBinding>(R.layout.item_loading)
            return LoadingVH(errorMsg!!, binding, onRetry)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == item) {
            with((holder as GifListViewHolder).binding) {
                media = items[position].images.fixed_width
            }
        }
    }

    private fun removeLoadingFooter() {

        notifyItemRemoved(itemCount - item)

    }

    fun addGif(gifs: List<GifData>) {
        Log.d("keyur", "list came ${gifs.size}")
        if (retryPageLoad) {
            retryPageLoad = false
            removeLoadingFooter()
        }
        val previousItemSize = items.size
        items.addAll(gifs)
        val newSize = items.size
        if (newSize == pageCount) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(previousItemSize, newSize)
        }
    }

    /*fun addSearchedGif(data: List<GifData>) {

        Log.d("keyur", "list came")
        if (retryPageLoad) {
            retryPageLoad = false
            removeLoadingFooter()
        }

        val previousItemSize = items.size
        items.addAll(data)
        notifyItemRangeInserted(previousItemSize, items.size)

    }*/

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

    fun showErrorPage(s: String) {

        Log.d("keyur", "on error $s")
        retryPageLoad = true
        val itemSize = items.size
        this.errorMsg = s
        val size = if (itemSize >= loading) itemSize - loading else item
        notifyItemChanged(size)

    }


    fun clear() {
        Log.d("keyur", "itemed cleared now")
        items.clear()
        notifyDataSetChanged()


    }

    class GifListViewHolder(
        val binding: ItemGifBinding,
        private val onAdapterPositionClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.square.setOnClickListener() {
                onAdapterPositionClicked(adapterPosition)
            }
        }
    }

    class LoadingVH(
        errorMsg: String,
        binding: ItemLoadingBinding,
        private val onRetry: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.loadmoreErrorlayout.setOnClickListener {
                onRetry(adapterPosition)
            }
            binding.loadmoreRetry.setOnClickListener {
                onRetry(adapterPosition)
            }
            binding.loadmoreErrortxt.text = "$errorMsg"
        }
    }
}
