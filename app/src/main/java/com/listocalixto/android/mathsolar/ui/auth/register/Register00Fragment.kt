package com.listocalixto.android.mathsolar.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentRegister00Binding
import com.listocalixto.android.mathsolar.presentation.auth.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register00Fragment : Fragment(R.layout.fragment_register_00) {

    private val viewModel by activityViewModels<RegisterViewModel>()

    private lateinit var binding: FragmentRegister00Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }

        binding = FragmentRegister00Binding.bind(view)
        binding.run {
            lifecycleOwner = this@Register00Fragment.viewLifecycleOwner
            registerViewModel = viewModel
        }

    }

}