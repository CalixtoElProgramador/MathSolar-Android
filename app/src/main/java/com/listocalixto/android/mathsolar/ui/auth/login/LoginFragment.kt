package com.listocalixto.android.mathsolar.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentLoginBinding
import com.listocalixto.android.mathsolar.presentation.auth.login.LoginViewModel
import com.listocalixto.android.mathsolar.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by activityViewModels<LoginViewModel>()

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }

        binding = FragmentLoginBinding.bind(view)
        binding.run {
            lifecycleOwner = this@LoginFragment.viewLifecycleOwner
            loginViewModel = viewModel
        }

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

    companion object {
        const val TAG = "LoginFragment"
    }

}