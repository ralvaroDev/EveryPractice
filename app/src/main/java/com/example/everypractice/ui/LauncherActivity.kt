package com.example.everypractice.ui

import android.content.*
import android.os.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.*
import com.example.everypractice.ui.StartView.*
import com.example.everypractice.ui.signin.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        screenSplash.setKeepOnScreenCondition { true }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.launchDestination.collectLatest { view ->
                    when (view) {
                        ONBOARDING -> Timber.d("Should start with onboarding")
                        LOGIN -> {
                            startActivity(
                                Intent(this@LauncherActivity, LoginActivity::class.java).addFlags()
                            )
                            finish()
                        }
                        MAIN -> {
                            startActivity(
                                Intent(this@LauncherActivity, MainActivity::class.java).addFlags()
                            )
                            finish()
                        }
                    }
                }
            }
        }

    }
}

fun Intent.addFlags(): Intent {
    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    return this
}