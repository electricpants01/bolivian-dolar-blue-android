package com.locotoinnovations.core.network

import com.locotoinnovations.core.network.api.response.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Request
import retrofit2.Call
import java.io.IOException
import kotlin.coroutines.resume

/**
 * Shortcut to retrieving the url from a request
 */
val Call<*>.url get() = (request() as Request).url.toString()

/**
 * Executes the [Call] in a coroutine and returns the response. This will also add
 * headers specific to Procore at run-time before it is executed. These headers
 * are required to make requests work with our backend.
 *
 * @param companyServerId the server id of the company used for MPZ. See [addMPZSupport]
 * @param idempotentToken (only used for uploads)
 *
 * @return Returns a [ApiResponse] with the data or error
 */
suspend fun <T> Call<T>.executeNetwork(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ApiResponse<T> {
    return withContext(ioDispatcher) {
        suspendCancellableCoroutine { continuation ->
            continuation.resume(blockingExecute())
            continuation.invokeOnCancellation { cancel() }
        }
    }
}

/**
 * Executes a blocking [Call] and returns a [ApiResponse]. It will throw an [IOException] if there
 * is a problem talking to the server, for example, if the device is offline.
 */
private fun <T> Call<T>.blockingExecute(): ApiResponse<T> {
    return try {
        ApiResponse.create(execute())
    } catch (e: IOException) {
        ApiResponse.create(e)
    }
}