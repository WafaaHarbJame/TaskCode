package com.interview.task
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.interview.task.Utils.SharedPManger
import java.util.*


class RootApplication : Application() {


    @get:Synchronized
    var sharedPManger: SharedPManger? = null

    companion object {

        var instance: RootApplication? = null
            private set
        var changesMap: MutableMap<String, Any> =
            HashMap()


        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        sharedPManger = SharedPManger(this)

        instance = this
    }

}