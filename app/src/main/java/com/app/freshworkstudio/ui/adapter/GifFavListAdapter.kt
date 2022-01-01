

package com.app.freshworkstudio.ui.adapter

import android.view.ViewGroup
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ItemGifFavBinding
import com.app.freshworkstudio.model.Media
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.ui.adapter.viewHolders.GifListViewHolder
import com.skydoves.bindables.binding


/**
 * gif list adapter for favourite gifs
 *
 * @param onAdapterPositionClicked = higher order function return clicked item model to remove it in local db
 * */
class GifFavListAdapter(private val onAdapterPositionClicked: (Any) -> Unit) :
    BaseGifAdapter<GifListViewHolder>() {

    val items: MutableList<GifFavourite> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifListViewHolder {

        val binding = parent.binding<ItemGifFavBinding>(R.layout.item_gif_fav)
        return GifListViewHolder(binding, onAdapterPositionClicked).apply {
            binding.square.apply {
                setOnClickListener() {
                    val pos = absoluteAdapterPosition
                    val item = items[pos]
                    items.removeAt(pos)
                    notifyItemRemoved(pos)
                    onAdapterPositionClicked(item)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: GifListViewHolder, position: Int) {
        with(holder.binding as ItemGifFavBinding) {
            val item = items[position]
            media = Media(item.url, true, item.title)
        }
    }

    override fun addGif(dList: Any) {

        val list = dList as List<GifFavourite>
        val listSize = list.size
        val oItemSize = items.size

        if (oItemSize == listSize) {
            return
        }
        items.clear()
        items.addAll(list)
        val nItemSize = items.size
        if (oItemSize < listSize) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(oItemSize, nItemSize)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun showErrorPage(message: String) {
        // no implementation as we don't have error page for fav gif list
    }

    override fun clear() {
        items.clear()
    }
}
