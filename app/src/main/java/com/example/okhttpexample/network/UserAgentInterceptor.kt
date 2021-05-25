package com.example.okhttpexample.network

import android.app.Application
import com.example.okhttpexample.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class UserAgentInterceptor(application: Application) :
    Interceptor {

    private val userAgent: String

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithUserAgent = originalRequest
            .newBuilder()
            .header(
                USER_AGENT_LABEL,
                userAgent
            )
            .build()
        return chain.proceed(requestWithUserAgent)
    }

    init {
        val resources = application.resources
        userAgent = (resources.getString(R.string.app_name) + " Android "
            + resources.getString(R.string.version_name))
    }

    companion object {
        private const val USER_AGENT_LABEL = "User-Agent"
    }
}
