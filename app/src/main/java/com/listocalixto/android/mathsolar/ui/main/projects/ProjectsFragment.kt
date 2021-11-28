package com.listocalixto.android.mathsolar.ui.main.projects

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentProjectsBinding
import com.listocalixto.android.mathsolar.presentation.main.projects.ProjectsViewModel
import com.listocalixto.android.mathsolar.ui.main.projects.adapter.ProjectsAdapter
import com.listocalixto.android.mathsolar.utils.hideOrShowBottomAppBarOnRecyclerScrolled
import com.listocalixto.android.mathsolar.utils.onMenuItemSelected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private val viewModel by activityViewModels<ProjectsViewModel>()

    private lateinit var binding: FragmentProjectsBinding
    private lateinit var listAdapter: ProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectsBinding.bind(view).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.projectsViewModel = viewModel
            setupListAdapter(it)
        }

        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar).also {
            binding.listProjects.hideOrShowBottomAppBarOnRecyclerScrolled(it)
            it?.onMenuItemSelected(viewModel)
        }


    }

    private fun setupListAdapter(binding: FragmentProjectsBinding) {
        val viewModel = binding.projectsViewModel
        viewModel?.let {
            listAdapter = ProjectsAdapter(it)
            binding.listProjects.adapter = listAdapter
        } ?: Log.d(
            TAG,
            "setupListAdapter: ViewModel not initialized when attempting to set up adapter."
        )
    }

    companion object {
        const val TAG = "ProjectsFragment"
    }
}