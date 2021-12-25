package com.app.freshworkstudio.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.FragmentMainBinding
import com.app.freshworkstudio.ui.adapter.GifListAdapter
import com.app.freshworkstudio.ui.viewDataModels.TrendingViewModel
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * A trending fragment containing a recyclerview to load all gif.
 */
@AndroidEntryPoint
class TrendingFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val vm: TrendingViewModel by viewModels()

    private val gifListAdapter: GifListAdapter by lazy { GifListAdapter() }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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