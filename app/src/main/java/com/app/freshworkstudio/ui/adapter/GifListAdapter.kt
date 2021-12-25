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
import com.app.freshworkstudio.model.GifData
import com.skydoves.bindables.binding


// add lamba function as callback
class GifListAdapter : RecyclerView.Adapter<GifListAdapter.MovieListViewHolder>() {

    private val items: MutableList<GifData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val binding = parent.binding<ItemGifBinding>(R.layout.item_gif)
        return MovieListViewHolder(binding).apply {
            /*binding.root.setOnClickListener {
              val movie = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                ?: return@setOnClickListener
              MovieDetailActivity.startActivityModel(it.context, items[movie])
            }*/
        }
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        with(holder.binding) {
            media = items[position].images.fixed_width
        }
    }

    fun addGif(gifs: List<GifData>) {
        val previousItemSize = items.size
        items.addAll(gifs)
        notifyItemRangeInserted(previousItemSize, items.size)
    }

    override fun getItemCount(): Int = items.size

    class MovieListViewHolder(val binding: ItemGifBinding) : RecyclerView.ViewHolder(binding.root)
}
