package com.example.everypractice.start.launcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.everypractice.prinoptions.movies.ui.MoviesMainActivity
import com.example.everypractice.start.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate Launcher")

        val viewModel: LaunchViewModel by viewModels()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.launchDestination.collectLatest { action ->
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
        }
    }


}