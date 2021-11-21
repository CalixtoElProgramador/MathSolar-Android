package com.listocalixto.android.mathsolar.app

import android.app.Activity
import com.listocalixto.android.mathsolar.R

object Constants {

    const val APP_DATABASE_NAME = "app_database"

    // Keys for navigation
    const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
    const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
    const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3

    const val ERROR_FIELDS_EMPTY = R.string.err_fields_empty
    const val ERROR_INVALID_EMAIL = R.string.err_invalid_email
    const val ERROR_PASSWORD_SHORT = R.string.err_password_short
    const val ERROR_EMAIL_IN_USE = R.string.err_email_is_already_in_use
    const val ERROR_INCORRECT_PASSWORD = R.string.err_incorrect_password
    const val ERROR_UNREGISTERED_EMAIL = R.string.err_unregistered_email
    const val ERROR_TO_MANY_REQUESTS = R.string.err_too_many_requests
    const val ERROR_INTERNET_CONNECTION = R.string.err_internet_connection
    const val ERROR_SOMETHING_WENT_WRONG = R.string.err_something_went_wrong
    const val ERROR_PASSWORDS_ARE_DIFFERENT = R.string.error_passwords_are_not_the_same

    const val ERROR_NO_GALLERY_APP_FOUNDED = R.string.err_no_gallery_app
    const val ERROR_PERMISION_DENIED = R.string.err_permission_denied

}