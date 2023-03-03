package com.example.rrhhapp.util

import android.content.Context
import com.example.rrhhapp.util.PreferenceHelper.set

object MyCustomPrefs {

    private const val SESSION_PREFS = "SESSION_PREFS"

    private const val JWT_KEY = "JWT_KEY"
    private const val TENANT_KEY = "TENANT_KEY"
    private const val USER_LOGGED_KEY = "USER_LOGGED_KEY"


    private fun getDefaultPrefs(context: Context) = PreferenceHelper.defaultPrefs(context)
    private fun getSessionPrefs(context: Context) =
        PreferenceHelper.customPrefs(context, SESSION_PREFS)


    fun getJwt(context: Context) = getDefaultPrefs(context).getString(JWT_KEY, "")
    fun setJwt(jwt: String, context: Context) =
        getDefaultPrefs(context).set(JWT_KEY, jwt)

    fun getTenant(context: Context) = getSessionPrefs(context).getString(TENANT_KEY, "")
    fun setTenant(tenant: String, context: Context) =
        getSessionPrefs(context).set(TENANT_KEY, tenant)


    fun getIsUserLogged(context: Context) =
        getSessionPrefs(context).getBoolean(USER_LOGGED_KEY, false)
    fun setIsUserLogged(isUserLogged: Boolean, context: Context) =
        getSessionPrefs(context).set(USER_LOGGED_KEY, isUserLogged)

    fun clearPrefs(context: Context){
        getDefaultPrefs(context).edit().clear().commit()
        getSessionPrefs(context).edit().clear().commit()
    }


}