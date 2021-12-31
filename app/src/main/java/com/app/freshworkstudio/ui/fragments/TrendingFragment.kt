package com.app.freshworkstudio.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.FragmentMainBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.ui.adapter.GifListAdapter
import com.app.freshworkstudio.ui.viewDataModels.TrendingViewModel
import com.app.freshworkstudio.utils.DataUtils.loading
import com.app.freshworkstudio.utils.binding.ViewUtils
import dagger.hilt.android.AndroidEntryPoint


/**
 * A trending fragment containing a recyclerview to load all gif.
 */
@AndroidEntryPoint
class TrendingFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {


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
        onConfigurationChanged(newConfig, binding.recyclerView.layoutManager as GridLayoutManager)
        super.onConfigurationChanged(newConfig)
    }

    /*
    *  callback to save item in db for fav.
    * */
    private fun onAdapterPositionClicked(): (GifData) -> Unit {
        return { gifData ->

            // show dialog of gif to mark fav and unfav

            ViewUtils.showFavDialog(gifData,requireContext(),vm)
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
                showError(getString(R.string.error_msg_no_internet))
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