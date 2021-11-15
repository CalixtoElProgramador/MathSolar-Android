package com.listocalixto.android.mathsolar.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

}