package com.listocalixto.android.mathsolar.bindings.main.projects.addedit_project

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import com.listocalixto.android.mathsolar.utils.PVProjectType

@BindingAdapter("projectTypeSelected")
fun RadioGroup.onProjectTypeSelected(viewModel: AddEditProjectViewModel) {
    setOnCheckedChangeListener { group, checkedId ->
        when(checkedId) {
            R.id.radioBtn_withoutBatteries -> {
                viewModel.setProjectTypeSelected(PVProjectType.WITHOUT_BATTERIES)
            }
            R.id.radioBtn_withBatteries -> {
                viewModel.setProjectTypeSelected(PVProjectType.WITH_BATTERIES)
            }
        }
    }
}