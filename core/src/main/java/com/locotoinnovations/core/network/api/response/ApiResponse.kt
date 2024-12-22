package com.locotoinnovations.core.network.api.response

import retrofit2.Response
import java.util.regex.Pattern
import kotlin.math.ceil

/**
 * Common class used by API [Response]'s and acts as a wrapper around Retrofit's Response.
 * This code is a slightly modified version from the [Google Sample Architecture](https://github.com/android/architecture-components-samples/blob/88747993139224a4bb6dbe985adf652d557de621/GithubBrowserSample/app/src/main/java/com/android/example/github/api/ApiResponse.kt)
 * code.
 *
 * @param T the type of the response object
 */
sealed class ApiResponse<out T> {

    companion object {

        const val ERROR_CODE_LOCAL = -1

        /**
         * Creates a [ApiResponse.Error] from a [Throwable]. This is
         * used for local errors when attempting to execute a request.
         */
        fun create(error: Throwable): Error {
            return Error(
                throwable = error,
                errorCode = ERROR_CODE_LOCAL,
                errorMessage = error.message ?: "unknown error",
                errorBody = null,
                headers = emptyMap()
            )
        }

        /**
         * Creates a [ApiResponse.Success] or [ApiResponse.Empty] if the response was successful or [ApiResponse.Error] otherwise.
         * The empty case happens when the response is successful but has a null response body or the code is 204.
         *
         * See [ApiResponse.Empty] for more info about the details of the empty case.
         */
        fun <T> create(response: Response<T>): ApiResponse<T> {
            val headers = response.headers().toMultimap()

            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    Empty(
                        headers = headers
                    )
                } else {
                    Success(
                        body = body,
                        linkHeader = response.headers()["link"],
                        headers = headers
                    )
                }
            } else {
                Error(
                    throwable = null,
                    errorCode = response.code(),
                    errorMessage = response.message(),
                    errorBody = response.errorBody()?.string(),
                    headers = headers
                )
            }
        }
    }

    /**
     * Map of http headers in the API response.
     * Combines headers with the same name (case-insensitive) into a single list.
     */
    abstract val headers: Map<String, List<String>>

    abstract fun <R> map(transform: (T) -> R): ApiResponse<R>

    /**
     * A class that represents a successful response.
     *
     * @param body the response body / data
     * @param links a map of string key-value pairs coming from the "link" header in the response. This typically
     * has information like the next page or last page in paginated responses.
     */
    data class Success<T> internal constructor(
        val body: T,
        val links: Map<String, String>,
        override val headers: Map<String, List<String>>
    ) : ApiResponse<T>() {

        internal constructor(
            body: T,
            linkHeader: String?,
            headers: Map<String, List<String>>
        ) : this(
            body = body,
            links = linkHeader?.extractLinks() ?: emptyMap(),
            headers = headers
        )

        override fun <R> map(transform: (T) -> R) = Success<R>(
            body = transform(body),
            links = links,
            headers = headers
        )

        /**
         * Gets the last page if it exists in a paginated response
         */
        val lastPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
            val totalItems = headers["total"]?.firstOrNull()?.toLongOrNull()
            val numItemsPerPage = headers["per-page"]?.firstOrNull()?.toIntOrNull()

            return@lazy when {
                // If there are no items, then there is only 1 page which is just the first page
                totalItems != null && totalItems == 0L -> 1

                // If there are items and we know how many items per page, then we can calculate the last page
                totalItems != null && numItemsPerPage != null -> ceil(totalItems.toDouble() / numItemsPerPage.toDouble()).toInt()

                // If we don't know how many items per page or the total amount of items, then fallback to the "last" link if it exists
                else -> links[LAST_LINK]?.let { next ->
                    val matcher = PAGE_PATTERN.matcher(next)
                    if (!matcher.find() || matcher.groupCount() != 1) {
                        null
                    } else {
                        matcher.group(1)?.toIntOrNull()
                    }
                }
            }
        }

        companion object {
            private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
            private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
            private const val LAST_LINK = "last"

            private fun String.extractLinks(): Map<String, String> {
                val links = mutableMapOf<String, String>()
                val matcher = LINK_PATTERN.matcher(this)

                while (matcher.find()) {
                    val count = matcher.groupCount()
                    if (count == 2) {
                        links[matcher.group(2).orEmpty()] = matcher.group(1).orEmpty()
                    }
                }
                return links
            }
        }
    }

    /**
     * A class representing an error in the response.
     *
     * @param errorCode the http response error code (400, 401, 403, etc.)
     * @param errorMessage the http response message such as "Bad Request" when it is a 400
     * @param errorBody the http error body also known as the reason why the resource failed. It is nullable
     * because it is not always supplied in the response.
     *
     * An example of this could be when creating an album with a name that already exists. In that case, it would be:
     * [errorCode] = 400
     * [errorMessage] = "Bad Request
     * [errorBody] = "name already exists"
     */
    data class Error internal constructor(
        val throwable: Throwable?,
        val errorCode: Int,
        val errorMessage: String,
        val errorBody: String?,
        override val headers: Map<String, List<String>>
    ) : ApiResponse<Nothing>() {

        override fun <R> map(transform: (Nothing) -> R): ApiResponse<R> = this

        /**
         * This translates the [ApiResponse.Error.errorCode] into a [NetworkError]. These are
         * all of the errors that the Procore API can possibly send back when something goes wrong.
         *
         * This NewRelic query shows all of the error codes that mobile receives when hitting the API:
         * ```
         * SELECT count(*) as 'Volume' FROM Transaction WHERE application_name = 'Procore' AND email_address NOT LIKE '%procore%'
         * AND (os_name = 'Android' OR os_name = 'iOS') AND httpResponseCode NOT LIKE '2%' AND httpResponseCode NOT LIKE '3%'
         * LIMIT MAX SINCE 90 days ago FACET httpResponseCode
         * ```
         *
         * https://procoretech.atlassian.net/wiki/spaces/DEV/pages/1458995266/Procore+REST+API+Requirements#HTTP-status-codes-and-errors
         * https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
         * https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#client_error_responses
         */
        fun toNetworkError(): NetworkError = when (errorCode) {
            /* The server could not understand the request due to invalid syntax. (e.g. malformed request syntax) */
            400 -> NetworkError.BadRequest(errorMessage = errorBody ?: errorMessage)

            /*
             * Although the HTTP standard specifies "unauthorized", semantically this response means
             * "unauthenticated". That is, the client must authenticate itself to get the requested response.
             * Similar to 403 Forbidden, but specifically for use when authentication is required and has
             * failed or has not yet been provided.
             */
            401 -> NetworkError.Unauthorized

            /*
             * The client does not have access rights to the content so the server is refusing to give the requested
             * resource. This may be due to the user not having the necessary permissions for a resource or attempting
             * a prohibited action (e.g. creating a duplicate record where only one is allowed). Unlike 401, the client's
             * identity is known to the server.
             */
            403 -> NetworkError.Forbidden

            /* The server can not find the requested resource. */
            404 -> NetworkError.NotFound

            /* Indicates that the request method is known by the server but is not supported by the target resource. */
            405 -> NetworkError.MethodNotAllowed

            /*
             * Indicates that the server cannot produce a response matching the list of acceptable values defined in the
             * request's proactive content negotiation headers, and that the server is unwilling to supply a default representation.
             */
            406 -> NetworkError.NotAcceptable

            /*
             * The server timed out waiting for the request. According to HTTP specifications: "The client did not produce
             * a request within the time that the server was prepared to wait. The client MAY repeat the request without
             * modifications at any later time."
             */
            408 -> NetworkError.RequestTimeout

            /*
             * Indicates that the request could not be processed because of conflict in the current
             * state of the resource, such as an edit conflict between multiple simultaneous updates.
             */
            409 -> NetworkError.Conflict

            /*
             * Indicates that the resource requested is no longer available and will not be available again. This
             * should be used when a resource has been intentionally removed and the resource should be purged.
             */
            410 -> NetworkError.Gone

            /*
             * The server refuses to accept the request because the payload format is in an unsupported format.
             * The format problem might be due to the request's indicated Content-Type or Content-Encoding,
             * or as a result of inspecting the data directly.
             */
            415 -> NetworkError.UnsupportedMediaType

            /* The request was well-formed but was unable to be followed due to semantic errors. */
            422 -> NetworkError.UnprocessableEntity

            /* The resource that is being accessed is locked. */
            423 -> NetworkError.Locked

            /*
             * A generic error message, given when an unexpected condition was encountered and no more specific
             * message is suitable. The server has encountered a situation it doesn't know how to handle.
             */
            500 -> NetworkError.InternalServerError

            /* The server does not support the functionality required to fulfill the request. */
            501 -> NetworkError.NotImplemented

            /* The server, while acting as a gateway or proxy, received an invalid response from the upstream server. */
            502 -> NetworkError.BadGateway

            /* The server cannot handle the request because it is overloaded or down for maintenance. */
            503 -> NetworkError.ServiceUnavailable

            /* The catch-all case for whatever errors are left. */
            else -> NetworkError.Unknown(
                errorCode = errorCode,
                errorMessage = errorMessage,
                errorBody = errorBody
            )
        }

        /**
         * Returns a loggable string representing the error
         */
        fun loggableErrorString(): String = "code=[$errorCode], message=[$errorMessage], body=[$errorBody]"
    }

    /**
     * Separate class for HTTP 204 responses so that we can make [Success]'s body non-null.
     *
     * A 204 is defined as "No Content: the server has successfully fulfilled the request
     * and that there is no additional content to send in the response payload body."
     */
    data class Empty internal constructor(
        override val headers: Map<String, List<String>>
    ) : ApiResponse<Nothing>() {

        override fun <R> map(transform: (Nothing) -> R): ApiResponse<R> = this

        /**
         * Method to map an empty [ApiResponse] to a [NetworkError] object
         */
        fun toNetworkError(): NetworkError = NetworkError.Empty
    }
}