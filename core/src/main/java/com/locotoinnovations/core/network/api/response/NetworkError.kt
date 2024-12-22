package com.locotoinnovations.core.network.api.response

import com.locotoinnovations.core.room.operation.common.DataError

/**
 * Represents possible errors from a network response. These are defined in [ApiResponse.Error.toNetworkError].
 */
sealed class NetworkError {
    data class BadRequest(val errorMessage: String) : NetworkError()
    data object Forbidden : NetworkError()
    data object Empty : NetworkError()
    data object Unauthorized : NetworkError()
    data object NotFound : NetworkError()
    data object MethodNotAllowed : NetworkError()
    data object NotAcceptable : NetworkError()
    data object Gone : NetworkError()
    data object UnsupportedMediaType : NetworkError()
    data object RequestTimeout : NetworkError()
    data object Conflict : NetworkError()
    data object UnprocessableEntity : NetworkError()
    data object Locked : NetworkError()
    data object InternalServerError : NetworkError()
    data object NotImplemented : NetworkError()
    data object BadGateway : NetworkError()
    data object ServiceUnavailable : NetworkError()
    data class Unknown(
        val errorCode: Int,
        val errorMessage: String,
        val errorBody: String?
    ) : NetworkError()

    /**
     * Method to map a [NetworkError] to a [DataError] object
     */
    fun toDataError(): DataError {
        return when (this) {
            is Locked,
            is Forbidden -> DataError.Forbidden

            is Gone,
            is Empty,
            is NotFound -> DataError.NotFound

            is RequestTimeout,
            is ServiceUnavailable -> DataError.Unreachable

            is InternalServerError,
            is Unauthorized,
            is Conflict,
            is UnprocessableEntity,
            is Unknown,
            is BadGateway,
            is MethodNotAllowed,
            is NotAcceptable,
            is NotImplemented,
            is UnsupportedMediaType,
            is BadRequest -> DataError.GeneralError
        }
    }
}