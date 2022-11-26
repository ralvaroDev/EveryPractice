package com.example.everypractice.start.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.everypractice.consval.USER_IMAGE
import com.example.everypractice.consval.USER_NAME
import com.example.everypractice.databinding.FragmentStartFagmentBinding
import com.example.everypractice.prinoptions.movies.ui.MoviesMainActivity
import com.example.everypractice.prinoptions.search.SearchActivity


class StartFragment : Fragment() {

    private var _binding : FragmentStartFagmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStartFagmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSearch.setOnClickListener {
            val content = context
            val intent = Intent(context,SearchActivity::class.java)
            content?.startActivity(intent)
            //es necesario tanto context?
        }

        binding.btnCartelera.setOnClickListener {
            val content = context
            val intent = Intent(context,MoviesMainActivity::class.java)
            content?.startActivity(intent)
        }



        binding.apply {
            btnCartelera.isEnabled = true
            btnTimer.isEnabled = false
            prox.isEnabled = false
            tvUserName.text = USER_NAME
            logo.setImageResource(USER_IMAGE)
        }

    }



}