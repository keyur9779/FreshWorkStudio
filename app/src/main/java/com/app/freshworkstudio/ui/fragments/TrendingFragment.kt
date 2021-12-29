package com.app.freshworkstudio.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.FavDialogBinding
import com.app.freshworkstudio.databinding.FragmentMainBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.model.entity.GifFavourite
import com.app.freshworkstudio.ui.adapter.GifListAdapter
import com.app.freshworkstudio.ui.viewDataModels.TrendingViewModel
import com.app.freshworkstudio.utils.DataUtils
import com.app.freshworkstudio.utils.DataUtils.loading
import com.app.freshworkstudio.utils.ErrorUtil.showError
import com.google.android.material.snackbar.Snackbar
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


/**
 * A trending fragment containing a recyclerview to load all gif.
 */
@AndroidEntryPoint
class TrendingFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {


    // initialized view model
    private val vm: TrendingViewModel by viewModels()

    // initialized adapter while getting used first time
    private val gifListAdapter: GifListAdapter by lazy {
        GifListAdapter(onAdapterPositionClicked(), onRetry())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding {
            adapter = gifListAdapter
            viewModel = vm
        }.root
    }

    /*
       * when configuration chagnes we should not re create the fragment , just handle the ui changes
       * */
    override fun onConfigurationChanged(newConfig: Configuration) {
        val layoutManager = binding.recyclerView.layoutManager as GridLayoutManager
        layoutManager.spanCount =
            if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
                DataUtils.landScapSpanCount
            } else {
                DataUtils.portraitSpanCount
            }
        super.onConfigurationChanged(newConfig)
    }

    /*
    *  callback to save item in db for fav.
    * */
    private fun onAdapterPositionClicked(): (GifData) -> Unit {
        return { gifData ->

            // show dialog of gif to mark fav and unfav

            val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val dBinding = FavDialogBinding.inflate(LayoutInflater.from(context));

            with(dBinding) {

                alert.setView(root)
                vm.gifFavID = gifData.id
                viewModel = vm
                media = gifData.images.fixed_width
                val alertView = alert.show()
                val radim = gifData.id.substring(Random.nextInt(gifData.id.length))
                vm.fetchGifFavMarket(radim)
                square.setOnClickListener {
                    square.text = if (square.isChecked) getString(R.string.unFav) else getString(R.string.fav)
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
                vm.loadGifPage(vm.getCurrentPage().plus(loading))
            } else {
                showError(getString(R.string.error_msg_no_internet),binding.root)
                gifListAdapter.showErrorPage(getString(R.string.error_msg_no_internet))
            }
        }
    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(): TrendingFragment {
            return TrendingFragment()
        }
    }


}