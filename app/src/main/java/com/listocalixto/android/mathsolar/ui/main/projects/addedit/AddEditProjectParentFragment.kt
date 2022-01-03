package com.listocalixto.android.mathsolar.ui.main.projects.addedit

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.ParentFragmentAddeditProjectBinding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditProjectParentFragment : Fragment(R.layout.parent_fragment_addedit_project) {

    private val viewModel by activityViewModels<AddEditProjectViewModel>()
    private val currentNavigationFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.nav_host_addedit_project)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    private lateinit var binding: ParentFragmentAddeditProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ParentFragmentAddeditProjectBinding.inflate(inflater, container, false)
        binding.run {
            lifecycleOwner = this@AddEditProjectParentFragment.viewLifecycleOwner
            addEditProjectViewModel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.run {
            enterTransition = MaterialContainerTransform().apply {
                startView = requireActivity().findViewById(R.id.fab_main)
                endView = cardViewAddEditProject
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                scrimColor = Color.TRANSPARENT
                containerColor = MaterialColors.getColor(requireContext(), R.attr.colorSurface, Color.MAGENTA)
                startContainerColor = MaterialColors.getColor(requireContext(), R.attr.colorSecondary, Color.MAGENTA)
                endContainerColor = MaterialColors.getColor(requireContext(), R.attr.colorSurface, Color.MAGENTA)
            }
            returnTransition = Slide().apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_medium).toLong()
                addTarget(R.id.cardView_addEditProject)
            }
        }

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_addedit_project) as NavHostFragment
        val navController = navHostFragment.navController
        setupDestinationChangeListener(navController)

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.run {

            backEvent.observe(viewLifecycleOwner, EventObserver {
                activity?.onBackPressed()
            })

            onCancelEvent.observe(viewLifecycleOwner, EventObserver{
                navigateToProjectsFragment()
            })

        }
    }

    private fun applyExitMotionTransition() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    private fun navigateToProjectsFragment() {
        activity?.let {
            val mainNavController = it.findViewById<View>(R.id.nav_host_main)
            Navigation.findNavController(mainNavController).popBackStack()
        }

    }

    private fun setupDestinationChangeListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.setCurrentFragment(destination.id)
        }
    }
}