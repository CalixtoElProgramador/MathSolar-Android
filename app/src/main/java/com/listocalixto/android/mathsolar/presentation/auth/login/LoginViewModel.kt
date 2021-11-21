package com.listocalixto.android.mathsolar.presentation.auth.login

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.listocalixto.android.mathsolar.app.Constants.ERROR_FIELDS_EMPTY
import com.listocalixto.android.mathsolar.app.Constants.ERROR_INTERNET_CONNECTION
import com.listocalixto.android.mathsolar.app.Constants.ERROR_INCORRECT_PASSWORD
import com.listocalixto.android.mathsolar.app.Constants.ERROR_INVALID_EMAIL
import com.listocalixto.android.mathsolar.app.Constants.ERROR_PASSWORD_SHORT
import com.listocalixto.android.mathsolar.app.Constants.ERROR_SOMETHING_WENT_WRONG
import com.listocalixto.android.mathsolar.app.Constants.ERROR_TO_MANY_REQUESTS
import com.listocalixto.android.mathsolar.app.Constants.ERROR_UNREGISTERED_EMAIL
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.domain.auth.AuthRepo
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import com.listocalixto.android.mathsolar.utils.Event
import com.listocalixto.android.mathsolar.utils.SnackbarMessage
import com.listocalixto.android.mathsolar.utils.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: AuthRepo) : ViewModel() {

    val email = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    private val _signInEvent = MutableLiveData<Event<Unit>>()
    val signInEvent: LiveData<Event<Unit>> = _signInEvent

    private val _signUpEvent = MutableLiveData<Event<Unit>>()
    val signUpEvent: LiveData<Event<Unit>> = _signUpEvent

    private val _guestLoginEvent = MutableLiveData<Event<Unit>>()
    val guestLoginEvent: LiveData<Event<Unit>> = _guestLoginEvent

    private val _passwordForgotEvent = MutableLiveData<Event<Unit>>()
    val passwordForgotEvent: LiveData<Event<Unit>> = _passwordForgotEvent

    private val _errorMessage = MutableLiveData<ErrorMessage?>(null)
    val errorMessage: LiveData<ErrorMessage?> = _errorMessage

    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState

    /*val isAnError: LiveData<Boolean> = Transformations.map(_errorMessage) {
        it != R.string.no_error_key
    }*/

    private val _snackbarText = MutableLiveData<Event<SnackbarMessage>>()
    val snackbarText: LiveData<Event<SnackbarMessage>> = _snackbarText

    private var resultMessageShown: Boolean = false

    fun onSignIn() {
        val currentEmail = email.value
        val currentPassword = password.value
        val patternEmail = Patterns.EMAIL_ADDRESS.toRegex()

        when {
            currentEmail.isNullOrEmpty() || currentPassword.isNullOrEmpty() ->
                _errorMessage.value = ErrorMessage(stringRes = ERROR_FIELDS_EMPTY)
            !currentEmail.matches(patternEmail) ->
                _errorMessage.value = ErrorMessage(stringRes = ERROR_INVALID_EMAIL)
            currentPassword.length < 8 ->
                _errorMessage.value = ErrorMessage(stringRes = ERROR_PASSWORD_SHORT)
            else -> sendInputs(currentEmail, currentPassword)
        }

    }

    private fun sendInputs(currentEmail: String, currentPassword: String) {
        viewModelScope.launch {
            isLoading(true)
            when (val result = repo.signIn(currentEmail, currentPassword)) {
                is Resource.Success -> {
                    _signInEvent.value = Event(Unit)
                }
                is Resource.Error -> {
                    _errorMessage.value = ErrorMessage(exception = result.exception)
                }
            }
            isLoading(false)
        }
    }

    private fun isLoading(b: Boolean) {
        _loadingState.value = b
    }

    private fun disableError() {
        _errorMessage.value = null
    }

    fun showErrorMessage(errorMessage: ErrorMessage) {
        errorMessage.stringRes?.let {
            showSnackbarErrorMessage(it)
        }

        errorMessage.exception?.let {
            when (it) {
                is FirebaseAuthInvalidCredentialsException -> {
                    showSnackbarErrorMessage(ERROR_INCORRECT_PASSWORD)
                }
                is FirebaseAuthInvalidUserException -> {
                    showSnackbarErrorMessage(ERROR_UNREGISTERED_EMAIL)
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
        type: SnackbarType = SnackbarType.DEFAULT
    ) {
        _snackbarText.value = Event(SnackbarMessage(message, type, true))
    }

    fun onSignUp() {
        _signUpEvent.value = Event(Unit)
    }

}

