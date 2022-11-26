package com.example.everypractice.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.everypractice.R
import com.example.everypractice.databinding.ActivityMainBinding
import com.example.everypractice.prinoptions.movies.ui.MoviesMainActivity
import com.example.everypractice.start.datastore.UserPreferenceRepository
import kotlinx.coroutines.flow.collectLatest

enum class ProviderType {
    BASIC
}

const val USER_PREFERENCE_NAME = "user_preferences"
private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCE_NAME
)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var viewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController= navHostFragment.navController


        //TODO LA EXPLOTACION PORQUE USO VM EN MAS DE UNA VISTA
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(NetworkStatusTracker(this),
                UserPreferenceRepository(dataStore))
        )[MainViewModel::class.java]


        /*viewModel.state.observe(this){ state ->
            Timber.d("In State")
            when(state){
                MyState.Fetched -> binding.connection.gone()
                MyState.Error -> binding.connection.visible()
            }
        }*/



        //AQUI PONDREMOS EL INICIO DE SESION
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            viewModel.userPreferenceFlow.collectLatest {
                if (it.userLastSession){
                    reload()
                }
            }
        }
    }

    private fun reload() {
            val intent = Intent(this,MoviesMainActivity::class.java)
            startActivity(intent)
    }

}