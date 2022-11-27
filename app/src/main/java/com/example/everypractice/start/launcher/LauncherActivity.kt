package com.example.everypractice.start.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.everypractice.prinoptions.HistoryApplication
import com.example.everypractice.prinoptions.movies.ui.MoviesMainActivity
import com.example.everypractice.start.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber



class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        screenSplash.setKeepOnScreenCondition{ true }
        Timber.d("onCreate Launcher")

        lifecycleScope.launch {
            (this@LauncherActivity.application as HistoryApplication).userPreferenceRepository.userPreferenceFlow.collectLatest {
                when(it.userLastSession){
                    true -> startActivity(Intent(this@LauncherActivity, MoviesMainActivity::class.java))
                    false -> startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
                }
                finish()
            }
        }

        //val viewModel: LaunchViewModel by viewModels()

        /*lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.launchDestination.collectLatest { _ ->
                    when(action) {
                        is LaunchNavigatonAction.NavigateToMainActivityAction -> startActivity(
                            Intent(this@LauncherActivity, MainActivity::class.java)
                        )
                        is LaunchNavigatonAction.NavigateToOnboardingAction -> startActivity(
                            Intent(this@LauncherActivity, MoviesMainActivity::class.java)
                        )
                    }
                    finish()
                }
            }
        }*/
    }


}