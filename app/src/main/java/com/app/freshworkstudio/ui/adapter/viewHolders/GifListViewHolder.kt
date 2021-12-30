package com.app.freshworkstudio.ui.adapter.viewHolders

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.model.GifData

class GifListViewHolder(
    val binding: ViewDataBinding,
    private val onAdapterPositionClicked: (GifData) -> Unit
) : RecyclerView.ViewHolder(binding.root)