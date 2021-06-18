package com.example.okhttpexample

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.example.okhttpexample.di.components.DaggerNetComponent
import com.example.okhttpexample.di.components.NetComponent
import com.example.okhttpexample.di.modules.AppModule
import com.example.okhttpexample.di.modules.NetModule
import com.good.gd.GDAndroid
import com.good.gd.GDAppEvent
import com.good.gd.GDAppEventListener
import com.good.gd.GDAppEventType
import com.good.gd.GDStateListener
import com.good.gd.ui.GDInternalActivity

class OkHttpExampleApp : Application(), GDStateListener, Application.ActivityLifecycleCallbacks,
    GDAppEventListener {
    lateinit var netComponent: NetComponent

    override fun onCreate() {
        super.onCreate()
        GDAndroid.getInstance().setGDStateListener(this)
        GDAndroid.getInstance().setGDAppEventListener(this)
        registerActivityLifecycleCallbacks(this)

        netComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule("https://api.github.com"))
            .build()
    }

    // https://developers.blackberry.com/us/en/resources/get-started/blackberry-dynamics-getting-started.html?platform=android#step-3
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        // if activity is not an instance of GDInternalActivity that means the SDK has
        // not been initialized for that activity yet. Must initialize.
        if ((activity is GDInternalActivity).not()) {
            GDAndroid.getInstance().activityInit(activity)
        }
    }

    // GDAppEventListener
    override fun onGDEvent(event: GDAppEvent?) {
        when (event?.eventType) {
            GDAppEventType.GDAppEventAuthorized,
            GDAppEventType.GDAppEventNotAuthorized,
            GDAppEventType.GDAppEventRemoteSettingsUpdate,
            GDAppEventType.GDAppEventServicesUpdate,
            GDAppEventType.GDAppEventPolicyUpdate,
            GDAppEventType.GDAppEventEntitlementsUpdate ->
                Log.i("GDEvent: %s", event.toString())
        }
    }

    // GDStateListener
    override fun onLocked() {
        Log.i("GD: Lock", "")
    }

    override fun onWiped() {
        Log.i("GD: Wipe", "")
    }

    override fun onUpdateConfig(config: MutableMap<String, Any>?) {
        Log.i("GD: Update Config", "")
    }

    override fun onUpdateServices() {
        Log.i("GD: UpdateServices", "")
    }

    override fun onAuthorized() {
        Log.i("GD: Authorized", "")
    }

    override fun onUpdateEntitlements() {
        Log.i("GD: Update Entitlements", "")
    }

    override fun onUpdatePolicy(policy: MutableMap<String, Any>?) {
        Log.i("GD: Update Policy", "")
    }

    // empty activity lifecycle methods
    override fun onActivityPaused(activity: Activity) {
        // do nothing
    }

    override fun onActivityStarted(activity: Activity) {
        // do nothing
    }

    override fun onActivityDestroyed(activity: Activity) {
        // do nothing
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        // do nothing
    }

    override fun onActivityStopped(activity: Activity) {
        // do nothing
    }

    override fun onActivityResumed(activity: Activity) {
        // do nothing
    }
}
