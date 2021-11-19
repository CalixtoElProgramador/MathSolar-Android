package com.listocalixto.android.mathsolar.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentLoginBinding
import com.listocalixto.android.mathsolar.presentation.auth.login.LoginViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import com.listocalixto.android.mathsolar.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by activityViewModels<LoginViewModel>()

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.loginViewModel = viewModel

        setupNavigation()
        setupSnackbar()

    }

    private fun setupSnackbar() {
        view?.setupSnackbar(
            this.viewLifecycleOwner,
            viewModel.snackbarText,
            Snackbar.LENGTH_INDEFINITE,
            binding.signInGuest,
            true
        )
        viewModel.errorMessage.observe(viewLifecycleOwner, { message ->
            message?.let {
                viewModel.showEditResultMessage(it)
            }
        })
    }

    private fun setupNavigation() {
        viewModel.signInEvent.observe(viewLifecycleOwner, EventObserver {
            navigateToMainParentFragment()
        })
    }

    private fun navigateToMainParentFragment() {
        val activityNavHost = activity?.findViewById<View>(R.id.nav_host_activity)
        activityNavHost?.let {
            Navigation.findNavController(it)
                .navigate(R.id.loginParentFragment_to_mainParentFragment)
        }
    }

}