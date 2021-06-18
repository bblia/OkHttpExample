package com.example.okhttpexample.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CacheInterceptor(connectivityHelper: ConnectivityHelper): Interceptor {
    private val connectivityHelper = connectivityHelper

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
        val cacheHeader: String
        if (connectivityHelper.isConnected) {
            cacheHeader = "public, max-age=2419200"
            request.cacheControl(CacheControl.FORCE_NETWORK)
        } else {
            cacheHeader = "public, only-if-cached, max-stale=2419200"
        }
        val response = chain.proceed(request.build())
        return response
            .newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header(
                "Cache-Control",
                cacheHeader
            )
            .build()
    }
}
