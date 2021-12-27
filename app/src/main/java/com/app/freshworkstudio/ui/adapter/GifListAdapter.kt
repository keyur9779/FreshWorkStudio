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
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ItemGifDetailsBinding
import com.app.freshworkstudio.databinding.ItemLoadingBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.DataUtils.loading
import com.skydoves.bindables.binding


/**
 * gif list adapter for searched and trending gifs
 *
 * @param onAdapterPositionClicked = higher order function return clicked item model to save it in local db
 * @param onRetry = retry download same page on error
 * */
class GifListAdapter(
    private val onAdapterPositionClicked: (GifData) -> Unit,
    private val onRetry: (Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items: MutableList<GifData> = arrayListOf()
    private var retryPageLoad: Boolean = false
    private var errorMsg: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == item) {
            val binding = parent.binding<ItemGifDetailsBinding>(R.layout.item_gif_details)
            GifListViewHolder(binding, onAdapterPositionClicked)
        } else {
            val binding = parent.binding<ItemLoadingBinding>(R.layout.item_loading)
            LoadingVH(errorMsg!!, binding, onRetry)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == item) {
            with((holder as GifListViewHolder).binding) {
                gifData = items[position]
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

    fun showErrorPage(s: String) {

        retryPageLoad = true
        val itemSize = items.size
        this.errorMsg = s
        val size = if (itemSize >= loading) itemSize - loading else item
        notifyItemChanged(size)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()


    }

    inner class GifListViewHolder(
        val binding: ItemGifDetailsBinding,
        private val onAdapterPositionClicked: (GifData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.square.setOnClickListener() {
                val item = items[adapterPosition]
                item.images.fixed_width.isFav = (it as AppCompatCheckBox).isChecked
                onAdapterPositionClicked(items[adapterPosition])
            }
        }
    }

    inner class LoadingVH(
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
