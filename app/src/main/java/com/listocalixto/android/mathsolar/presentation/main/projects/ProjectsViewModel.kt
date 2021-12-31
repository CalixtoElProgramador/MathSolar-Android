package com.listocalixto.android.mathsolar.presentation.main.projects

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.ADD_EDIT_RESULT_OK
import com.listocalixto.android.mathsolar.app.Constants.DELETE_RESULT_OK
import com.listocalixto.android.mathsolar.app.Constants.EDIT_RESULT_OK
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
class ProjectsViewModel @Inject constructor(
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

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private var currentFiltering = ALL_PROJECTS
    private var resultMessageShown: Boolean = false

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
                setFilter(
                    R.string.label_all, R.string.no_projects_all,
                    R.drawable.ic_projects_empty, true
                )
            }
            CONNECTED_TO_THE_GRID -> {
                setFilter(
                    R.string.label_connected, R.string.no_projects_connected,
                    R.drawable.ic_projects_empty, false
                )
            }
            ISOLATED -> {
                setFilter(
                    R.string.label_isolated, R.string.no_projects_isolated,
                    R.drawable.ic_projects_empty, false
                )
            }
            HYBRID -> {
                setFilter(
                    R.string.label_hybrid, R.string.no_projects_hybrid,
                    R.drawable.ic_projects_empty, false
                )
            }
            FAVORITE -> {
                setFilter(
                    R.string.label_favorites, R.string.no_projects_favorites,
                    R.drawable.ic_projects_empty, false
                )
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

    fun showEditResultMessage(result: Int) {
        if (resultMessageShown) return
        when (result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_project_message)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_added_project_message)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.successfully_deleted_project_message)
        }
        resultMessageShown = true
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
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
                CONNECTED_TO_THE_GRID -> if (project.type == CONNECTED_TO_THE_GRID) {
                    projectsToShow.add(project)
                }
                HYBRID -> if (project.type == HYBRID) {
                    projectsToShow.add(project)
                }
                ISOLATED -> if (project.type == ISOLATED) {
                    projectsToShow.add(project)
                }
                FAVORITE -> if (project.isFavorite) {
                    projectsToShow.add(project)
                }
            }
        }
        return projectsToShow
    }

    fun favoriteProject(project: PVProject, favorite: Boolean) = viewModelScope.launch {
        if (favorite) {
            repo.likePVProject(project)
            showSnackbarMessage(R.string.project_marked_favorite)
        } else {
            repo.dislikePVProject(project)
            showSnackbarMessage(R.string.project_removed_favorite)
        }
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    fun onAddNewProject() {
        _newProjectEvent.value = Event(Unit)
    }

    /**
     * Called by Data Binding.
     */
    fun openProject(projectId: String) {
        _openProjectEvent.value = Event(projectId)
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [PVProjectDataSource]
     */
    fun loadProjects(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    fun refresh() {
        _forceUpdate.value = true
    }

}