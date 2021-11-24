package com.listocalixto.android.mathsolar.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentHomeBinding
import com.listocalixto.android.mathsolar.presentation.main.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by activityViewModels<HomeViewModel>()

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view).also {
            it.lifecycleOwner = this.viewLifecycleOwner
        }

        viewModel.initViewModel.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(context, "viewModel init", Toast.LENGTH_SHORT).show()
            }
        })

    }
}