package com.example.everypractice.ui.movies.ui

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.fragment.app.*


/**
 * To be implement by components that host top-level navigation destinations
 */
interface NavigationHost {

    /** Called by MainNavigationFragment to setup it's toolbar with the navigation controller. */
    fun registerToolbarWithNavigation(toolbar: androidx.appcompat.widget.Toolbar)
}

/**
 * To be implemented by main navigation destinations shown by a [NavigationHost]
 */
interface NavigationDestination{

    /** Called by the host when the user interacts with it. */
    fun onUserInteraction() {}
}

/**
 * Fragment representing a main navigation destination. This class handles wiring up the [Toolbar]
 * navigation icon if the fragment is attached to a [NavigationHost].
 */
open class MainNavigationFragment : Fragment(), NavigationDestination{

    protected var navigationHost: NavigationHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationHost) {
            navigationHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // If we have a toolbar and we are attached to a proper navigation host, set up the toolbar
        // navigation icon
        val host = navigationHost ?: return

        //THIS IN CASE WE HAVE A TOOLBAR ADDITIONAL TO BOTTOM NAV
        /*val mainToolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar) ?: return
        mainToolbar.apply {
            host.registerToolbarWithNavigation(this)
        }*/

    }
}