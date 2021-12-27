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

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ItemGifBinding
import com.app.freshworkstudio.model.Media
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.utils.DataUtils.item
import com.skydoves.bindables.binding


/**
 * gif list adapter for favourite gifs
 *
 * @param onAdapterPositionClicked = higher order function return clicked item model to remove it in local db
 * */
class GifFavListAdapter(
    private val onAdapterPositionClicked: (GifFavourite) -> Unit
) :
    RecyclerView.Adapter<GifFavListAdapter.GifFavListViewHolder>() {

    val items: MutableList<GifFavourite> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifFavListViewHolder {

        val binding = parent.binding<ItemGifBinding>(R.layout.item_gif)
        return GifFavListViewHolder(binding, onAdapterPositionClicked)
    }

    override fun onBindViewHolder(holder: GifFavListViewHolder, position: Int) {
        with(holder.binding) {

            media = Media(items[position].url,true)
        }
    }

    fun addGif(gifs: List<GifFavourite>) {

        val oItemSize = items.size

        if (oItemSize == gifs.size) {
            return
        }

        items.clear()
        items.addAll(gifs)

        val nItemSize = items.size
        if (oItemSize == item) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(oItemSize, nItemSize)

        }

    }

    override fun getItemCount(): Int = items.size

    inner class GifFavListViewHolder(
        val binding: ItemGifBinding,
        private val onAdapterPositionClicked: (GifFavourite) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.square.apply {
                setOnClickListener() {
                    val pos = adapterPosition
                    val item = items[adapterPosition]
                    items.removeAt(pos)
                    notifyItemRemoved(pos)
                    onAdapterPositionClicked(item)
                }
            }
        }
    }
}
