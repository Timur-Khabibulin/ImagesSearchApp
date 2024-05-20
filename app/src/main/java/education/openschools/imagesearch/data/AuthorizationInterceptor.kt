package education.openschools.imagesearch.data

import education.openschools.imagesearch.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val CONTENT_TYPE_HEADER = "Content-Type"
private const val API_KEY_HEADER = "X-API-KEY"
private const val APPLICATION_JSON = "application/json"

class AuthorizationInterceptor : Interceptor {

    private val apiKey: String = BuildConfig.API_KEY
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val request: Request = original.newBuilder()
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}
