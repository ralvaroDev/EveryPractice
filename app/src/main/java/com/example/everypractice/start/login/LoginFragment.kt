package com.example.everypractice.start.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.everypractice.consval.CURRENT_USER_NAME
import com.example.everypractice.consval.USER_EMAIL_TEST
import com.example.everypractice.consval.USER_PASSWORD_TEST
import com.example.everypractice.databinding.FragmentLoginBinding
import com.example.everypractice.helpers.extensions.gone
import com.example.everypractice.helpers.extensions.inVisible
import com.example.everypractice.helpers.extensions.visible
import com.example.everypractice.prinoptions.movies.ui.MoviesMainActivity
import com.example.everypractice.start.*
import com.example.everypractice.start.datastore.UserPreferenceRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executor

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCE_NAME,
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, USER_PREFERENCE_NAME))
    }
)

class LoginFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    /*private val viewModel2: MainViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val networkStatusTracker = NetworkStatusTracker(requireContext())
                    return MainViewModel(networkStatusTracker, UserPreferenceRepository()) as T
                }
            }
        )[MainViewModel::class.java]
    }*/

    companion object {
        val EMAIL_PATTERN = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        private const val EMAIL = "email"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var email = ""
    private var signInTries = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            email = it?.getString(EMAIL).toString()
        }
    }


    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(NetworkStatusTracker(requireContext()),UserPreferenceRepository(requireContext().dataStore))
        )[MainViewModel::class.java]


        //TODO ESTA COSA TA RARA, tiene bug cada que chapa error de no connection y se activa el internet
        lifecycleScope.launch {
            viewModel.stateHot.collectLatest{ state ->
                Timber.d("Getting state")
                    when (state) {
                        MyState.Fetched -> {
                            binding.preventLogin.gone()
                            binding.notifyConnection.inVisible()
                        }
                        MyState.Error -> {
                            binding.preventLogin.visible()
                            binding.notifyConnection.visible()
                        }
                    }

                }

        }

        binding.preventLogin.setOnClickListener {
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "No internet connection!! :(", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        enableOrDisableButtonSearch()
        /*checkDeviceHasBiometric()
        binding.ivFingerprint.setOnClickListener {
            verificationBiometric()
            biometricPrompt.authenticate(promptInfo)
        }
        binding.btnLogin.setOnClickListener {goToStart()}
        binding.tvRegister.setOnClickListener { goToRegister() }*/

        //TODO SI VIENE DE ESA PANTALLA MEDIANTE EL INTENT DE CORRECTO QUE VENGA CON INDICADOR 1
        //SINO ES POR EL BOTON QUE VENGA CON OTRO
        //Y SI NO HAY INDICADOR QUE NO PINTE NADA

        val emailTest = USER_EMAIL_TEST
        val passwordTest = USER_PASSWORD_TEST

        binding.tfEmail.setText(emailTest, TextView.BufferType.SPANNABLE)
        binding.tfEmail.hint = emailTest

        binding.tfPassword.setText(passwordTest, TextView.BufferType.SPANNABLE)
        binding.tfPassword.hint = passwordTest

        binding.tvRegister.setOnClickListener {
            goToRegister()
        }

        binding.btnLogin.setOnClickListener {
            validateEntries(
                binding.tfEmail.text.toString(),
                binding.tfPassword.text.toString()
            )
        }

    }

    //EDIT TEXT LISTENER
    private fun enableOrDisableButtonSearch() {
        binding.btnLogin.isEnabled = false
        binding.tfEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Log.d(TAG, "onTextChanged: ${s.toString().trim()}")
                binding.btnLogin.isEnabled = EMAIL_PATTERN.matches(s.toString())
                binding.tflLogin.isErrorEnabled = false
            }
        })
        binding.tfPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tfPassword.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
                binding.tflPassword.isErrorEnabled = false
            }
        })
    }


    //FUN QUE VERIFICA EL PATRON DE INGRESO DEBE DEVOLVER BOOLEAN PARA ASI QUE ESTA FUN AVANCE EN ENVIAR PETICION
    private fun validateEntries(
        inputEmail: String = USER_EMAIL_TEST,
        inputPassword: String = USER_PASSWORD_TEST,
    ) {


        val instante = FirebaseAuth.getInstance()


        instante.signInWithEmailAndPassword(
            inputEmail,
            inputPassword
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                CURRENT_USER_NAME = it.result.user?.displayName.toString()
                lifecycleScope.launchWhenStarted {
                    viewModel.updateExistUser(true)
                }
                //TODO ESTO DEBE SER PARA GUARDAR CREDENCIALES EN DISPOSITIVO NO MEDIANTE ROOM
                goToMovies(ProviderType.BASIC)
                signInTries = 0
            } else {
                try {
                    var errorAuth = (it.exception as FirebaseAuthException).errorCode

                    signInTries++
                    if (signInTries > 2) {
                        showAlert()
                    }
                    if (errorAuth == "ERROR_USER_NOT_FOUND") {
                        binding.tflLogin.error = "Invalid Email"
                    }
                    if (errorAuth == "ERROR_WEAK_PASSWORD") {
                        binding.tflPassword.error = "Password lees than 6 characters"
                    }
                    if (errorAuth == "ERROR_WRONG_PASSWORD") {
                        binding.tflPassword.error = "Wrong password"
                    }
                } catch (e: Exception){
                    Timber.d("Erro catching: ${ e.message }")
                    var error = it.exception?.message
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage("No internet connection").setPositiveButton("OK", null).create().show()
                    Timber.d("Error net: ${error.toString()}")
                }

            }
        }


    }

    private fun goToMovies(provider: ProviderType = ProviderType.BASIC) {
        val intent = Intent(context, MoviesMainActivity::class.java)
        startActivity(intent)
    }


    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
            .setMessage("The information still Incorrect :( Desea restablecerlo?")
            .setPositiveButton("Aceptar", null)
            .setNegativeButton("Si, lo olvide :c", myDialogInterface())
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun myDialogInterface() = DialogInterface.OnClickListener { _, _ ->
        goToRegister()
    }


    private fun goToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

    private fun goToStart() {
        val action = LoginFragmentDirections.actionLoginFragmentToStartFragment()
        findNavController().navigate(action)
    }

    private fun verificationBiometric() {

        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(context, "Error de autenticacion", Toast.LENGTH_LONG).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    goToStart()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Autenticacion fallida", Toast.LENGTH_LONG).show()

                }
            })

        //Esta pantalla sale cuando te pide que ingreses tu huella
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Everything")
            .setSubtitle("Ingrese su huella")
            .setNegativeButtonText("Cancel Autentication")
            .build()
    }

    /*fun checkDeviceHasBiometric(){
        val biometricManager = BiometricManager.from(requireContext())
        when(biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)){
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.ivFingerprint.visibility = View.VISIBLE
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE->{
                binding.ivFingerprint.visibility = View.GONE
            }
            //supported but no set
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED->{
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                binding.ivFingerprint.visibility= View.VISIBLE

                startActivityForResult(enrollIntent,100)
            }
        }
    }*/


}