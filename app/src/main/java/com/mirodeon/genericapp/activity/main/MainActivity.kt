package com.mirodeon.genericapp.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.activity.main.utils.NavigationManager
import com.mirodeon.genericapp.databinding.ActivityMainBinding
import com.mirodeon.genericapp.network.utils.AlertNetwork
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var navigationManager: NavigationManager
    private lateinit var alertNetwork: AlertNetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        registerNetworkChange()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navigationManager = NavigationManager(
            this,
            R.id.waifuFragment,
            null,
            R.id.navHostFragment,
            binding?.toolbarMenu?.root,
            binding?.bottomNavigationView,
            binding?.toolbarMenu?.titleToolbar,
            mapOf(
                R.id.waifuFragment to getString(R.string.waifus_title)
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

    private fun registerNetworkChange() {
        binding?.root?.let {
            alertNetwork = AlertNetwork(this, it)
        }
        alertNetwork.registerForNetworkStateChanges()
    }

}