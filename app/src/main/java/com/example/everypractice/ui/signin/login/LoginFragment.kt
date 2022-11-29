package com.example.everypractice.ui.signin.login

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.everypractice.consval.USER_EMAIL_TEST
import com.example.everypractice.consval.USER_PASSWORD_TEST
import com.example.everypractice.databinding.FragmentLoginBinding
import com.example.everypractice.ui.MainActivity
import com.example.everypractice.ui.MainApplication
import com.example.everypractice.ui.signin.LoginFragmentViewModel
import com.example.everypractice.ui.signin.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class LoginFragment : Fragment() {

    companion object {
        val EMAIL_PATTERN = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginFragmentViewModel by activityViewModels {
        MainViewModelFactory(
            (requireActivity().application as MainApplication).userPreferenceRepository
        )
    }

    private var signInTries = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvForgot.setOnClickListener { goToRecoverPassword() }
        binding.tvRegister.setOnClickListener { goToRegister() }

        enableOrDisableButtonSignIn()

        val emailTest = USER_EMAIL_TEST
        val passwordTest = USER_PASSWORD_TEST

        binding.tfEmail.setText(emailTest, TextView.BufferType.SPANNABLE)
        binding.tfEmail.hint = emailTest

        binding.tfPassword.setText(passwordTest, TextView.BufferType.SPANNABLE)
        binding.tfPassword.hint = passwordTest

        binding.btnLogin.setOnClickListener {
            validateEntries(
                binding.tfEmail.text.toString(),
                binding.tfPassword.text.toString()
            )
        }

    }

    //FUN QUE VERIFICA EL PATRON DE INGRESO DEBE DEVOLVER BOOLEAN PARA ASI QUE ESTA FUN AVANCE EN ENVIAR PETICION
    private fun validateEntries(
        inputEmail: String,
        inputPassword: String,
    ) {
        val instante = Firebase.auth

        instante.signInWithEmailAndPassword(
            inputEmail,
            inputPassword
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                lifecycleScope.launchWhenStarted {
                    viewModel.updateExistUser(true, emailUser = it.result.user?.email.toString())
                }

                goToHome()
                signInTries = 0
            } else {
                try {
                    val errorAuth = (it.exception as FirebaseAuthException).errorCode

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
                } catch (e: Exception) {
                    Timber.d("Error catching: ${e.message} | ${it.exception?.message}")
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),
                            "No internet connection!! :(",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun goToHome() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
        val action = LoginFragmentDirections.toSignUp()
        findNavController().navigate(action)
    }

    private fun goToRecoverPassword() {
        val action = LoginFragmentDirections.toRecoverPassword()
        findNavController().navigate(action)
    }

    //EDIT TEXT LISTENER
    private fun enableOrDisableButtonSignIn() {
        var emailWithValidPattern = false
        var passWithValidPattern = false
        var passLengthWithPattern = false

        binding.tfEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailWithValidPattern = EMAIL_PATTERN.matches(s.toString())
                binding.tflLogin.isErrorEnabled = false
                binding.btnLogin.isEnabled = emailWithValidPattern && passWithValidPattern && passLengthWithPattern
            }
        })

        binding.tfPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passWithValidPattern = s.toString().trim { it <= ' ' }.isNotEmpty()
                passLengthWithPattern = s.toString().length > 6
                binding.tflPassword.isErrorEnabled = false
                binding.btnLogin.isEnabled = emailWithValidPattern && passWithValidPattern && passLengthWithPattern
            }
        })
    }
}