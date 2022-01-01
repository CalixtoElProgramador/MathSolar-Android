package com.listocalixto.android.mathsolar.ui.main.projects.addedit_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentAddEditProject01Binding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditProjectFragment01 : Fragment(R.layout.fragment_add_edit_project_01) {

    private val viewModel by activityViewModels<AddEditProjectViewModel>()

    private lateinit var binding: FragmentAddEditProject01Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEnterMotionTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }

        binding = FragmentAddEditProject01Binding.bind(view)
        binding.run {
            lifecycleOwner = this@AddEditProjectFragment01.viewLifecycleOwner
            addEditProjectViewModel = viewModel

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

    companion object {
        private const val TAG = "AddEditProjectFragment01"
    }

}