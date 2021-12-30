package com.app.freshworkstudio.ui.uiActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ActivitySearchBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.ui.adapter.GifSearchListAdapter
import com.app.freshworkstudio.ui.viewDataModels.SearchViewModel
import com.app.freshworkstudio.utils.DataUtils
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.binding.ViewUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    // initialized view model same as trending as functionality is very much same except search
    private val vm: SearchViewModel by viewModels()

    // initialized adapter while getting used first time
    private val gifSearchListAdapter: GifSearchListAdapter by lazy {
        GifSearchListAdapter(onAdapterPositionClicked(), onRetry())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {

            adapter = gifSearchListAdapter
            viewModel = vm
            searchBar.addTextChangedListener(object : TextWatcher {
                //This method is called to notify you that, within s, the count characters beginning at start are about to be
                // replaced by new text with length after.
                override fun beforeTextChanged(
                    text: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                    vm.lastPageNumber = item
                    vm.searchQuery = text.toString()
                    vm.query.value = text.toString()
                    if (text.isNotEmpty() && cancelButton.visibility == INVISIBLE) {
                        cancelButton.visibility = VISIBLE
                    }
                }

                override fun afterTextChanged(text: Editable) {
                    if (text.isEmpty()) {
                        gifSearchListAdapter.clear()
                        cancelButton.visibility = INVISIBLE
                    }

                }
            })

            backIv.setOnClickListener { onBackPressed() }
            cancelButton.setOnClickListener {
                searchBar.setText("")
            }

            searchBar.postDelayed({
                searchBar.requestFocus()
                showKeyBoard(searchBar)
            }, DataUtils.delay.toLong())

        }
    }


    /*
    *  callback to save item in db for fav.
    * */
    private fun onAdapterPositionClicked(): (GifData) -> Unit {
        return { gifData ->

            // show dialog of gif to mark fav and unfav
            ViewUtils.showFavDialog(gifData, this, vm)
        }
    }

    /*
    *  callback to retry emit last page due to recovery from error
    * */
    private fun onRetry(): (Int) -> Unit {
        return {
            if (FreshWorkApp.isInternetAvailable()) {
                vm.loadGifPage(vm.getCurrentPage().plus(DataUtils.loading))
            } else {
                showError(getString(R.string.error_msg_no_internet))
                gifSearchListAdapter.showErrorPage(getString(R.string.error_msg_no_internet))
            }
        }
    }

}