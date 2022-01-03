package com.listocalixto.android.mathsolar.ui.main.projects.addedit.section_04

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentAddeditProject04Binding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver

class AddEditProjectFragment04 : Fragment(R.layout.fragment_addedit_project_04) {

    private val viewModel by activityViewModels<AddEditProjectViewModel>()

    private lateinit var parentNavHost: View
    private lateinit var binding: FragmentAddeditProject04Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEnterMotionTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
        binding = FragmentAddeditProject04Binding.bind(view)
        binding.run {
            lifecycleOwner = this@AddEditProjectFragment04.viewLifecycleOwner
            addEditProjectViewModel = viewModel
            activity?.let { parentNavHost = it.findViewById(R.id.nav_host_main) }
        }

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.run {
            nextEvent.observe(viewLifecycleOwner, EventObserver {

            })

            openMapEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToAddEditProjectMapsFragment04()
            })
        }
    }

    private fun navigateToAddEditProjectMapsFragment04() {
        applyExitMotionTransition()
        val direction = AddEditProjectFragment04Directions.actionAddEditProjectFragment04ToAddEditProjectMapsFragment04()
        findNavController().navigate(direction)

    }

    private fun applyExitMotionTransition() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    private fun applyEnterMotionTransition() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

}