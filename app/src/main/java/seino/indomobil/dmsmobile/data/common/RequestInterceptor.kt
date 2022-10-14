package seino.indomobil.dmsmobile.data.common

import okhttp3.Interceptor
import okhttp3.Response
import seino.indomobil.dmsmobile.presentation.utils.SharedPrefs

class RequestInterceptor constructor(private val pref: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = pref.getToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}