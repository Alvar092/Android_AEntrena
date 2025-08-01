package com.aentrena.db19aentrena

import android.app.Application
import com.aentrena.db19aentrena.di.respositoryModule
import com.aentrena.db19aentrena.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DBApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DBApp)
            modules(respositoryModule, viewModelModule)
        }
    }
}