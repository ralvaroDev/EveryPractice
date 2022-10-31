package com.example.everypractice.start.login

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.everypractice.R
import com.example.everypractice.databinding.FragmentLoginBinding
import com.example.everypractice.databinding.FragmentStartFagmentBinding
import java.util.concurrent.Executor


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!



    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        checkDeviceHasBiometric()
        binding.ivFingerprint.setOnClickListener {
            verificationBiometric()
            biometricPrompt.authenticate(promptInfo)
        }
        binding.btnLogin.setOnClickListener {goToStart()}
        binding.tvRegister.setOnClickListener { goToRegister() }

    }

    private fun goToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

    private fun goToStart(){
        val action = LoginFragmentDirections.actionLoginFragmentToStartFragment()
        findNavController().navigate(action)
    }

    private fun verificationBiometric(){

        executor= ContextCompat.getMainExecutor(requireContext())

        biometricPrompt= BiometricPrompt(this,executor,object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context,"Error de autenticacion",Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                goToStart()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context,"Autenticacion fallida",Toast.LENGTH_LONG).show()

            }
        })

        //Esta pantalla sale cuando te pide que ingreses tu huella
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Everything")
            .setSubtitle("Ingrese su huella")
            .setNegativeButtonText("Cancel Autentication")
            .build()
    }

    fun checkDeviceHasBiometric(){
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
    }




}