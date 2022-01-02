package com.listocalixto.android.mathsolar.bindings.main.projects.addedit_project

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import com.listocalixto.android.mathsolar.ui.main.projects.addedit_project.fragments.adapter.AddEditProjectAdapter02
import com.listocalixto.android.mathsolar.utils.PVProjectType
import com.listocalixto.android.mathsolar.utils.PeriodConsumptionType
import com.listocalixto.android.mathsolar.utils.RateType

@BindingAdapter(
    "projectTypeSelected"
)
fun RadioGroup.onProjectTypeSelected(viewModel: AddEditProjectViewModel) {
    setOnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.radioBtn_withoutBatteries -> {
                viewModel.setProjectTypeSelected(PVProjectType.WITHOUT_BATTERIES)
            }
            else -> {
                viewModel.setProjectTypeSelected(PVProjectType.WITH_BATTERIES)
            }
        }
    }
    viewModel.projectTypeSelected.value?.let {
        when (it) {
            PVProjectType.ALL_PROJECTS -> {
            }
            PVProjectType.WITHOUT_BATTERIES -> {
                check(R.id.radioBtn_withoutBatteries)
            }
            PVProjectType.ISOLATED -> {
            }
            PVProjectType.WITH_BATTERIES -> {
                check(R.id.radioBtn_withBatteries)
            }
            PVProjectType.FAVORITE -> {
            }
        }
    }
}

@BindingAdapter(
    "setupSpinner",
    "saveRateType",
    requireAll = false
)
fun TextInputLayout.setupSpinner(rateType: RateType?, viewModel: AddEditProjectViewModel) {
    when (id) {
        R.id.inputLayout_rateType -> {
            val ratesTypes = resources.getStringArray(R.array.rate_type_options)
            val layout = R.layout.support_simple_spinner_dropdown_item
            val adapter = ArrayAdapter(context, layout, ratesTypes)
            val spinner = this.editText as AutoCompleteTextView
            spinner.setAdapter(adapter)
            rateType?.let { spinner.setText(ratesTypes[it.ordinal], false) }
            spinner.setOnItemClickListener { _, _, position, _ ->
                viewModel.setRateType(position)
                viewModel.setCurrentFragment(R.id.addEditProjectFragment01)
            }
        }
        R.id.inputLayout_rate -> {
            rateType?.let {
                val rates = resources.getStringArray(it.ratesIdResource)
                val layout = R.layout.support_simple_spinner_dropdown_item
                val adapter = ArrayAdapter(context, layout, rates)
                val spinner = this.editText as AutoCompleteTextView
                spinner.setAdapter(adapter)
                spinner.setText(rates[it.rateSelected], false)
                spinner.setOnItemClickListener { _, _, position, _ ->
                    viewModel.setRate(rateType.ordinal, position)
                }
            }
        }
    }
}

@BindingAdapter(
    "paymentItems"
)
fun RecyclerView.setPaymentItems(items: List<Double>?) {
    items?.let {
        val adapter = (adapter as AddEditProjectAdapter02)
        adapter.submitList(it)
    }
}

@BindingAdapter(
    "periodTypeConsumptionSelected"
)
fun RadioGroup.onPeriodTypeConsumptionSelected(viewModel: AddEditProjectViewModel) {
    setOnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.radioBtn_monthly -> {
                viewModel.setPeriodConsumption(PeriodConsumptionType.MONTHLY)
                viewModel.setCurrentFragment(R.id.addEditProjectFragment02)
            }
            else -> {
                viewModel.setPeriodConsumption(PeriodConsumptionType.BIMONTHLY)
                viewModel.setCurrentFragment(R.id.addEditProjectFragment02)
            }
        }
    }
    viewModel.periodConsumptionType.value?.let {
        when (it) {
            PeriodConsumptionType.MONTHLY -> {
                check(R.id.radioBtn_monthly)
            }
            PeriodConsumptionType.BIMONTHLY -> {
                check(R.id.radioBtn_bimonthly)
            }
        }
    }
}

