package com.listocalixto.android.mathsolar.presentation.auth.register

import android.content.ActivityNotFoundException
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.utils.SnackbarType.DEFAULT
import com.listocalixto.android.mathsolar.app.Constants.ERROR_EMAIL_IN_USE
import com.listocalixto.android.mathsolar.app.Constants.ERROR_FIELDS_EMPTY
import com.listocalixto.android.mathsolar.app.Constants.ERROR_INTERNET_CONNECTION
import com.listocalixto.android.mathsolar.app.Constants.ERROR_INVALID_EMAIL
import com.listocalixto.android.mathsolar.app.Constants.ERROR_NO_GALLERY_APP_FOUNDED
import com.listocalixto.android.mathsolar.app.Constants.ERROR_PASSWORDS_ARE_DIFFERENT
import com.listocalixto.android.mathsolar.app.Constants.ERROR_PASSWORD_SHORT
import com.listocalixto.android.mathsolar.app.Constants.ERROR_PERMISION_DENIED
import com.listocalixto.android.mathsolar.app.Constants.ERROR_SOMETHING_WENT_WRONG
import com.listocalixto.android.mathsolar.app.Constants.ERROR_TO_MANY_REQUESTS
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.core.Resource.Success
import com.listocalixto.android.mathsolar.domain.auth.AuthRepo
import com.listocalixto.android.mathsolar.utils.*
import com.listocalixto.android.mathsolar.utils.SnackbarType.GO_TO_SETTINGS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repo: AuthRepo) : ViewModel() {

    private val _errorMessage = MutableLiveData<ErrorMessage?>(null)
    val errorMessage: LiveData<ErrorMessage?> = _errorMessage

    val name = MutableLiveData<String>()
    val lastname = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    private val _profilePicture = MutableLiveData<ProfilePicture?>(null)
    val profilePicture: LiveData<ProfilePicture?> = _profilePicture

    private val _backEvent = MutableLiveData<Event<Unit>>()
    val backEvent: LiveData<Event<Unit>> = _backEvent

    private val _nextEvent = MutableLiveData<Event<Unit>>()
    val nextEvent: LiveData<Event<Unit>> = _nextEvent

    private val _selectProfilePictureEvent = MutableLiveData<Event<Unit>>()
    val selectProfilePictureEvent: LiveData<Event<Unit>> = _selectProfilePictureEvent

    private val _openCameraEvent = MutableLiveData<Event<Unit>>()
    val openCameraEvent: LiveData<Event<Unit>> = _openCameraEvent

    private val _openGalleryEvent = MutableLiveData<Event<Unit>>()
    val openGalleryEvent: LiveData<Event<Unit>> = _openGalleryEvent

    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState

    private val _snackbarText = MutableLiveData<Event<SnackbarMessage>>()
    val snackbarText: LiveData<Event<SnackbarMessage>> = _snackbarText

    private val currentFragment = MutableLiveData(R.id.register01Fragment)

    init {
        _profilePicture.value = ProfilePicture(drawable = R.drawable.ic_error_placeholder)
    }

    fun onBack() {
        _backEvent.value = Event(Unit)
    }

    fun setCurrentFragment(fragment: Int) {
        currentFragment.value = fragment
    }

    fun onNext() {
        when (currentFragment.value) {
            R.id.register01Fragment -> {
                val currentName = name.value
                val currentLastName = lastname.value
                val currentEmail = email.value
                val patternEmail = Patterns.EMAIL_ADDRESS.toRegex()

                when {
                    currentName.isNullOrEmpty() || currentLastName.isNullOrEmpty() || currentEmail.isNullOrEmpty() -> {
                        _errorMessage.value = ErrorMessage(ERROR_FIELDS_EMPTY)
                    }
                    !currentEmail.matches(patternEmail) -> {
                        _errorMessage.value = ErrorMessage(ERROR_INVALID_EMAIL)
                    }
                    else -> isCurrentEmailRegister(currentEmail)

                }
            }
            R.id.register02Fragment -> {
                val currentPassword = password.value
                val currentConfirmPassword = confirmPassword.value

                when {
                    currentPassword.isNullOrEmpty() || currentConfirmPassword.isNullOrEmpty() -> {
                        _errorMessage.value = ErrorMessage(ERROR_FIELDS_EMPTY)
                    }
                    currentPassword.length < 8 -> _errorMessage.value =
                        ErrorMessage(ERROR_PASSWORD_SHORT)
                    currentPassword != currentConfirmPassword -> _errorMessage.value =
                        ErrorMessage(ERROR_PASSWORDS_ARE_DIFFERENT)
                    else -> navigateToNextFragment()
                }

            }
            R.id.register03Fragment -> {
            }
        }

    }

    private fun isCurrentEmailRegister(email: String) {
        viewModelScope.launch {
            isLoading(true)
            repo.isEmailRegister(email).also { result ->
                when (result) {
                    is Success -> {
                        if (result.data /* if (query.isEmpty) */) {
                            navigateToNextFragment()
                            disableError()
                        } else {
                            _errorMessage.value = ErrorMessage(ERROR_EMAIL_IN_USE)
                        }
                    }
                    is Resource.Error -> {
                        _errorMessage.value = ErrorMessage(exception = result.exception)
                    }
                    is Resource.Loading -> isLoading(true)
                }
                isLoading(false)
            }
        }
    }

    private fun disableError() {
        _errorMessage.value = null
    }

    private fun isLoading(b: Boolean) {
        _loadingState.value = b
    }

    private fun navigateToNextFragment() {
        _nextEvent.value = Event(Unit)
    }

    fun onProfilePicture() {
        _selectProfilePictureEvent.value = Event(Unit)
    }

    fun onCameraOpen() {
        _openCameraEvent.value = Event(Unit)
    }

    fun onGalleryOpen() {
        _openGalleryEvent.value = Event(Unit)
    }

    fun setProfilePictureType(profilePicture: ProfilePicture) {
        _profilePicture.value = null
        _profilePicture.value = profilePicture
    }

    fun setErrorStringResMessage(@StringRes message: Int) {
        _errorMessage.value = ErrorMessage(stringRes = message)
    }

    fun setExceptionMessage(e: Exception) {
        _errorMessage.value = ErrorMessage(exception = e)
    }

    fun showErrorMessage(errorMessage: ErrorMessage) {
        errorMessage.stringRes?.let {
            when (it) {
                ERROR_PERMISION_DENIED -> showSnackbarErrorMessage(
                    ERROR_PERMISION_DENIED,
                    GO_TO_SETTINGS,
                )
            }
        }

        errorMessage.exception?.let {
            when (it) {
                is ActivityNotFoundException -> {
                    showSnackbarErrorMessage(ERROR_NO_GALLERY_APP_FOUNDED)
                }
                is FirebaseTooManyRequestsException -> {
                    showSnackbarErrorMessage(ERROR_TO_MANY_REQUESTS)
                }
                is FirebaseNetworkException -> {
                    showSnackbarErrorMessage(ERROR_INTERNET_CONNECTION)
                }
                else -> {
                    showSnackbarErrorMessage(ERROR_SOMETHING_WENT_WRONG)
                }
            }
        }
        disableError()
    }

    private fun showSnackbarErrorMessage(
        @StringRes message: Int,
        type: SnackbarType = DEFAULT
    ) {
        _snackbarText.value = Event(SnackbarMessage(message, type, true))
    }

}