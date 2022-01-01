package com.app.freshworkstudio.utils.binding

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.FavDialogBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.ui.viewDataModels.BaseViewModel
import kotlin.random.Random

object ViewUtils {
    private var isDialogShowning = false


    fun showFavDialog(gifData: GifData, context: Context, vm: BaseViewModel) {

        if (isDialogShowning) {
            return
        }

        isDialogShowning = true
        val alert: AlertDialog.Builder = AlertDialog.Builder(context)
        val dBinding = FavDialogBinding.inflate(LayoutInflater.from(context));

        with(dBinding) {

            alert.setView(root)

            vm.gifFavID = gifData.id
            viewModel = vm
            media = gifData.images.fixed_width
            val alertView = alert.show()
            alertView.setOnDismissListener {
                isDialogShowning = false
            }
            val radim = gifData.id.substring(Random.nextInt(gifData.id.length))
            vm.fetchGifFavMarket(radim)
            square.setOnClickListener {
                square.text =
                    if (square.isChecked) context.getString(R.string.unFav) else context.getString(R.string.fav)
                vm.insertItem(
                    GifFavourite(
                        gifID = gifData.id,
                        url = gifData.images.fixed_width.url,
                        title = gifData.title
                    )
                )
            }
            cancelAction.setOnClickListener {
                alertView.dismiss()
            }
        }

    }

}