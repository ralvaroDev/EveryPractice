package com.example.everypractice.ui.signin.login

import android.content.*
import android.os.*
import android.text.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import com.example.everypractice.consval.*
import com.example.everypractice.databinding.*
import com.example.everypractice.ui.*
import com.example.everypractice.utils.Result.*
import com.google.firebase.auth.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableOrDisableButtonSearch()

        val emailTest = USER_EMAIL_TEST
        val passwordTest = USER_PASSWORD_TEST

        binding.tfRegisterEmail.setText(emailTest, TextView.BufferType.SPANNABLE)
        binding.tfRegisterEmail.hint = emailTest

        binding.tfRegisterPassword.setText(passwordTest, TextView.BufferType.SPANNABLE)
        binding.tfRegisterPassword.hint = passwordTest

        binding.tfRegisterName.setText("Alvaro", TextView.BufferType.SPANNABLE)



        binding.btnRegister.setOnClickListener {
            signUpAction()
        }



    }

    private fun signUpAction() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startSignIn(
                    binding.tfRegisterEmail.text.toString(),
                    binding.tfRegisterPassword.text.toString(),
                    binding.tfRegisterName.text.toString()
                ).collectLatest {
                    when(it) {
                        is Loading -> Loading
                        is Error -> {
                            try {
                                val eCredentials = (it.exception as FirebaseAuthException).errorCode
                                toast(eCredentials)
                                binding.tflPassword.error = "Something invalid"
                                binding.tflLogin.error = "Something invalid"
                                binding.tflName.error = "Something invalid"
                                Timber.d("Error with credentials: $eCredentials")
                            } catch (e: Exception){
                                Timber.d("Error signUp:  ${it.exception.message}")
                            }
                        }
                        is Success -> {
                            goToMainActivity()
                        }
                    }
                }
            }
        }
    }

    private fun toast(message: String?){
        requireActivity().runOnUiThread {
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java).addFlags()
        startActivity(intent)
        requireActivity().finish()
    }

    //EDIT TEXT LISTENER
    private fun enableOrDisableButtonSearch() {
        binding.btnRegister.isEnabled = false
        var email = false
        var pass = false
        var name = false
        var length : Boolean
        binding.tfRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                email = LoginFragment.EMAIL_PATTERN.matches(s.toString())
                binding.btnRegister.isEnabled = email && pass && name
            }
        })
        binding.tfRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pass = s.toString().trim { it <= ' ' }.isNotEmpty()
                length = s.toString().length > 6
                binding.btnRegister.isEnabled = email && pass && name && length
            }
        })

        binding.tfRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                name = s.toString().trim { it <= ' ' }.isNotEmpty()
                binding.btnRegister.isEnabled = email && pass && name
            }
        })

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
            .setMessage("Se ha producido un error autenticando al usuario")
            .setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

}