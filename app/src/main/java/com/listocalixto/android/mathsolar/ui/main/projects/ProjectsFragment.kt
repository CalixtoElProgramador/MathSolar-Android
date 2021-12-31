package com.listocalixto.android.mathsolar.ui.main.projects

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentProjectsBinding
import com.listocalixto.android.mathsolar.presentation.main.projects.ProjectsViewModel
import com.listocalixto.android.mathsolar.ui.main.projects.adapter.ProjectsAdapter
import com.listocalixto.android.mathsolar.utils.EventObserver
import com.listocalixto.android.mathsolar.utils.hideOrShowBottomAppBarOnRecyclerScrolled
import com.listocalixto.android.mathsolar.utils.onMenuItemSelected
import com.listocalixto.android.mathsolar.utils.setFunctionOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private val viewModel by activityViewModels<ProjectsViewModel>()

    private lateinit var binding: FragmentProjectsBinding
    private lateinit var listAdapter: ProjectsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        setupFab()

        binding = FragmentProjectsBinding.bind(view).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.projectsViewModel = viewModel
            setupListAdapter(it)
        }

        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar).also {
            binding.listProjects.hideOrShowBottomAppBarOnRecyclerScrolled(it)
            it?.onMenuItemSelected(viewModel)
        }

        binding.toolbarProjects.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_search -> { navigateToSearchProjectFragment(); true }
                else -> {false}
            }
        }

        setupNavigation()

    }

    private fun navigateToSearchProjectFragment() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        val action = ProjectsFragmentDirections.actionProjectsFragmentToSearchProjectFragment()
        findNavController().navigate(action)
    }

    private fun setupNavigation() {
        viewModel.apply {
            newProjectEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToAddEditProjectFragment00()
            })
        }
    }

    private fun navigateToAddEditProjectFragment00() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        activity?.let { activity ->
            activity.findViewById<FloatingActionButton>(R.id.fab_main)?.apply { hide() }
            activity.findViewById<BottomAppBar>(R.id.bottomAppBar)?.apply { performHide() }
            val activityNavHost = activity.findViewById<View>(R.id.nav_host_activity)
            val action = R.id.action_mainParentFragment_to_addEditProjectParentFragment
            Navigation.findNavController(activityNavHost).navigate(action)
        }

    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab_main)?.apply {
            setFunctionOnClick(viewModel)
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