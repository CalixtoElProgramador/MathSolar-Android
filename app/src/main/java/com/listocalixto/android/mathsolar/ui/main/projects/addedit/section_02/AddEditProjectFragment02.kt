package com.listocalixto.android.mathsolar.ui.main.projects.addedit.section_02

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
import com.listocalixto.android.mathsolar.databinding.FragmentAddeditProject02Binding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import com.listocalixto.android.mathsolar.ui.main.projects.addedit.section_02.adapter.AddEditProjectAdapter02
import com.listocalixto.android.mathsolar.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditProjectFragment02 : Fragment(R.layout.fragment_addedit_project_02) {

    private val viewModel by activityViewModels<AddEditProjectViewModel>()

    private lateinit var binding: FragmentAddeditProject02Binding
    private lateinit var listAdapter: AddEditProjectAdapter02

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEnterMotionTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
        binding = FragmentAddeditProject02Binding.bind(view)
        binding.run {
            lifecycleOwner = this@AddEditProjectFragment02.viewLifecycleOwner
            addEditProjectViewModel = viewModel
            setupListAdapter(this)
        }

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.run {
            nextEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToAddEditProjectFragment03()
            })
        }
    }

    private fun navigateToAddEditProjectFragment03() {
        applyExitMotionTransition()
        val direction = AddEditProjectFragment02Directions.actionAddEditProjectFragment02ToAddEditProjectFragment03()
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

    private fun setupListAdapter(binding: FragmentAddeditProject02Binding) {
        val viewModel = binding.addEditProjectViewModel
        viewModel?.let { vm ->
            context?.let { c ->
                listAdapter = AddEditProjectAdapter02(vm, c, viewLifecycleOwner)
                binding.listPayments.adapter = listAdapter
            } ?: Log.d(TAG, "setupListAdapter: Context not founded")
        } ?: Log.d(
            TAG,
            "setupListAdapter: ViewModel not initialized when attempting to set up adapter."
        )
    }

    private fun applyEnterMotionTransition() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    companion object {
        private const val TAG = "AddEditProjectFragment02"
    }

}