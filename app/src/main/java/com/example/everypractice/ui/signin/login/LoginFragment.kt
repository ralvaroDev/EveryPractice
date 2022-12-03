package com.example.everypractice.ui.signin.login

import android.content.*
import android.os.*
import android.text.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import com.example.everypractice.consval.*
import com.example.everypractice.data.models.login.UserResponseStatus.DONE
import com.example.everypractice.data.models.login.UserResponseStatus.ERROR
import com.example.everypractice.databinding.*
import com.example.everypractice.ui.*
import com.example.everypractice.ui.signin.*
import com.example.everypractice.utils.Result.*
import com.google.firebase.auth.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        val EMAIL_PATTERN = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginFragmentViewModel by activityViewModels()

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

        /* binding.btnLogin.setOnClickListener {
             viewModel.startFakeLogin(
                 binding.tfEmail.text.toString(),
                 binding.tfPassword.text.toString()
             )
         }*/
        /*lifecycleScope.launch {
            viewModel.nav.collectLatest {
                when (it) {
                    DONE -> {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    ERROR -> Timber.d("Receive error en UI, no jump")
                }
            }
        }*/
        /////////////////
        /*binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.login(
                        binding.tfEmail.text.toString(),
                        binding.tfPassword.text.toString()
                    ).collectLatest {
                        Timber.d("Colectando $it")
                        when(it){
                            is Success -> {
                                when(it.data.status) {
                                    DONE -> {
                                        val intent = Intent(requireContext(), MainActivity::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()
                                    }
                                    ERROR -> Timber.d("Error with credentials")
                                }
                            }
                            is Error -> Timber.d("Receive error en UI : ${it.exception}")
                            Loading -> {}
                        }
                    }
                }
            }
        }*/
        /////////////////////////
        binding.btnLogin.setOnClickListener {
            loginActionSTABLE()
        }
    }

    private fun loginAction() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startLogin(
                    binding.tfEmail.text.toString(),
                    binding.tfPassword.text.toString()
                ).collectLatest {
                    when (it) {
                        DONE -> {
                            signInTries = 0
                            jumpToMainActivity()
                        }
                        ERROR -> {
                            signInTries++
                            if (signInTries > 2) {
                                showAlert()
                            }
                            binding.tflLogin.error = "Something invalid"
                            binding.tflPassword.error = "Something invalid"
                        }
                        is Exception -> toast()
                        is Loading -> Loading
                    }
                }
            }
        }
    }

    //TODO PUEDE LUEGO DEVOLVERME SOLO UN ENUM DEL CASO QUE HA SUCEDIDO
    // Y EN BASE A ESO REALIZA UNA FUNCION EN ESPECIFICO XD
    private fun loginActionSTABLE(){
        lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startLoginSTABLE(
                    binding.tfEmail.text.toString(),
                    binding.tfPassword.text.toString()
                ).collectLatest {
                    when(it) {
                        is Success -> {
                            signInTries = 0
                            jumpToMainActivity()
                        }
                        is Error -> {
                            try {
                                val eCredentials = (it.exception as FirebaseAuthException).errorCode
                                binding.tflLogin.error = "Something invalid"
                                binding.tflPassword.error = "Something invalid"
                                Timber.d("Error with credentials: $eCredentials")
                            } catch (e: Exception){
                                Timber.d("Error ${it.exception.message}")
                            }
                        }
                        Loading -> {
                            Timber.d("LOADING")
                        }
                    }
                }
            }
        }
    }

    private fun jumpToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun toast(){
        requireActivity().runOnUiThread {
            Toast.makeText(
                requireContext(),
                "No internet connection!! :(",
                Toast.LENGTH_SHORT
            )
                .show()
        }
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
                binding.btnLogin.isEnabled =
                    emailWithValidPattern && passWithValidPattern && passLengthWithPattern
            }
        })

        binding.tfPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passWithValidPattern = s.toString().trim { it <= ' ' }.isNotEmpty()
                passLengthWithPattern = s.toString().length > 6
                binding.tflPassword.isErrorEnabled = false
                binding.btnLogin.isEnabled =
                    emailWithValidPattern && passWithValidPattern && passLengthWithPattern
            }
        })
    }
}