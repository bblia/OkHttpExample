package com.example.okhttpexample.di.components

import com.example.okhttpexample.MainActivity
import com.example.okhttpexample.di.modules.AppModule
import com.example.okhttpexample.di.modules.NetModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface NetComponent {
    fun inject(activity: MainActivity)
}
