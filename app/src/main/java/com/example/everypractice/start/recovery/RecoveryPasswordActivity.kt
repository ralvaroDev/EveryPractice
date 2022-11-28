package com.example.everypractice.start.recovery

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.everypractice.databinding.ActivityRecoveryPasswordBinding
import com.example.everypractice.start.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class RecoveryPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoveryPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoveryPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        enableOrDisableButtonSearch()

        //Timber.d("URI POSSIBLE: ${data.toString()}")

        binding.btnRecoveryPassword.setOnClickListener {
            if (data!!.getQueryParameter("oobCode") != null && data.getQueryParameter("mode") != null){
                val code = data.getQueryParameter("oobCode")!!
                val mode = data.getQueryParameter("mode")!!
                resetingPassword(
                    mode, code
                )
            }
        }

    }

    private fun resetingPassword(mode: String, oobCode: String){
        val password = binding.tfPassword.text.toString()

        val instance = Firebase.auth
        instance.verifyPasswordResetCode(oobCode).addOnCompleteListener {
            try {
                binding.tvEmailConfirmation.text = it.result
                Timber.d("Verifying the code oobCode receiving email: ${it.result}")
                instance.confirmPasswordReset(oobCode,password).addOnCompleteListener { cfmPassword ->
                    try {
                        //TODO REVISAR LOS POSIBLES RETORNOS Y MANEJARLOS TODOS
                        Timber.d("Changing password is OK: ${cfmPassword.result}")
                        showDoneRecover()
                    } catch (e: Exception){
                        Timber.d("Some mistake with sending change password petition: ${e.message}")
                    }
                }
            } catch (e: Exception){
                //TODO IMPLEMENT METHOS TO TRY AGAIN
                Timber.d("Some mistake getting confirmation code: ${e.message}")
            }
        }

    }


    //TODO IMPLEMENTATION OF RECOVERY DYNAMIC LINKS
    private fun recoveringDynamicLinks(){
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) {
                val deppLink: Uri?
                if (it != null){
                    deppLink = it.link
                    //val decoded = URLDecoder.decode(deppLink.toString(),"utf-8")
                    //Timber.d("Query: ${decoded.getQueryParameter("email").toString()}")
                    Timber.d("Query: ${deppLink.toString()}")
                } else {
                    Timber.d("Error getting dynamic lik")
                }

            }
            .addOnFailureListener(this) { e -> Timber.d( "getDynamicLink:onFailure -> ${e.message}") }

    }

    private fun showDoneRecover() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Done")
            .setMessage("New password set, use it to log again")
            .setPositiveButton("Accept", myDialogInterface())
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun myDialogInterface() = DialogInterface.OnClickListener { _, _ ->
        goToLogin()
    }


    private fun goToLogin() {
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    //EDIT TEXT LISTENER
    private fun enableOrDisableButtonSearch() {
        binding.btnRecoveryPassword.isEnabled = false
        var pass: Boolean
        var lenght: Boolean
        binding.tfPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pass = s.toString().trim { it <= ' ' }.isNotEmpty()
                lenght = s.toString().length > 6
                binding.btnRecoveryPassword.isEnabled = pass && lenght
            }
        })
    }
}