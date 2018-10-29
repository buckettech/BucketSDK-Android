package bucket.sdk.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WebServiceHelper {

    inline fun <reified T> createWebService(baseUrl: String, vararg defaultHeaders: Pair<String, String>): T {
        // okhttp client
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60L, TimeUnit.SECONDS)  // 60s connection timeout
                .readTimeout(60L, TimeUnit.SECONDS)     // 60s read timeout
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })   // print response body

        // add headers
        for (header in defaultHeaders) {
            val (headerName, headerValue) = header
            okHttpClient.addInterceptor { chain -> chain.proceed(chain.request().newBuilder().addHeader(headerName, headerValue).build()) }
        }

        // retrofit
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(T::class.java)
    }

}