package com.example.everypractice.ui.signin.login

import android.content.*
import android.os.*
import android.text.*
import android.view.*
import androidx.appcompat.app.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import com.example.everypractice.databinding.*
import com.example.everypractice.ui.*
import com.example.everypractice.ui.signin.*
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.*
import com.google.firebase.ktx.*
import dagger.hilt.android.*
import timber.log.*

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableOrDisableButtonSearch()
        binding.btnRegister.setOnClickListener {
            signUpSetter()

            viewModel.startFakeSignIn(
                binding.tfRegisterEmail.text.toString(),
                binding.tfRegisterPassword.text.toString(),
                binding.tfRegisterName.text.toString()
            )
        }



    }



    private fun signUpSetter() {
        val email = binding.tfRegisterEmail.text.toString()
        val password = binding.tfRegisterPassword.text.toString()
        val name = binding.tfRegisterName.text.toString()

        val instance = Firebase.auth
        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
            instance.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        lifecycleScope.launchWhenStarted {
                            viewModel.updateExistUser(true,
                                emailUser = it.result.user?.email.toString())
                        }
                        //TODO, PARA VERIFICAR SI ES NUEVO USUARIO O INGRESA A UN LUGAR SIN DATOS, QUE DEPENDA INICIALMENTE DEL ONBOARDING PARA QUE DESCARGUE
                        // DATOS, Y USEMOS MEJOR EL CURRENT USER DEL PREFERENCE PARA DEFINIR SI ES UN USUARIO DIFERENTE AL QUE LOS DATOS ESTAN ALMACENADOS, Y CON ESTE NUEVAMENTE HACER LA PETICION DE DATOS
                        //POR AHORA QUE EL IDENTIFICADOR SEA EL EMAIL, LUEGO SE PUEDE HACER PARA COMPARTIR USUARIOS Y CON ESTE SE CREE UN CODIGO MAS BONITO Y NO VER CONSTANTEMENTE EL CORREO
                        //ERROR TAMBIEN Y NOSE SI SOLO DE PRODUCT, PERO CUANDO RELANSO LA APP CON LA SESION ABVIERTO Y MATO LA CUENTA DESDE GOOGLE SIGUE PASANDO NORMAL
                        //viewModel.createUser(email)
                        updateProfile(instance, name)
                    } else {
                        showAlert()
                    }
                }
        }
    }

    private fun updateProfile(instance: FirebaseAuth, userName: String) {
        val user = instance.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = userName
            //photoUri = Uri.parse(profilePhoto)
        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("Update profile success!")
                    goToMainActivity()
                }
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    //EDIT TEXT LISTENER
    private fun enableOrDisableButtonSearch() {
        binding.btnRegister.isEnabled = false
        var email = false
        var pass = false
        var name = false
        var length : Boolean
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
                length = s.toString().length > 6
                binding.btnRegister.isEnabled = email && pass && name && length
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

}