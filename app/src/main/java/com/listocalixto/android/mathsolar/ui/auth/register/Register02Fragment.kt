package com.listocalixto.android.mathsolar.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentRegister02Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register02Fragment : Fragment(R.layout.fragment_register_02) {

    private lateinit var binding: FragmentRegister02Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegister02Binding.bind(view)
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

}