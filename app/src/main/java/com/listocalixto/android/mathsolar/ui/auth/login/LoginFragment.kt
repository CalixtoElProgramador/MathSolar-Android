package com.listocalixto.android.mathsolar.ui.auth.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentLoginBinding
import com.listocalixto.android.mathsolar.presentation.auth.login.LoginViewModel
import com.listocalixto.android.mathsolar.ui.auth.register.Register03Fragment
import com.listocalixto.android.mathsolar.utils.EventObserver
import com.listocalixto.android.mathsolar.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by activityViewModels<LoginViewModel>()

    private var activityNavHost: View? = null

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.loginViewModel = viewModel

        activityNavHost = activity?.findViewById(R.id.nav_host_activity)

        setupNavigation()
        setupSnackbar()

    }

    private fun setupSnackbar() {
        view?.setupSnackbar(
            this.viewLifecycleOwner,
            viewModel.snackbarText,
            Snackbar.LENGTH_INDEFINITE,
            binding.signInGuest
        )
        viewModel.apply {
            errorMessage.observe(viewLifecycleOwner, { errorMessage ->
                errorMessage?.let {
                    showErrorMessage(it)
                }
            })
        }
    }

    private fun setupNavigation() {
        viewModel.apply {
            signInEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToMainParentFragment()
            })
            signUpEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToRegisterParentFragment()
            })
        }
    }

    private fun navigateToMainParentFragment() {
        activityNavHost?.let {
            Navigation.findNavController(it)
                .navigate(R.id.loginParentFragment_to_mainParentFragment)
        }
    }

    private fun navigateToRegisterParentFragment() {
        activityNavHost?.let {
            Navigation.findNavController(it)
                .navigate(R.id.loginParentFragment_to_registerParentFragment)
        }
    }

    companion object {
        const val TAG = "LoginFragment"
    }

}