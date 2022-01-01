package com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project

import androidx.lifecycle.*
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.domain.pv_project.PVProjectRepo
import com.listocalixto.android.mathsolar.utils.Event
import com.listocalixto.android.mathsolar.utils.PVProjectType
import com.listocalixto.android.mathsolar.utils.RateType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditProjectViewModel @Inject constructor(
    private val repo: PVProjectRepo
) : ViewModel() {

    private val _projectTypeSelected = MutableLiveData<PVProjectType>()
    val projectTypeSelected: LiveData<PVProjectType> = _projectTypeSelected

    private val _onCancelEvent = MutableLiveData<Event<Unit>>()
    val onCancelEvent: LiveData<Event<Unit>> = _onCancelEvent

    private val currentFragment = MutableLiveData(R.id.addEditProjectFragment00)

    private val _backEvent = MutableLiveData<Event<Unit>>()
    val backEvent: LiveData<Event<Unit>> = _backEvent

    private val _nextEvent = MutableLiveData<Event<Unit>>()
    val nextEvent: LiveData<Event<Unit>> = _nextEvent

    private val _rateTypeSelected = MutableLiveData<RateType>()
    val rateTypeSelected: LiveData<RateType> = _rateTypeSelected

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
                _rateTypeSelected.value == null
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
        _rateTypeSelected.value = RateType.values()[position]
    }

    fun setRate(rateType: Int, position: Int) {
        RateType.values()[rateType].rateSelected = position
        _rateTypeSelected.value = RateType.values()[rateType]
    }

}