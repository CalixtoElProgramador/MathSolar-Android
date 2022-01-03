package com.listocalixto.android.mathsolar.ui.auth

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.FirebaseAuth
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.ParentFragmentLoginBinding
import com.listocalixto.android.mathsolar.presentation.auth.AuthViewModel
import com.listocalixto.android.mathsolar.presentation.auth.login.LoginViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginParentFragment : Fragment(R.layout.parent_fragment_login) {

    private val viewModel by activityViewModels<LoginViewModel>()

    private lateinit var binding: ParentFragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAuth.getInstance().currentUser?.let {
            navigateToMainParentFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }

        binding = ParentFragmentLoginBinding.bind(view)
        binding.run {
            lifecycleOwner = this@LoginParentFragment.viewLifecycleOwner
            loginViewModel = viewModel
        }

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.run {
            signInEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToMainParentFragment()
            })

            signUpEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToRegisterParentFragment()
            })
        }
    }

    private fun navigateToMainParentFragment() {
        applyExitMotionTransition()
        val direction = LoginParentFragmentDirections.loginParentFragmentToMainParentFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToRegisterParentFragment() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        val direction = LoginParentFragmentDirections.loginParentFragmentToRegisterParentFragment()
        findNavController().navigate(direction)
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