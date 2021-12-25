package com.app.freshworkstudio.utils.binding

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette

object ViewBinding {

    @JvmStatic
    @BindingAdapter("visible")
    fun setVisibleGone(view: View, isVisible: Boolean?) {
        view.visibility = if (isVisible != false) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("loadGif")
    fun loadGif(itemView: AppCompatImageView, url: String) {

        val circularProgressDrawable = CircularProgressDrawable(itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f

        circularProgressDrawable.start()
        Glide.with(itemView.context)
            .asGif()
            .load(url)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.placeholder(ColorDrawable(ColoredPlaceholderGenerator.generate(itemView.context)))
                    .placeholder(circularProgressDrawable)
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
    @BindingAdapter("loadPaletteImage", "loadPaletteTarget")
    fun bindLoadImage(view: AppCompatImageView, url: String, targetView: View) {
        Glide.with(view)
            .load(url)
            .listener(
                GlidePalette.with(url)
                    .use(BitmapPalette.Profile.VIBRANT)
                    .intoBackground(targetView)
                    .crossfade(true)
            )
            .into(view)
    }
}