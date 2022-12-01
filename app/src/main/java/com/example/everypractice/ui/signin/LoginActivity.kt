package com.example.everypractice.ui.signin

import android.os.*
import androidx.appcompat.app.*
import androidx.navigation.*
import androidx.navigation.fragment.*
import com.example.everypractice.R
import com.example.everypractice.databinding.*
import dagger.hilt.android.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController= navHostFragment.navController
    }
}