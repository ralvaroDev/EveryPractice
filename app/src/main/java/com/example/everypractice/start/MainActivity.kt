package com.example.everypractice.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.everypractice.R
import com.example.everypractice.databinding.ActivityMainBinding
import com.example.everypractice.prinoptions.HistoryApplication
import com.example.everypractice.prinoptions.movies.ui.MoviesMainActivity
import timber.log.Timber

enum class ProviderType {
    BASIC
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController



    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            NetworkStatusTracker(this),
            (this.application as HistoryApplication).userPreferenceRepository
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController= navHostFragment.navController

        /*lifecycleScope.launchWhenCreated {
            Timber.d("Init Scope")
            viewModel.userPreferenceFlow.collectLatest {
                if (it.userLastSession){
                    Timber.d("Obtaining info of existUser")
                    reload()
                    Timber.d("EndScope")
                }
            }
        }*/


        //TODO LA EXPLOTACION PORQUE USO VM EN MAS DE UNA VISTA
        /*viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(NetworkStatusTracker(this),
                UserPreferenceRepository(dataStore))
        )[MainViewModel::class.java]*/


        /*viewModel.state.observe(this){ state ->
            Timber.d("In State")
            when(state){
                MyState.Fetched -> binding.connection.gone()
                MyState.Error -> binding.connection.visible()
            }
        }*/



        //AQUI PONDREMOS EL INICIO DE SESION
    }

    //TODO CREO QUE AQUI ESTA EL PROBLEMA DE LA RECREACION
    //TODO PARA EL REPINTADO DE LAS LISTAS DE SEARCH, QUE FILTREMOS LEYENDO BIEN EL ESTADO, Y APLICANDO UN SHIMMER PARA EVITAR DOBLES
    override fun onStart() {
        super.onStart()

    }

    private fun reload() {
            val intent = Intent(this,MoviesMainActivity::class.java)
        Timber.d("Sending intent MovieActivity")
            startActivity(intent)
    }

}