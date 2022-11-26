package com.example.everypractice.start.login

import android.os.Bundle
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

        binding.btnRegister.setOnClickListener {  goToStart() }

        setup()


    }

    private fun setup() {
        binding.btnRegister.setOnClickListener {
            if (binding.tfRegisterEmailId.text!!.isNotEmpty() && binding.tfRegisterPassword.text!!.isNotEmpty()) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.tfRegisterEmailId.text.toString(),
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
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment(email = email))
    }


}