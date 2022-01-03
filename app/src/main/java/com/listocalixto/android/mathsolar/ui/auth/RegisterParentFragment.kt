package com.listocalixto.android.mathsolar.ui.auth

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.ParentFragmentRegisterBinding
import com.listocalixto.android.mathsolar.presentation.auth.register.RegisterViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterParentFragment : Fragment(R.layout.parent_fragment_register) {

    private val viewModel by activityViewModels<RegisterViewModel>()

    private lateinit var binding: ParentFragmentRegisterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }

        binding = ParentFragmentRegisterBinding.bind(view)
        binding.run {
            lifecycleOwner = this@RegisterParentFragment.viewLifecycleOwner
            registerViewModel = viewModel

            enterTransition = MaterialContainerTransform().apply {
                startView = requireActivity().findViewById(R.id.button_singUp)
                endView = cardViewRegister
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                scrimColor = Color.TRANSPARENT
                setAllContainerColors(MaterialColors.getColor(requireContext(), R.attr.colorSurface, Color.MAGENTA))
            }
            returnTransition = Slide().apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_medium).toLong()
                addTarget(R.id.cardView_register)
            }
        }

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.run {
            backEvent.observe(viewLifecycleOwner, EventObserver {
                activity?.onBackPressed()
            })

            cancelEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToLoginParentFragment()
            })

            successfullyUserCreatedEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToMainParentFragment()
            })
        }
    }

    private fun navigateToMainParentFragment() {
        applyExitMotionTransition()
        val direction = RegisterParentFragmentDirections.registerParentFragmentToMainParentFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToLoginParentFragment() {
        activity?.let {
            val activityNavController = it.findViewById<View>(R.id.nav_host_activity)
            Navigation.findNavController(activityNavController).popBackStack()
        }
    }

    private fun applyExitMotionTransition() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }
}