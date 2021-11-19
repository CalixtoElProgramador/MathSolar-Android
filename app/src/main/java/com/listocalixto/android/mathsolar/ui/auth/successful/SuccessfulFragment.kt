package com.listocalixto.android.mathsolar.ui.auth.successful

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentSuccessfulBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessfulFragment : Fragment(R.layout.fragment_successful) {

    private lateinit var binding: FragmentSuccessfulBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSuccessfulBinding.bind(view)
        binding.lifecycleOwner = this.viewLifecycleOwner

    }

}