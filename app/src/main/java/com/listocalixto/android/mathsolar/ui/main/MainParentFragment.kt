package com.listocalixto.android.mathsolar.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.ParentFragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainParentFragment : Fragment(R.layout.parent_fragment_main) {

    private lateinit var binding: ParentFragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ParentFragmentMainBinding.bind(view)
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

}