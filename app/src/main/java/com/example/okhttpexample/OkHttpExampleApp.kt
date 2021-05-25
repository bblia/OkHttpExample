package com.example.okhttpexample

import android.app.Application
import com.example.okhttpexample.di.components.DaggerNetComponent
import com.example.okhttpexample.di.components.NetComponent
import com.example.okhttpexample.di.modules.AppModule
import com.example.okhttpexample.di.modules.NetModule

class OkHttpExampleApp : Application() {
    lateinit var netComponent: NetComponent

    override fun onCreate() {
        super.onCreate()

        netComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule("https://api.github.com"))
            .build()
    }
}
