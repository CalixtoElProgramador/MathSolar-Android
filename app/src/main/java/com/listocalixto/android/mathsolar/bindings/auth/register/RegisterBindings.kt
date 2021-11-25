package com.listocalixto.android.mathsolar.bindings

import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.ERROR_EMAIL_IN_USE
import com.listocalixto.android.mathsolar.app.Constants.ERROR_FIELDS_EMPTY
import com.listocalixto.android.mathsolar.app.Constants.ERROR_INVALID_EMAIL
import com.listocalixto.android.mathsolar.app.Constants.ERROR_PASSWORDS_ARE_DIFFERENT
import com.listocalixto.android.mathsolar.app.Constants.ERROR_PASSWORD_SHORT
import com.listocalixto.android.mathsolar.utils.enableError
import com.listocalixto.android.mathsolar.utils.isEditTextEmpty
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("app:setErrorMessage")
fun TextInputLayout.setErrorMessage(message: Int?) {
    when (message) {
        ERROR_FIELDS_EMPTY -> if (isEditTextEmpty()) enableError(message)
        ERROR_INVALID_EMAIL, ERROR_EMAIL_IN_USE -> if (id == R.id.inputLayout_email) enableError(
            message
        )
        ERROR_PASSWORD_SHORT -> if (id == R.id.inputLayout_password) enableError(message)
        ERROR_PASSWORDS_ARE_DIFFERENT -> if (id == R.id.inputLayout_passwordConfirm) enableError(
            message
        )
        null -> isErrorEnabled = false
    }
    editText?.doAfterTextChanged { isErrorEnabled = false }
}

@BindingAdapter("app:profilePicture")
fun CircleImageView.setProfilePicture(item: Bitmap?) {
    item?.let {
        setImageBitmap(it)
    }

}


