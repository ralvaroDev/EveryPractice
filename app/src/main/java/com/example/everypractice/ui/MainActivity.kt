package com.example.everypractice.ui

import android.os.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.view.*
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.navigation.*
import androidx.navigation.fragment.*
import com.example.everypractice.R
import com.example.everypractice.databinding.*
import com.google.firebase.messaging.*
import dagger.hilt.android.*
import timber.log.*
import javax.inject.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
//, NavigationHost
{

    companion object {
        /** Key for an int extra defining the initial navigation target. */
        const val EXTRA_NAVIGATION_ID = "extra.NAVIGATION_ID"

        private const val NAV_ID_NONE = -1

        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_home,
            R.id.navigation_search,
            R.id.navigation_favourite
        )
        const val TOKEN = "token"
    }

    @Inject
    lateinit var firebaseMessaging: FirebaseMessaging

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var currentNavId = NAV_ID_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO REVISAR SI ES UNA BUENA MANERA DE EVITAR EL RETROCESO NO DESEADO
        val callback = this.onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_movie) as NavHostFragment
        navController = navHostFragment.navController

        /**
         * That allow us to handle repeat event
         */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentNavId = destination.id
            //binding.bottomNavigation.isGone = (currentNavId == R.id.detailFavouriteFragment)
            //why? TODO hide nav if not a top level destination??
        }

        /*binding.bottomNavigation.apply {
            setupWithNavController(navController)
            //handle navigation to the same item
            //TODO REVISAR LUEGO BIEN ESTO PARA EVITAR QUE SE CREEN MUCHAS PILAS
            setOnItemReselectedListener {
                *//*if (it.itemId ==  R.id.navigation_search && currentNavId != it.itemId){
                    navigateTo(R.id.navigation_search)
                }*//*
            }
        }*/



        binding.bottomNavigation.setOnItemSelectedListener {
            when (val id = it.itemId) {
                R.id.navigation_home -> {
                    navigateTo(id)
                    true
                }
                R.id.navigation_search -> {
                    navigateTo(id)
                    true
                }
                R.id.navigation_favourite -> {
                    navigateTo(id)
                    true
                }
                else -> false
            }
        }


        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = toWindowInsetsCompat(insets, view)
            binding.bottomNavigation.isGone = insetsCompat.isVisible(ime())
            view.onApplyWindowInsets(insets)
        }


    }

    private fun fireMesagge(){
        firebaseMessaging.token.addOnCompleteListener {
            if (!it.isSuccessful){
                Timber.d("Fetching FCM registration token failed: ${it.exception?.message}")
                return@addOnCompleteListener
            }
            val token = it.result
            Timber.d("The toke of user is: $token")
        }

        //TEMAS
        firebaseMessaging.subscribeToTopic("testing")

        //push

        val url =  intent.getStringExtra(TOKEN)
        url?.let {
            Timber.d("Ha llegado info en un push: $it")
        }

    }

    private fun navigateTo(navId: Int) {
        if (navId == currentNavId) {
            return //user tapped the current item
        }
        navController.navigate(navId)
    }

    /*override fun onUserInteraction() {
        super.onUserInteraction()
        getCurrentFragment()?.onUserInteraction()
    }

    private fun getCurrentFragment(): MainNavigationFragment? {
        return navHostFragment.childFragmentManager
            .primaryNavigationFragment as? MainNavigationFragment
    }*/
}

/*

override fun registerToolbarWithNavigation(toolbar: Toolbar) {
    val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
    toolbar.setupWithNavController(navController, appBarConfiguration)
}

override fun onUserInteraction() {
    super.onUserInteraction()
    getCurrentFragment()?.onUserInteraction()
}

* Obtain the current fragment we are
    private fun getCurrentFragment(): MainNavigationFragment? {
        return navHostFragment
            .childFragmentManager
            .primaryNavigationFragment as? MainNavigationFragment
    }

    */
/** This function handles the navigation directions /*


/**
 * Functions like that allow us to go any fragment
private fun openNoConnection() {
navigateTo(fragment de no conection)
}



}




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

}
*/
 */