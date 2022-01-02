package com.app.freshworkstudio.model

/**
 * Sealed class type-restricts the result of IO calls to success and failure. The type
 * <T> represents the model class expected from the API call in case of a success
 * In case of success, the result will be wrapped around the OnSuccessResponse class
 * In case of error, the throwable causing the error will be wrapped around OnErrorResponse class
 * @author keyur thumar
 * @since 1.0
 */
sealed class IOTaskResult<out T : Any> {
    data class OnSuccess<out T : Any>(val data: T) : IOTaskResult<T>()
    data class OnFailed<T : Any>(var message: T) : IOTaskResult<T>()
}