package com.example.everypractice.start.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.everypractice.R
import com.example.everypractice.databinding.FragmentLoginBinding
import com.example.everypractice.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //FULL BIND

        binding.btnRegister.setOnClickListener { goToStart() }

    }

    private fun goToStart() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToStartFragment())
    }


}