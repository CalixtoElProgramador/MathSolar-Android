package com.listocalixto.android.mathsolar.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentRegister01Binding
import com.listocalixto.android.mathsolar.presentation.auth.register.RegisterViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register01Fragment : Fragment(R.layout.fragment_register_01) {

    private val viewModel by activityViewModels<RegisterViewModel>()
    private lateinit var binding: FragmentRegister01Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegister01Binding.bind(view).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.registerViewModel = viewModel
        }

        viewModel.setCurrentFragment(R.id.register01Fragment)

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.apply {
            backEvent.observe(viewLifecycleOwner, EventObserver {
                activity?.onBackPressed()
            })

            nextEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToRegister02Fragment()
            })
        }
    }

    private fun navigateToRegister02Fragment() {
        findNavController().navigate(R.id.register01Fragment_to_register02Fragment)
    }

    companion object {
        const val TAG = "Register01Fragment"
    }

}