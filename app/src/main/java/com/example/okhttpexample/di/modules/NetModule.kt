package com.example.okhttpexample.di.modules

import android.app.Application
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import com.blackberry.okhttpsupport.interceptor.GDCustomInterceptor
import com.example.okhttpexample.network.ConnectivityHelper
import com.example.okhttpexample.network.CacheInterceptor
import com.example.okhttpexample.network.GitHubApiInterface
import com.example.okhttpexample.network.UserAgentInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule(var baseUrl: String) {
    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(
            application.cacheDir,
            cacheSize.toLong()
        )
    }

    @Provides // Dagger will only look for methods annotated with @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        application: Application,
        cache: Cache,
        userAgentInterceptor: UserAgentInterceptor,
        cacheInterceptor: CacheInterceptor
    ): OkHttpClient {

//        val gdCustomInterceptor = GDCustomInterceptor()
//        gdCustomInterceptor.disableHostVerification()
//        gdCustomInterceptor.disablePeerVerification()

        return OkHttpClient.Builder().apply {
            cache(cache)
            addInterceptor(userAgentInterceptor)
            addNetworkInterceptor(cacheInterceptor)
//            addInterceptor(gdCustomInterceptor)
            if (isApplicationDebuggable(application)) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
            }
        }.build()
    }

    private fun isApplicationDebuggable(application: Application): Boolean =
        0 != application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

    @Provides
    @Singleton
    fun providesConnectivityHelper(application: Application): ConnectivityHelper =
        ConnectivityHelper(application)

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesUserAgentInterceptor(application: Application): UserAgentInterceptor =
        UserAgentInterceptor(application)

    @Provides
    @Singleton
    fun providesCacheInterceptor(connectivityHelper: ConnectivityHelper): CacheInterceptor =
        CacheInterceptor(connectivityHelper)

    @Provides
    @Singleton
    fun providesGitHubInterface(retrofit: Retrofit): GitHubApiInterface {
        return retrofit.create(GitHubApiInterface::class.java)
    }
}
