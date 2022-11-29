package com.example.everypractice.data.red

import okhttp3.logging.HttpLoggingInterceptor

fun httpTimberInterceptor(): HttpLoggingInterceptor {
    val customLogger = object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            //Log.d("OkHttp", message)
            //Timber.tag("OkHttp").v(message)
        }
    }
    return HttpLoggingInterceptor(customLogger)
}

fun HttpLoggingInterceptor.bodyLevel(): HttpLoggingInterceptor {
    level = HttpLoggingInterceptor.Level.BODY
    return this
}
