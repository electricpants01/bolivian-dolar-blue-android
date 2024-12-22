package com.locotoinnovations.core.room.operation.common

/**
 * Represents a result of an operation that was executed. For the failure cause,
 * the operation could have failed for any number of reasons so the [DataError]
 * describes the exact error that happened.
 */
sealed class DataResult<out Data> {
    data class Success<Data>(val data: Data) : DataResult<Data>()
    data class Failure(val error: DataError) : DataResult<Nothing>()
}

fun <T, R> DataResult<T>.map(transform: (T) -> R): DataResult<R> {
    return when (this) {
        is DataResult.Success -> DataResult.Success(transform(data))
        is DataResult.Failure -> DataResult.Failure(error)
    }
}