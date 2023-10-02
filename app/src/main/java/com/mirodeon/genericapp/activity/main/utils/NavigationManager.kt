package com.mirodeon.genericapp.activity.main.utils

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationManager(
    activity: AppCompatActivity,
    homeId: Int,
    settingsId: Int?,
    navGraphId: Int,
    toolbar: Toolbar?,
    bottomBar: BottomNavigationView?,
    titleView: TextView?,
    fragments: Map<Int, String>,
    homeButton: View?,
    settingsButton: View?,
    noTitle: String
) {
    val navController: NavController

    init {
        navController = activity.findNavController(navGraphId)
        setNavigation(activity, toolbar, homeId, bottomBar)
        changeOnDestination(fragments, titleView, toolbar, noTitle)
        setNavigationToolBar(homeButton, homeId, settingsButton, settingsId)
    }

    private fun setNavigation(
        activity: AppCompatActivity,
        toolbar: Toolbar?,
        homeId: Int,
        bottomBar: BottomNavigationView?
    ) {
        activity.setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(
            activity, navController, AppBarConfiguration(setOf(homeId))
        )

        bottomBar?.let {
            NavigationUI.setupWithNavController(
                it,
                navController
            )
        }

    }

    private fun changeOnDestination(
        fragments: Map<Int, String>,
        titleView: TextView?,
        toolbar: Toolbar?,
        noTitle: String
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->

            var destinationNotFound = true
            fragments.forEach { fragment ->
                if (destination.id == fragment.key) {
                    titleView?.text = fragment.value
                    toolbar?.visibility = View.VISIBLE
                    destinationNotFound = false
                }
            }

            if (destinationNotFound) {
                titleView?.text = noTitle
                toolbar?.visibility = View.INVISIBLE
            }
        }
    }

    private fun setNavigationToolBar(
        homeButton: View?,
        homeId: Int,
        settingsButton: View?,
        settingsId: Int?
    ) {
        homeButton?.setOnClickListener {
            navController.popBackStack(homeId, false)
        }

        settingsButton?.setOnClickListener {
            settingsId?.let {
                navController.navigate(it)
            }
        }
    }
}