package com.example.okhttpexample.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(application: Application) {
    var application = application

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }
}
