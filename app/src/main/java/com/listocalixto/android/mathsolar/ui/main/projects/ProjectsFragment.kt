package com.listocalixto.android.mathsolar.ui.main.projects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentProjectsBinding
import com.listocalixto.android.mathsolar.presentation.projects.ProjectsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private val viewModel by activityViewModels<ProjectsViewModel>()

    private lateinit var binding: FragmentProjectsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectsBinding.bind(view)
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.viewmodel = viewModel
    }

}