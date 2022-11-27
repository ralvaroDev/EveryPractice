package com.example.everypractice.start.login

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.everypractice.databinding.FragmentRecoverPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import timber.log.Timber

class RecoverPasswordFragment : Fragment() {

    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableOrDisableButtonSearch()

        binding.btnRecoveryPassword.setOnClickListener {
            recoveryFun(
                binding.tfEmail.text.toString()
            )
        }


    }

    private fun recoveryFun(email: String) {
        val instance = FirebaseAuth.getInstance()
        instance.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                showDoneRecover()
            } else {
                try {
                    var errorAuth = (it.exception as FirebaseAuthException).errorCode
                    AlertDialog.Builder(requireContext()).setTitle("Error")
                        .setMessage(errorAuth).setPositiveButton("Try again", null).create()
                        .show()
                } catch (e: Exception) {
                    Timber.d("Error RECOVERY catching: ${e.message}")
                    var error = it.exception?.message
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),
                            "No internet connection!! :(",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                    Timber.d("Error RECOVERY net: ${error.toString()}")
                }
            }
        }
    }

    private fun showDoneRecover() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Done")
            .setMessage("Look at your mail to recover your account")
            .setPositiveButton("Aceptar", myDialogInterface())
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun myDialogInterface() = DialogInterface.OnClickListener { _, _ ->
        goToLogin()
    }

    //EDIT TEXT LISTENER
    private fun enableOrDisableButtonSearch() {
        binding.btnRecoveryPassword.isEnabled = false
        var email: Boolean
        binding.tfEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Log.d(TAG, "onTextChanged: ${s.toString().trim()}")
                email = LoginFragment.EMAIL_PATTERN.matches(s.toString())
                binding.btnRecoveryPassword.isEnabled = email
            }
        })
    }

    private fun goToLogin(){
        val action = RecoverPasswordFragmentDirections.toLogin("")
        findNavController().navigate(action)
    }

}