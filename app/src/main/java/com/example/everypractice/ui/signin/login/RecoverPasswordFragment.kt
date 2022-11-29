package com.example.everypractice.ui.signin.login

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.navigationInfoParameters
import com.google.firebase.ktx.Firebase
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

        /*val emailTest = USER_EMAIL_TEST
        binding.tfEmail.setText(emailTest, TextView.BufferType.SPANNABLE)*/

        binding.btnRecoveryPassword.setOnClickListener {
            sendEmailToResetPassword(
                binding.tfEmail.text.toString()
            )
        }

    }

    private fun dynamicLinkCreator(email: String) {
        //TODO IMPLEMENTACION DYNAMIC LINK
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://sunoff.page.link/recoverypassword?email=$email")
            domainUriPrefix = "https://sunoff.page.link"
            navigationInfoParameters {
                forcedRedirectEnabled = true
            }
        }
        val dynamicLinkUri = dynamicLink.uri


        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, dynamicLinkUri.toString())
            type = "text/plain"
        }
        //https://sunoff-d6a55.firebaseapp.com
        startActivity(sendIntent)
        Timber.d("Dynamic Link: $dynamicLinkUri")

        val actionCode = actionCodeSettings {
            url = dynamicLinkUri.toString()
            handleCodeInApp = true
        }
    }

    private fun sendEmailToResetPassword(email: String) {

        val instance = FirebaseAuth.getInstance()
        instance.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                val sendIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_EMAIL)
                }
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(),
                        "Sending Petition",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                Timber.d("Send Password manage")
                sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                requireActivity().finish()

            }
            .addOnFailureListener {
                Timber.d("Error sending: ${it.message} -- LocalizedMessage: ${it.localizedMessage}")
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

    private fun goToLogin() {
        val action = RecoverPasswordFragmentDirections.toLogin()
        findNavController().navigate(action)
    }

}