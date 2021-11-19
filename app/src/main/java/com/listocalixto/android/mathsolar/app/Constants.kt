package com.listocalixto.android.mathsolar.app

import android.app.Activity
import android.util.Patterns
import com.listocalixto.android.mathsolar.R

object Constants {

    const val APP_DATABASE_NAME = "app_database"

    // Keys for navigation
    const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
    const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
    const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3

    const val ERROR_FIELDS_EMPTY = R.string.err_login_fields_empty
    const val ERROR_INVALID_EMAIL = R.string.err_login_invalid_email
    const val ERROR_PASSWORD_SHORT = R.string.err_login_password_short
    const val ERROR_INCORRECT_PASSWORD = R.string.err_firebase_auth_incorrect_password
    const val ERROR_UNREGISTERED_EMAIL = R.string.err_firebase_auth_unregistered_email
    const val ERROR_TO_MANY_REQUESTS = R.string.err_firebase_auth_too_many_requests
    const val ERROR_CONNECTION = R.string.err_firebase_auth_internet_connection_login

}