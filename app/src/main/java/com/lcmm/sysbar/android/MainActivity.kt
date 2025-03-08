package com.lcmm.sysbar.android

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
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

        this.initNavigation()
        this.initToolbar()
        this.initDrawer()
        this.initSideMenu()
    }


    /**
     *
     */
    private fun initNavigation() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        NavigationUI.setupWithNavController(this.binding.sideMenu, navHostFragment.navController, )
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
        navController.navigateBack(R.id.accessFragment,null)
    }
}

/**
 *  Extension function on NavController - Forward
 */
fun NavController.navigateForward(@IdRes destinationId: Int, args: Bundle? ) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in)  // Enter animation
        .setExitAnim(R.anim.fade_out)    // Exit animation
        .setPopEnterAnim(R.anim.fade_in) // Pop enter animation (when going back)
        .setPopExitAnim(R.anim.slide_out)   // Pop exit animation (when going back)
        .build()
    this.navigate(destinationId, args, navOptions)
}

/**
 *
 */
fun NavController.navigateForward(directions: NavDirections) {
    navigateForward(directions.actionId, directions.arguments)
}

/**
*  Extension function on NavController - Back
*/
fun NavController.navigateBack(@IdRes destinationId: Int, args: Bundle?) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_out)  // Enter animation
        .setExitAnim(R.anim.fade_in)    // Exit animation
        .setPopEnterAnim(R.anim.fade_out) // Pop enter animation (when going back)
        .setPopExitAnim(R.anim.slide_in)   // Pop exit animation (when going back)
        .build()
    this.navigate(destinationId, args, navOptions)
}

/**
*  Extension function on NavController - Back
*/
fun NavController.navigateBack(directions: NavDirections) {
    this.navigateBack(directions.actionId, directions.arguments)
}