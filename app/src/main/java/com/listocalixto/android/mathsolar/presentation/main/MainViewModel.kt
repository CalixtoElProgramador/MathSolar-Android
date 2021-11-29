package com.listocalixto.android.mathsolar.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.User
import com.listocalixto.android.mathsolar.domain.auth.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _bottomNavExpandedState = MutableLiveData(false)
    val bottomNavExpandedState: LiveData<Boolean> = _bottomNavExpandedState

    private val _scrimColor = MutableLiveData<Int>()
    val scrimColor: LiveData<Int> = _scrimColor

    private val _currentFragment = MutableLiveData(R.id.articlesFragment)
    val currentFragment: LiveData<Int> = _currentFragment

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> = _userData

    init {
        getCurrentUserData()
    }

    private fun getCurrentUserData() {
        viewModelScope.launch {
            when (val result = authRepo.getCurrentUserData()) {
                is Resource.Success -> {
                    _userData.value = result.data
                }
                is Resource.Error -> {
                }
            }
        }
    }


    fun isBottomNavExpanded(b: Boolean) {
        _bottomNavExpandedState.value = b
    }

    fun setScrimBackground(color: Int) {
        _scrimColor.value = color
    }

    fun onScrim() {
        _bottomNavExpandedState.value = false
    }

    fun setCurrentFragment(fragment: Int) {
        _currentFragment.value = fragment
    }

}
