package com.listocalixto.android.mathsolar.presentation.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.domain.pv_project.PVProjectRepo
import com.listocalixto.android.mathsolar.data.source.pv_project.PVProjectDataSource
import com.listocalixto.android.mathsolar.utils.Event
import com.listocalixto.android.mathsolar.utils.PVProjectType
import com.listocalixto.android.mathsolar.utils.PVProjectType.ALL_PROJECTS
import com.listocalixto.android.mathsolar.utils.PVProjectType.CONNECTED_TO_THE_GRID
import com.listocalixto.android.mathsolar.utils.PVProjectType.HYBRID
import com.listocalixto.android.mathsolar.utils.PVProjectType.ISOLATED
import com.listocalixto.android.mathsolar.utils.PVProjectType.FAVORITE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: PVProjectRepo
) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _items: LiveData<List<PVProject>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                repo.refreshPVProjects()
                _dataLoading.value = false
            }
        }
        repo.observePVProjects().switchMap { filterProjects(it) }
    }
    val items: LiveData<List<PVProject>> = _items

    private val _currentFilteringLabel = MutableLiveData<Int>()
    val currentFilteringLabel: LiveData<Int> = _currentFilteringLabel

    private val _noProjectsLabel = MutableLiveData<Int>()
    val noProjectsLabel: LiveData<Int> = _noProjectsLabel

    private val _noProjectsIconRes = MutableLiveData<Int>()
    val noProjectsIconRes: LiveData<Int> = _noProjectsIconRes

    private val _projectsAddViewVisible = MutableLiveData<Boolean>()
    val projectsAddViewVisible: LiveData<Boolean> = _projectsAddViewVisible

    private val _openProjectEvent = MutableLiveData<Event<String>>()
    val openProjectEvent: LiveData<Event<String>> = _openProjectEvent

    private val _newProjectEvent = MutableLiveData<Event<Unit>>()
    val newProjectEvent: LiveData<Event<Unit>> = _newProjectEvent

    private val isDataLoadingError = MutableLiveData<Boolean>()
    private var currentFiltering = ALL_PROJECTS

    init {
        // Set initial state
        setFiltering(ALL_PROJECTS)
        loadProjects(true)
    }

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be [ALL_PROJECTS],
     * [CONNECTED_TO_THE_GRID],
     * [HYBRID],
     * [ISOLATED], or
     * [FAVORITE]
     */
    fun setFiltering(requestType: PVProjectType) {
        currentFiltering = requestType

        // Depending on the filter type, set the filtering label, icon drawables, etc.
        when (requestType) {
            ALL_PROJECTS -> {
            }
            CONNECTED_TO_THE_GRID -> {
            }
            ISOLATED -> {
            }
            HYBRID -> {
            }
            FAVORITE -> {
            }
        }
        // Refresh list
        loadProjects(false)
    }

    private fun setFilter(
        @StringRes filteringLabelString: Int, @StringRes noProjectsLabelString: Int,
        @DrawableRes noProjectsIconDrawable: Int, projectsAddVisible: Boolean
    ) {
        _currentFilteringLabel.value = filteringLabelString
        _noProjectsLabel.value = noProjectsLabelString
        _noProjectsIconRes.value = noProjectsIconDrawable
        _projectsAddViewVisible.value = projectsAddVisible
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    private fun filterProjects(projectResult: Resource<List<PVProject>>): LiveData<List<PVProject>> {
        val result = MutableLiveData<List<PVProject>>()

        if (projectResult is Resource.Success) {
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(projectResult.data, currentFiltering)
            }
        } else {
            result.value = emptyList()
            showSnackbarMessage(R.string.err_loading_projects)
            isDataLoadingError.value = true
        }
        return result
    }

    private fun filterItems(
        projects: List<PVProject>,
        filteringType: PVProjectType
    ): List<PVProject> {
        val projectsToShow = ArrayList<PVProject>()
        projects.forEach { project ->
            when (filteringType) {
                ALL_PROJECTS -> projectsToShow.add(project)
                CONNECTED_TO_THE_GRID -> if (project.projectType == CONNECTED_TO_THE_GRID) {
                    projectsToShow.add(project)
                }
                HYBRID -> if (project.projectType == HYBRID) {
                    projectsToShow.add(project)
                }
                ISOLATED -> if (project.projectType == ISOLATED) {
                    projectsToShow.add(project)
                }
                FAVORITE -> if (project.isFavorite) {
                    projectsToShow.add(project)
                }
            }
        }
        return projectsToShow
    }

    fun openProject(projectId: String) {
        _openProjectEvent.value = Event(projectId)
    }
    /**
     * @param forceUpdate   Pass in true to refresh the data in the [PVProjectDataSource]
     */
    fun loadProjects(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }


}