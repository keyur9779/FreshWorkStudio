package com.app.freshworkstudio.ui.uiActivity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.viewbinding.BuildConfig
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ActivityMainBinding
import com.app.freshworkstudio.ui.adapter.SectionsPagerAdapter
import com.app.freshworkstudio.ui.view.callBack.OnSearchCallBack
import com.app.freshworkstudio.ui.viewDataModels.SearchViewModel
import com.app.freshworkstudio.utils.DataUtils.pagerTitleList
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.tabs.TabLayoutMediator
import com.app.freshworkstudio.BuildConfig.isFragmentSearch


/**
 * Main class where user will interact with application
 * */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val vm: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {

            setSupportActionBar(toolbar)

            viewModel = vm

            searchView.onSearchView = object : OnSearchCallBack {
                override fun onClose() {
                    updateToolBar(true,toolbar)
                }

            }

            viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, lifecycle)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = pagerTitleList[position]
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle toolbar item clicks here. It'll
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_search -> {


                if(isFragmentSearch) {
                    binding.searchView.openSearch()
                    updateToolBar(false, binding.toolbar)
                }else{
                    // Open the search view on the menu item click.
                    startActivity(
                        Intent(this@MainActivity, SearchActivity::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*
    * Handle back stack of tab layout, just make sure activity is not finishing.
    * */
    override fun onBackPressed() {
        Log.d("keyur", "on actovotu onBackPressed")
        super.onBackPressed()
    }

    override fun onDestroy() {
        Log.d("keyur", "on actovotu onDestroy")

        super.onDestroy()
    }
}