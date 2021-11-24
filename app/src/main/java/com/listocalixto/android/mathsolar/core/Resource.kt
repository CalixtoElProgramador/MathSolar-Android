package com.listocalixto.android.mathsolar.core

import com.listocalixto.android.mathsolar.core.Resource.Success
import com.listocalixto.android.mathsolar.utils.ErrorMessage


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<out R> {

    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val errorMessage: ErrorMessage) : Resource<Nothing>()


    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=${errorMessage.exception}]"
            is Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Resource] is of type [Success] & holds non-null [Success.data].
 */
val Resource<*>.succeeded
    get() = this is Success && data != null
