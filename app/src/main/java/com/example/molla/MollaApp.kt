package com.example.molla

import android.app.Application

class MollaApp : Application() {
    var userId: Long? = null;
    var username: String? = null;
    var email: String? = null;

    companion object {
        lateinit var instance: MollaApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isLoggedIn(): Boolean {
        return userId != null && username != null && email != null
    }
}