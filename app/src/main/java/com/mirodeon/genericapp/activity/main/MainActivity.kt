package com.mirodeon.genericapp.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.activity.main.utils.NavigationManager
import com.mirodeon.genericapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navigationManager = NavigationManager(
            this,
            R.id.listApiFragment,
            null,
            R.id.navHostFragment,
            binding?.toolbarMenu?.root,
            binding?.bottomNavigationView,
            binding?.toolbarMenu?.titleToolbar,
            mapOf(
                R.id.listApiFragment to getString(R.string.list_api_title),
                R.id.listDetailsApiFragment to getString(R.string.list_api_details_title)
            ),
            binding?.toolbarMenu?.imageHomeAction,
            binding?.toolbarMenu?.btnActionBar,
            getString(R.string.no_title)
        )
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationManager.navController.navigateUp()
    }

}