package com.app.freshworkstudio.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.FragmentMainBinding
import com.app.freshworkstudio.ui.adapter.GifListAdapter
import com.app.freshworkstudio.ui.viewDataModels.TrendingViewModel
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.DataUtils.loading
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * A trending fragment containing a recyclerview to load all gif.
 */
@AndroidEntryPoint
class TrendingFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val vm: TrendingViewModel by viewModels()

    private val gifListAdapter: GifListAdapter by lazy {
        GifListAdapter(
            onAdapterPositionClicked(),
            onRetry()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding {
            adapter = gifListAdapter
            viewModel = vm
        }.root
    }

    private fun onAdapterPositionClicked(): (Int) -> Unit {
        return {
            // save to database and check to list
            Log.d("keyur", "onAdapterPositionClicked")
        }

    }

    private fun onRetry(): (Int) -> Unit {
        return {
            Log.d("keyur", "onRetry")
            vm.loadGifPage(vm.lastPageNumber.plus(loading))
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    return false
                }
                val cacheItem = if (vm.lastPageNumber > item) item else loading
                vm.lastPageNumber = item
                vm.loadGifPage(cacheItem)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //if (!newText.isNullOrEmpty()) {
                newText?.let { vm.searchQuery = newText }
                //}
                return false
            }
        })

        val closeButton: View? =
            binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {
            Log.d("keyur", "on search cleared")

            vm.searchQuery = ""

            with(binding.searchView) {
                setQuery(vm.searchQuery, false)
                clearFocus()
            }
            val cacheCount = vm.lastPageNumber
            vm.lastPageNumber = item
            vm.loadGifPage(cacheCount.plus(loading))
        }

    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): TrendingFragment {
            return TrendingFragment()/*.apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }*/
        }
    }


}