package com.app.freshworkstudio.ui.uiActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ActivitySearchBinding
import com.app.freshworkstudio.databinding.FavDialogBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.ui.adapter.GifSearchListAdapter
import com.app.freshworkstudio.ui.viewDataModels.TrendingViewModel
import com.app.freshworkstudio.utils.DataUtils
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.ErrorUtil
import com.skydoves.bindables.BindingActivity
import com.skydoves.whatif.whatIfNotNull
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class SearchActivity : BindingActivity<ActivitySearchBinding>(R.layout.activity_search) {

    // initialized view model same as trending as functionality is very much same except search
    private val vm: TrendingViewModel by viewModels()

    // initialized adapter while getting used first time
    private val gifSearchListAdapter: GifSearchListAdapter by lazy {
        GifSearchListAdapter(onAdapterPositionClicked(), onRetry())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            // set an exit transition
            enterTransition = Slide(Gravity.RIGHT)
            exitTransition = Slide(Gravity.LEFT)
        }
        super.onCreate(savedInstanceState)

        Log.d("test", "updateinf ser ${vm.searchQuery}")
        with(binding) {
            adapter = gifSearchListAdapter
            viewModel = vm
            searchBar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // beforeTextChanged
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    Log.d("test", "onasdasdsad")
                    vm.lastPageNumber = item
                    vm.searchQuery = text.toString()
                    vm.query.value = text.toString()
                }

                override fun afterTextChanged(text: Editable?) {
                    Log.d("test", "afterTextChanged $text")

                    text.whatIfNotNull {
                        if (it.isEmpty()) {
                            gifSearchListAdapter.clear()
                        }
                    }

                }
            })

            backIv.setOnClickListener { onBackPressed() }
            cancelButton.setOnClickListener {
                searchBar.setText("")
            }

        }
    }

    /*
    *  callback to save item in db for fav.
    * */
    private fun onAdapterPositionClicked(): (GifData) -> Unit {
        return { gifData ->

            // show dialog of gif to mark fav and unfav

            val alert: AlertDialog.Builder = AlertDialog.Builder(this)
            val dBinding = FavDialogBinding.inflate(LayoutInflater.from(this));

            with(dBinding) {

                alert.setView(root)
                vm.gifFavID = gifData.id
                viewModel = vm
                media = gifData.images.fixed_width
                val alertView = alert.show()
                val radim = gifData.id.substring(Random.nextInt(gifData.id.length))
                vm.fetchGifFavMarket(radim)
                square.setOnClickListener {
                    square.text =
                        if (square.isChecked) getString(R.string.unFav) else getString(R.string.fav)
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

    /*
    *  callback to retry emit last page due to recovery from error
    * */
    private fun onRetry(): (Int) -> Unit {
        return {
            if (FreshWorkApp.isInternetAvailable()) {
                vm.loadGifPage(vm.lastPageNumber.plus(DataUtils.loading))
            } else {
                ErrorUtil.showError(getString(R.string.error_msg_no_internet), binding.root)
                gifSearchListAdapter.showErrorPage(getString(R.string.error_msg_no_internet))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        DataUtils.condition = ""
    }

}