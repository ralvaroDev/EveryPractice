package com.example.everypractice.start.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.everypractice.consval.USER_NAME
import com.example.everypractice.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import timber.log.Timber


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //FULL BIND

        enableOrDisableButtonSearch()
        setup()


    }

    private fun setup() {
        binding.btnRegister.setOnClickListener {
            if (binding.tfRegisterEmail.text!!.isNotEmpty() && binding.tfRegisterPassword.text!!.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.tfRegisterEmail.text.toString(),
                    binding.tfRegisterPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        updateProfile(USER_NAME)
                        goToLogin(it.result.user?.email!!)
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun updateProfile(userName: String) {
        val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = userName
            //photoUri = Uri.parse(profilePhoto)
        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("Update profile success!")
                }
            }
    }

    //EDIT TEXT LISTENER
    private fun enableOrDisableButtonSearch() {
        binding.btnRegister.isEnabled = false
        var email = false
        var pass = false
        var name = false
        binding.tfRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Log.d(TAG, "onTextChanged: ${s.toString().trim()}")
                email = LoginFragment.EMAIL_PATTERN.matches(s.toString())
                binding.btnRegister.isEnabled = email && pass && name
            }
        })
        binding.tfRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pass = s.toString().trim { it <= ' ' }.isNotEmpty()
                binding.btnRegister.isEnabled = email && pass && name
            }
        })

        binding.tfRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
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

    private fun goToStart() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToStartFragment())
    }

    private fun goToLogin(email: String) {
        findNavController().navigate(SignUpFragmentDirections.toLogin(email = email))
    }


}