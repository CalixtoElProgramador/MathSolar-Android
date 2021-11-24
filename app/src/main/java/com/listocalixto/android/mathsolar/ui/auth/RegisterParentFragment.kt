package com.listocalixto.android.mathsolar.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.ParentFragmentRegisterBinding
import com.listocalixto.android.mathsolar.presentation.auth.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterParentFragment : Fragment(R.layout.parent_fragment_register) {

    private val viewModel by activityViewModels<RegisterViewModel>()

    private lateinit var binding: ParentFragmentRegisterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ParentFragmentRegisterBinding.bind(view).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.registerViewModel = viewModel
        }
    }

}