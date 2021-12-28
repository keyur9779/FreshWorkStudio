package com.app.freshworkstudio.ui.uiActivity

import android.os.Bundle
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ActivityMainBinding
import com.app.freshworkstudio.ui.adapter.SectionsPagerAdapter
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.DataUtils.loading
import com.app.freshworkstudio.utils.DataUtils.pagerTitleList
import com.google.android.material.tabs.TabLayoutMediator
import com.skydoves.bindables.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main class where user will interact with application
 * */
@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {

            setSupportActionBar(toolbar)
            viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, lifecycle)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = pagerTitleList[position]
            }.attach()
        }
    }

    /*
    * Handle back stack of tab layout, just make sure activity is not finishing.
    * */
    override fun onBackPressed() {
        with(binding) {
            if (viewPager.currentItem > item) {
                viewPager.currentItem = viewPager.currentItem - loading
            } else {
                super.onBackPressed()
            }
        }
    }
}