package com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.domain.pv_project.PVProjectRepo
import com.listocalixto.android.mathsolar.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditProjectViewModel @Inject constructor(
    private val repo: PVProjectRepo
) : ViewModel() {

    private val listPayment = mutableListOf<Double?>(null)

    private val _payments = MutableLiveData(listPayment)
    val payments: LiveData<MutableList<Double?>> = _payments

    private val _projectTypeSelected = MutableLiveData<PVProjectType>()
    val projectTypeSelected: LiveData<PVProjectType> = _projectTypeSelected

    private val _onCancelEvent = MutableLiveData<Event<Unit>>()
    val onCancelEvent: LiveData<Event<Unit>> = _onCancelEvent

    private val currentFragment = MutableLiveData(R.id.addEditProjectFragment00)

    private val _backEvent = MutableLiveData<Event<Unit>>()
    val backEvent: LiveData<Event<Unit>> = _backEvent

    private val _nextEvent = MutableLiveData<Event<Unit>>()
    val nextEvent: LiveData<Event<Unit>> = _nextEvent

    private val _snackbarText = MutableLiveData<Event<SnackbarMessage>>()
    val snackbarText: LiveData<Event<SnackbarMessage>> = _snackbarText

    private val _rateType = MutableLiveData<RateType>()
    val rateType: LiveData<RateType> = _rateType

    private val _periodConsumptionType = MutableLiveData<PeriodConsumptionType>()
    val periodConsumptionType: LiveData<PeriodConsumptionType> = _periodConsumptionType

    val wasSelectedAnOption: LiveData<Boolean> = Transformations.map(_projectTypeSelected) {
        it == PVProjectType.WITHOUT_BATTERIES || it == PVProjectType.WITH_BATTERIES
    }

    val showBackButton: LiveData<Boolean> = Transformations.map(currentFragment) {
        it != R.id.addEditProjectFragment00
    }

    val disableNextBtn: LiveData<Boolean> = Transformations.map(currentFragment) {
        when (it) {
            R.id.addEditProjectFragment00 -> {
                false
            }
            R.id.addEditProjectFragment01 -> {
                _rateType.value == null
            }
            R.id.addEditProjectFragment02 -> {
                val periodType = _periodConsumptionType.value
                val consumptions = _payments.value!!
                periodType?.let { period ->
                    when (period) {
                        PeriodConsumptionType.MONTHLY -> {
                            (consumptions.size - 1) < period.minMonths
                        }
                        PeriodConsumptionType.BIMONTHLY -> {
                            (consumptions.size - 1) < period.minMonths
                        }
                    }
                } ?: true
            }
            else -> {
                false
            }
        }
    }

    fun onCancelPressed() {
        _onCancelEvent.value = Event(Unit)
    }

    fun setProjectTypeSelected(type: PVProjectType) {
        _projectTypeSelected.value = type
    }

    fun onBack() {
        _backEvent.value = Event(Unit)
    }

    fun onNext() {
        _nextEvent.value = Event(Unit)
    }

    fun setCurrentFragment(fragmentId: Int) {
        currentFragment.value = fragmentId
    }

    fun setRateType(position: Int) {
        _rateType.value = RateType.values()[position]
    }

    fun setRate(rateType: Int, position: Int) {
        RateType.values()[rateType].rateSelected = position
        _rateType.value = RateType.values()[rateType]
    }

    fun onAddPayment(value: String) {
        listPayment.add(value.toDouble())
        _payments.value = listPayment
    }

    fun onUpdatePayment(position: Int, paymentUpdated: String) {
        listPayment[position] = paymentUpdated.toDouble()
        _payments.value = listPayment
    }

    fun onDeleteConsumption(position: Int) {
        listPayment.removeAt(position)
        _payments.value = listPayment
    }

    fun setPeriodConsumption(type: PeriodConsumptionType) {
        _periodConsumptionType.value = type
    }

    private fun showSnackbarErrorMessage(
        @StringRes message: Int,
        type: SnackbarType = SnackbarType.DEFAULT
    ) {
        _snackbarText.value = Event(SnackbarMessage(message, type, true))
    }

    companion object {
        private const val TAG = "AddEditProjectViewModel"
    }

}