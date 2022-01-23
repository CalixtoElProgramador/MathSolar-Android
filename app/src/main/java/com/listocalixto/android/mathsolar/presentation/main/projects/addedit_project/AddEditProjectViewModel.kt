package com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project

import android.util.Log
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.PointOfInterest
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.MainDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.nrel.solar_resource.InputsSolarResource
import com.listocalixto.android.mathsolar.domain.DataStoreRepository
import com.listocalixto.android.mathsolar.domain.nrel.solar_resource.SolarResourceRepo
import com.listocalixto.android.mathsolar.domain.pv_project.PVProjectRepo
import com.listocalixto.android.mathsolar.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class AddEditProjectViewModel @Inject constructor(
    private val repo: PVProjectRepo,
    private val repoSolarResource: SolarResourceRepo,
    private val dataStoreRepository: DataStoreRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
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

    private val _openMapEvent = MutableLiveData<Event<Unit>>()
    val openMapEvent: LiveData<Event<Unit>> = _openMapEvent

    private val _average = MutableLiveData<Double>()
    val average: LiveData<Double> = _average

    private val _saving = MutableLiveData<Double>()
    val saving: LiveData<Double> = _saving

    private val _percentage = MutableLiveData(30.0f)
    val percentage: LiveData<Float> = _percentage

    private val _snackbarText = MutableLiveData<Event<SnackbarMessage>>()
    val snackbarText: LiveData<Event<SnackbarMessage>> = _snackbarText

    private val _rateType = MutableLiveData<RateType>()
    val rateType: LiveData<RateType> = _rateType

    private val _periodConsumptionType = MutableLiveData<PeriodConsumptionType>()
    val periodConsumptionType: LiveData<PeriodConsumptionType> = _periodConsumptionType

    private val _helpEvent = MutableLiveData<Event<Unit>>()
    val helpEvent: LiveData<Event<Unit>> = _helpEvent

    private val _moreEvent = MutableLiveData<Event<Unit>>()
    val moreEvent: LiveData<Event<Unit>> = _moreEvent

    private val _myLocationEvent = MutableLiveData<Event<Unit>>()
    val myLocationEvent: LiveData<Event<Unit>> = _myLocationEvent

    private val _poiSelected = MutableLiveData<PointOfInterest?>(null)
    val poiSelected: LiveData<PointOfInterest?> = _poiSelected

    private val _mapType = MutableLiveData<@IntegerRes Int>(R.id.normal_map)
    val mapType: LiveData<Int> = _mapType

    var poiSaved: PointOfInterest? = null

    var mapCameraPosition: CameraPosition? = null

    val locationName = MutableLiveData<String>()

    val namePoiSaved = MutableLiveData<String>()

    val showCancelButton: LiveData<Boolean> = Transformations.map(currentFragment) {
        it != R.id.addEditProjectMapsFragment04
    }

    val isLocationEnable: LiveData<Boolean> =
        dataStoreRepository.readIsLocationPermissionEnabled.asLiveData()

    val showNextButton: LiveData<Boolean> = Transformations.map(currentFragment) {
        when (it) {
            R.id.addEditProjectFragment00 -> {
                val projectType = _projectTypeSelected.value
                projectType == PVProjectType.WITH_BATTERIES || projectType == PVProjectType.WITHOUT_BATTERIES
            }
            R.id.addEditProjectMapsFragment04 -> {
                false
            }
            else -> true
        }
    }

    val enableSavePoiBtn: LiveData<Boolean> = Transformations.map(_poiSelected) {
        it != null
    }

    val showBackButton: LiveData<Boolean> = Transformations.map(currentFragment) {
        it != R.id.addEditProjectFragment00 && it != R.id.addEditProjectMapsFragment04
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
            R.id.addEditProjectFragment03 -> {
                false
            }
            R.id.addEditProjectFragment04 -> {
                poiSaved == null || locationName.value.isNullOrEmpty()
            }
            else -> {
                true
            }
        }
    }

    init {
        viewModelScope.launch(viewModelScope.coroutineContext + mainDispatcher) {
            val inputs = InputsSolarResource(latitude = 21.0, longitude = -89.0)
            val resource = repoSolarResource.getOutputs(inputs)
            if (resource is Resource.Success) {
                Log.d(TAG, "Outputs: ${resource.data.avgGhi}")
            }
            if (resource is Resource.Error) {
                Log.d(TAG, "Error message: ${resource.errorMessage.message}")
                Log.d(TAG, "Error exception: ${resource.errorMessage.exception}")
                Log.d(TAG, "Error stringRes: ${resource.errorMessage.stringRes.toString()}")
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

    fun calculateAverageConsumption() {
        val consumptions = listPayment.subList(1, listPayment.size)
        var summary = 0.0
        consumptions.forEach { value ->
            value?.let {
                summary += it
            }
        }
        val result = summary / consumptions.size
        _average.value = result
    }

    fun setPercentage(value: Float) {
        _percentage.value = value
    }

    fun calculateSaving() {
        val mPercentage = _percentage.value?.div(100)
        val mAverage = _average.value
        mPercentage?.let { p ->
            mAverage?.let { a ->
                _saving.value = (a * p)
            }
        }
    }

    fun onOpenMapClick() {
        _openMapEvent.value = Event(Unit)
    }

    private fun showSnackbarErrorMessage(
        @StringRes message: Int,
        type: SnackbarType = SnackbarType.DEFAULT
    ) {
        _snackbarText.value = Event(SnackbarMessage(message, type, true))
    }

    fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(SnackbarMessage(message))
    }

    fun onHelp() {
        _helpEvent.value = Event(Unit)
    }

    fun onGetMyLocation() {
        _myLocationEvent.value = Event(Unit)
    }

    fun savePoiSelected(poi: PointOfInterest) {
        _poiSelected.value = poi
    }

    fun setNullPoiSelected() {
        _poiSelected.value = null
    }

    fun onBackMap() {
        _backEvent.value = Event(Unit)
    }

    fun onSavePoi() {
        poiSaved = _poiSelected.value
        namePoiSaved.value = poiSaved?.name
        _backEvent.value = Event(Unit)
    }

    fun saveIsLocationPermissionEnabled(response: Boolean) =
        viewModelScope.launch(viewModelScope.coroutineContext + mainDispatcher) {
            if (response) {
                dataStoreRepository.saveIsLocationPermissionEnabled(false)
                dataStoreRepository.saveIsLocationPermissionEnabled(true)
            } else {
                dataStoreRepository.saveIsLocationPermissionEnabled(false)
            }
        }

    fun onMore() {
        _moreEvent.value = Event(Unit)
    }

    fun setCameraPosition(cameraPosition: CameraPosition) {
        mapCameraPosition = cameraPosition
    }

    fun setMapType(@IntegerRes type: Int) {
        _mapType.value = type
    }

    companion object {
        private const val TAG = "AddEditProjectViewModel"
    }

}