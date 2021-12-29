package com.app.freshworkstudio.ui.fragments

import android.content.res.Configuration
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.app.freshworkstudio.utils.DataUtils
import com.google.android.material.snackbar.Snackbar
import com.skydoves.bindables.BindingFragment

/**
 * Base fragment class used to remove duplicate code from main fragment.
 *
 * Use this to write your common code related to ui
 * */
open class BaseFragment<T : ViewDataBinding>(rLayout: Int) : BindingFragment<T>(rLayout) {


    /*
   * when configuration chagnes we should not re create the fragment , just handle the ui changes
   * */

    fun onConfigurationChanged(newConfig: Configuration, layoutManager: GridLayoutManager) {
        layoutManager.spanCount =
            if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
                DataUtils.landScapSpanCount
            } else {
                DataUtils.portraitSpanCount
            }
    }

    fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}