package com.listocalixto.android.mathsolar.presentation.auth.login

import android.util.Patterns
import androidx.lifecycle.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.ERROR_CONNECTION
import com.listocalixto.android.mathsolar.app.Constants.ERROR_FIELDS_EMPTY
import com.listocalixto.android.mathsolar.app.Constants.ERROR_INCORRECT_PASSWORD
import com.listocalixto.android.mathsolar.app.Constants.ERROR_INVALID_EMAIL
import com.listocalixto.android.mathsolar.app.Constants.ERROR_PASSWORD_SHORT
import com.listocalixto.android.mathsolar.app.Constants.ERROR_TO_MANY_REQUESTS
import com.listocalixto.android.mathsolar.app.Constants.ERROR_UNREGISTERED_EMAIL
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.domain.auth.AuthRepo
import com.listocalixto.android.mathsolar.utils.Event
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

    private val _errorMessage = MutableLiveData<Int?>(null)
    val errorMessage: LiveData<Int?> = _errorMessage

    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState

    /*val isAnError: LiveData<Boolean> = Transformations.map(_errorMessage) {
        it != R.string.no_error_key
    }*/

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var resultMessageShown: Boolean = false

    fun onSignIn() {
        val currentEmail = email.value
        val currentPassword = password.value
        val patternEmail = Patterns.EMAIL_ADDRESS.toRegex()

        when {
            currentEmail.isNullOrEmpty() || currentPassword.isNullOrEmpty() -> _errorMessage.value =
                R.string.err_login_fields_empty
            !currentEmail.matches(patternEmail) -> _errorMessage.value =
                R.string.err_login_invalid_email
            currentPassword.length < 8 -> _errorMessage.value = R.string.err_login_password_short
            else -> sendInputs(currentEmail, currentPassword)
        }

    }

    private fun sendInputs(currentEmail: String, currentPassword: String) {
        viewModelScope.launch {
            isLoading(true)
            when (val result = repo.signIn(currentEmail, currentPassword)) {
                is Resource.Success -> {
                    isLoading(false)
                    _signInEvent.value = Event(Unit)
                }
                is Resource.Error -> {
                    isLoading(false)
                    when (result.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            _errorMessage.value = R.string.err_firebase_auth_incorrect_password
                        }
                        is FirebaseAuthInvalidUserException -> {
                            _errorMessage.value = R.string.err_firebase_auth_unregistered_email
                        }
                        is FirebaseTooManyRequestsException -> {
                            _errorMessage.value = R.string.err_firebase_auth_too_many_requests
                        }
                        is FirebaseNetworkException -> {
                            _errorMessage.value =
                                R.string.err_firebase_auth_internet_connection_login
                        }
                        else -> {
                            _errorMessage.value = R.string.err_firebase_auth_something_went_wrong
                        }
                    }
                }
            }
        }
    }

    private fun isLoading(b: Boolean) {
            _loadingState.value = b
    }

    private fun disableError() {
        _errorMessage.value = null
    }

    fun showEditResultMessage(result: Int) {
        when (result) {
            ERROR_FIELDS_EMPTY -> showSnackbarMessage(R.string.err_login_fields_empty)
            ERROR_INVALID_EMAIL -> showSnackbarMessage(R.string.err_login_invalid_email)
            ERROR_PASSWORD_SHORT -> showSnackbarMessage(R.string.err_login_password_short)
            ERROR_INCORRECT_PASSWORD -> showSnackbarMessage(R.string.err_firebase_auth_incorrect_password)
            ERROR_UNREGISTERED_EMAIL -> showSnackbarMessage(R.string.err_firebase_auth_unregistered_email)
            ERROR_TO_MANY_REQUESTS -> showSnackbarMessage(R.string.err_firebase_auth_too_many_requests)
            ERROR_CONNECTION -> showSnackbarMessage(R.string.err_firebase_auth_internet_connection_login)
            else -> showSnackbarMessage(R.string.err_firebase_auth_something_went_wrong)
        }
        disableError()
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

}

