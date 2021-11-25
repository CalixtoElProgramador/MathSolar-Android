package com.listocalixto.android.mathsolar.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton

@BindingAdapter("app:disableButton")
fun MaterialButton.disable(isLoading: Boolean) {
    if (isLoading) {
        alpha = 0.4f
        isEnabled = false
    } else {
        alpha = 1.0f
        isEnabled = true
    }
}