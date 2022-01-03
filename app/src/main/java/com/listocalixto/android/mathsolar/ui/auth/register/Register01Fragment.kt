package com.listocalixto.android.mathsolar.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
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
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }

        binding = FragmentRegister01Binding.bind(view)
        binding.run {
            lifecycleOwner = this@Register01Fragment.viewLifecycleOwner
            registerViewModel = viewModel
        }

        viewModel.setCurrentFragment(R.id.register01Fragment)

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.apply {
            nextEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToRegister02Fragment()
            })
        }
    }

    private fun navigateToRegister02Fragment() {
        applyExitMotionTransition()
        val direction = Register01FragmentDirections.register01FragmentToRegister02Fragment()
        findNavController().navigate(direction)
    }

    private fun applyExitMotionTransition() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    companion object {
        const val TAG = "Register01Fragment"
    }

}