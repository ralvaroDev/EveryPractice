package com.example.everypractice.prinoptions.search

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.everypractice.R
import com.example.everypractice.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_PREFIX = "https://www.google.com/search?q="
    }

    private lateinit var navController: NavController

    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_search) as NavHostFragment
        navController = navHostFragment.navController

    }


}