package com.app.freshworkstudio.ui.adapter.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.freshworkstudio.databinding.ItemLoadingBinding

class LoadingVH(
    val binding: ItemLoadingBinding,
    private val onRetry: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    init {

        binding.loadmoreRetry.setOnClickListener {
            onRetry(absoluteAdapterPosition)
        }
        binding.loadmoreErrortxt.setOnClickListener {
            //binding.progressBar.visibility = VISIBLE
            onRetry(absoluteAdapterPosition)
        }

    }

    fun bind(errorMsg: String) {

        binding.loadmoreErrortxt.text = "$errorMsg"
        if (errorMsg.isNullOrEmpty()) {
            binding.loadmoreRetry.visibility = View.GONE
            binding.tap.visibility = View.GONE
            binding.loadmoreErrortxt.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.loadmoreRetry.visibility = View.VISIBLE
            binding.tap.visibility = View.VISIBLE
            binding.loadmoreErrortxt.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}