package com.example.everypractice.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.everypractice.ui.signin.LoginActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        screenSplash.setKeepOnScreenCondition{ true }

        lifecycleScope.launch {
            (this@LauncherActivity.application as MainApplication).userPreferenceRepository.userPreferenceFlow.collectLatest {
                when(it.userLastSession){
                    true -> startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
                    false -> startActivity(Intent(this@LauncherActivity, LoginActivity::class.java))
                }
                finish()
            }
        }
    }
}