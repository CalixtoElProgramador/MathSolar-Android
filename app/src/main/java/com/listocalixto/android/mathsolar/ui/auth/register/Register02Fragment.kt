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
import com.listocalixto.android.mathsolar.databinding.FragmentRegister02Binding
import com.listocalixto.android.mathsolar.presentation.auth.register.RegisterViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register02Fragment : Fragment(R.layout.fragment_register_02) {

    private val viewModel by activityViewModels<RegisterViewModel>()

    private lateinit var binding: FragmentRegister02Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEnterMotionTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }

        binding = FragmentRegister02Binding.bind(view)
        binding.run {
            lifecycleOwner = this@Register02Fragment.viewLifecycleOwner
            registerViewModel = viewModel
        }

        viewModel.setCurrentFragment(R.id.register02Fragment)

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.apply {

            nextEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToRegister03Fragment()
            })

        }
    }


    private fun navigateToRegister03Fragment() {
        applyExitMotionTransition()
        val direction = Register02FragmentDirections.register02FragmentToRegister03Fragment()
        findNavController().navigate(direction)
    }

    private fun applyEnterMotionTransition() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    private fun applyExitMotionTransition() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

}