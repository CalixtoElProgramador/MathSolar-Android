package com.listocalixto.android.mathsolar.ui.main.projects.addedit.section_00

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentAddeditProject00Binding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditProjectFragment00 : Fragment(R.layout.fragment_addedit_project_00) {

    private val viewModel by activityViewModels<AddEditProjectViewModel>()

    private lateinit var binding: FragmentAddeditProject00Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
        
        binding = FragmentAddeditProject00Binding.bind(view)
        binding.run {
            lifecycleOwner = this@AddEditProjectFragment00.viewLifecycleOwner
            addEditProjectViewModel = viewModel
        }

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.run {
            nextEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToAddEditFragment01()
            })
        }
    }

    private fun navigateToAddEditFragment01() {
        applyExitMotionTransition()
        val direction = AddEditProjectFragment00Directions.actionAddEditProjectFragment00ToAddEditProjectFragment01()
        findNavController().navigate(direction)

    }

    private fun applyExitMotionTransition() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    companion object {
        private const val TAG = "AddEditProjectFragment00"
    }

}