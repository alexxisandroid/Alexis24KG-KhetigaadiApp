package com.khetigaadi.dagger.module
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
class TokenAuthenticator @Inject constructor(private val appContext:Context,):Authenticator {
    override fun authenticate(route:Route?,response:Response):Request? {
        return runBlocking {
            var intent = Intent("anEvent")
            intent.putExtra("key", TAG)
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent)
            return@runBlocking null
        }
    }
    companion object {
        const val TAG = "TokenAuthenticator"
    }
}