package com.app.freshworkstudio.utils.binding

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.app.freshworkstudio.R
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.ui.viewDataModels.BaseViewModel
import com.app.freshworkstudio.utils.DataUtils.item
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object ViewBinding {

    @JvmStatic
    @BindingAdapter("visible")
    fun setVisibleGone(view: View, isVisible: Boolean?) {
        view.visibility = if (isVisible != false) {
            VISIBLE
        } else {
            GONE
        }
    }

    @JvmStatic
    @BindingAdapter("loadGif", "progressGif")
    fun loadGif(itemView: AppCompatImageView, url: String, progress: ProgressBar) {

        progress.visibility = VISIBLE
        Glide.with(itemView.context)
            .asGif()
            .load(url)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = GONE
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = GONE
                    return false
                }
            })
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade(250))
            .into(itemView)
    }

    @JvmStatic
    @BindingAdapter("loadCircleImage")
    fun bindLoadCircleImage(view: AppCompatImageView, url: String) {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions().circleCrop())
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("isFav")
    fun bindCheckBox(view: AppCompatCheckBox, list: List<GifFavourite>) {
        view.isChecked = list.isEmpty() || list[item] != null

        view.text = if (view.isChecked) {
            view.context.getString(R.string.unFav)
        } else {
            view.context.getString(R.string.fav)
        }
    }

    @JvmStatic
    @BindingAdapter("textTitle")
    fun bindTextTitle(view: AppCompatTextView, title: String) {
        view.text = "$title"
        view.isSelected = true
        view.marqueeRepeatLimit
    }

    @JvmStatic
    @BindingAdapter("loadLoader", "loaderViewModel")
    fun bindLoader(view: ProgressBar, isLoading: Boolean, viewModel: BaseViewModel) {
        view.visibility = if (viewModel.lastPageNumber == item) {
            if (isLoading) VISIBLE else GONE
        } else {
            GONE
        }
    }
}