package com.example.everypractice.prinoptions.movies.red

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $API_TOKEN_KEY_V4")
            .addHeader("Content-Type","application/json;charset=utf-8")

            .build()
        return chain.proceed(request)


        //EN ESTA PETICION EL API KEY NO VA COMO HEADER, POR LO QUE NO PODEMOS USAR EL DE ARRIBA
        //TENEMOS QUE PONERLO EN LA MISMA URL
        /*val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer " + API_TOKEN_KEY_V4)
            .addHeader("Content-Type","application/json;charset=utf-8")
        val httpBuilder = chain.request().url.newBuilder()
            //.addQueryParameter("api_key", API_KEY)
        request.url(httpBuilder.build())
        return chain.proceed(request.build())*/
    }
}
