package com.example.everypractice.prinoptions.movies.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.everypractice.R
import com.example.everypractice.databinding.ActivityMoviesMainBinding

class MoviesMainActivity : AppCompatActivity(), NavigationHost {

    companion object{
        /** Key for an int extra defining the initial navigation target. */
        const val EXTRA_NAVIGATION_ID = "extra.NAVIGATION_ID"

        private const val NAV_ID_NONE = -1

        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_home,
            R.id.navigation_search,
            R.id.navigation_favourite
        )
    }

    lateinit var glide: Glide

    private lateinit var binding: ActivityMoviesMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var currentNavId = NAV_ID_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO REVISAR SI ES UNA BUENA MANERA DE EVITAR EL RETROCESO NO DESEADO
        val callback = this.onBackPressedDispatcher.addCallback(this){
            finish()
        }



        //checkIfDeviceIsRooted()





        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_movie) as NavHostFragment

        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener{_, destination, _ ->
            currentNavId = destination.id
            //why? TODO hide nav if not a top level destination??
        }

        binding.bottomNavigation.apply {
            setupWithNavController(navController)
            //handle navigation to the same item
            //TODO REVISAR LUEGO BIEN ESTO PARA EVITAR QUE SE CREEN MUCHAS PILAS
            setOnItemReselectedListener {
                /*if (it.itemId ==  R.id.navigation_search && currentNavId != it.itemId){
                    navigateTo(R.id.navigation_search)
                }*/
            }
        }

        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = toWindowInsetsCompat(insets, view)
            binding.bottomNavigation.isGone = insetsCompat.isVisible(ime())
            view.onApplyWindowInsets(insets)
        }



        /*ViewCompat.setOnApplyWindowInsetsListener(binding.rootContainer) { view, insets ->
            // Hide the bottom navigation view whenever the keyboard is visible.
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            binding.bottomNavigation?.isVisible = !imeVisible

            // If we're showing the bottom navigation, add bottom padding. Also, add left and right
            // padding since there's no better we can do with horizontal insets.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val bottomPadding = if (binding.bottomNavigation?.isVisible == true) {
                systemBars.bottom
            } else 0
            view.updatePadding(
                left = systemBars.left,
                right = systemBars.right,
                bottom = bottomPadding
            )
            // Consume the insets we've used.
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(0, systemBars.top, 0, systemBars.bottom - bottomPadding)
            ).build()
        }
*/

    }













    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        getCurrentFragment()?.onUserInteraction()
    }

    /** Obtain the current fragment we are */
    private fun getCurrentFragment(): MainNavigationFragment? {
        return navHostFragment
            .childFragmentManager
            .primaryNavigationFragment as? MainNavigationFragment
    }

    /** This function handles the navigation directions */
    private fun navigateTo(navId: Int) {
        if (navId == currentNavId){
            return //user tapped the current item
        }
        navController.navigate(navId)
    }

    /**
     * Functions like that allow us to go any fragment
     */
    /*private fun openNoConnection() {
        navigateTo(*//*fragment de no conection*//*)
    }*/



}



/*

private fun checkIfDeviceIsRooted() = CoroutineScope(IO).launch {

    val isRooted = RootBeer(this@MoviesMainActivity).isRooted
    if (isRooted){
        withContext(Main) {
            val snack = Snackbar.make(binding.root,
                "Rooted", Snackbar.LENGTH_SHORT)
            snack.show()
        }
    }
    if (!isRooted){
        withContext(Main) {
            val snack = Snackbar.make(binding.root,
                "No root", Snackbar.LENGTH_SHORT)
            snack.show()
        }
    }

    if (isEmulator()){
        withContext(Main) {
            val snack = Snackbar.make(binding.root,
                "emulator", Snackbar.LENGTH_SHORT)
            delay(1000L)
            snack.show()
        }
    }
    if (!isEmulator()){
        withContext(Main) {
            val snack = Snackbar.make(binding.root,
                "Not emulator", Snackbar.LENGTH_SHORT)
            delay(1000L)
            snack.show()
        }
    }

}*/
