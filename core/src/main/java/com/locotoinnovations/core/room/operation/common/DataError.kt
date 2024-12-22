package com.locotoinnovations.core.room.operation.common

/**
 * Represents an error that can happen for an operation. These are all of the possible
 * cases that the UI may want to react to. For instance, if a [StorageFull] error happens,
 * the UI may want to alert the user that the operation failed because they are out of
 * storage space
 */
sealed class DataError {
    /** The requested resource could not be found */
    data object NotFound : DataError()

    /**
     * The client does not have access to that requested resource
     * or is not permitted to perform that action
     */
    data object Forbidden : DataError()

    /**
     * The resource could not be retrieved. The server
     * might be overloaded or down for maintenance.
     */
    data object Unreachable : DataError()

    /**
     * This means that a connection could not be established.
     */
    data object Offline : DataError()

    /** The device's storage is full */
    data object StorageFull : DataError()

    /** The catch-all case for an undetermined error */
    data object GeneralError : DataError()

    /**
     * There may be times where a custom error is warranted so the UI can
     * react on that specific error. This would be used for that case.
     *
     * For instance, when creating an album with a name that already exists,
     * the API returns back a 400 Bad Request with an error message. The API
     * may send back "name already exists" as the error message. So you would
     * create a AlbumAlreadyExistsDataError class that extends [CustomError]
     * and then map that Bad Request error with that specific message to the
     * subclass of this [CustomError].
     *
     * The UI can then check if the [CustomError] is an instance of that subclass
     * and then maybe show a SnackBar saying that the album already exists.
     */
    abstract class CustomError : DataError()
}