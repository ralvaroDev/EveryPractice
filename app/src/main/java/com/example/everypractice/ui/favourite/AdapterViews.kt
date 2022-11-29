package com.example.everypractice.ui.favourite

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterViews(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        //TODO UN FUTURO CREACION DE LISTAS PERSONALIZADAS DE MOVIES
        return when (position){
            0 -> FavouriteStoredFragment()
            else -> FavouriteSeenFragment()
        }
    }
}