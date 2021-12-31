package com.listocalixto.android.mathsolar.ui.main.projects.add_edit_project

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.transition.Slide
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentAddEditProject00Binding

class AddEditProjectFragment00 : Fragment(R.layout.fragment_add_edit_project_00) {

    private lateinit var binding: FragmentAddEditProject00Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditProject00Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.run {
            enterTransition = MaterialContainerTransform().apply {
                startView = requireActivity().findViewById(R.id.fab_main)
                endView = cardViewAddEditProject00
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                scrimColor = Color.TRANSPARENT
                containerColor = MaterialColors.getColor(requireContext(), R.attr.colorSurface, Color.MAGENTA)
                startContainerColor = MaterialColors.getColor(requireContext(), R.attr.colorSecondary, Color.MAGENTA)
                endContainerColor = MaterialColors.getColor(requireContext(), R.attr.colorSurface, Color.MAGENTA)
            }
            returnTransition = Slide().apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_medium).toLong()
                addTarget(R.id.cardView_addEditProject_00)
            }
        }



    }

}