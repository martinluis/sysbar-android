package com.lcmm.sysbar.android

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.navigateUp
import com.lcmm.sysbar.android.databinding.ActivityMainBinding
import com.lcmm.sysbar.android.models.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.userText.setOnClickListener {
            navigateToAccess()
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        this.initToolbar()
        this.initDrawer()
        this.initSideMenu()
    }


    /**
     *
     */
    private fun initToolbar() {
        setSupportActionBar(this.binding.toolbar)
    }

    /**
     *
     */
    private fun initSideMenu() {
        this.binding.sideMenu.bringToFront()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        NavigationUI.setupWithNavController(this.binding.sideMenu, navHostFragment.navController)
    }

    /**
     *
     */
    private fun initDrawer() {
        val menuToggle = ActionBarDrawerToggle(this, this.binding.drawerLayout, this.binding.toolbar, R.string.menu_side_open, R.string.menu_side_close)
        this.binding.drawerLayout.addDrawerListener(menuToggle)
        menuToggle.syncState()
    }

    /**
     *
     */
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        return navigateUp(navHostFragment.navController, this.binding.drawerLayout) || super.onSupportNavigateUp()
    }

    /**
     *
     */
    fun updateUserInfo(user: User){
        binding.userText.text =getString(R.string.home_active_user, user.name);
    }

    /**
     *
     */
    private fun navigateToAccess() {
        val navController = findNavController(R.id.nav_host_fragment_container)
        navController.popBackStack(R.id.accessFragment, true)
        navController.navigate(R.id.accessFragment)
    }
}