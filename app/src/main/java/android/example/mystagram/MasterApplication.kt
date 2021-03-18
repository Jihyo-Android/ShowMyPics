package android.example.mystagram

import android.app.Application
import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication: Application() {

    lateinit var service: RetrofitService

    override fun onCreate() {
        super.onCreate()

        createRetrofit()
    }

    // Retrofit 생성
    fun createRetrofit() {
        val header = Interceptor {
            val original = it.request()

            if (checkIsLogIn()) {
                getUserToken()?.let {token ->
                    val request = original.newBuilder()
                        .header("Authorization", "token " + token)
                        .build()
                    it.proceed(request)
                }
            } else {
                it.proceed(original)
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }

    fun checkIsLogIn(): Boolean {
        val sharedPreference = getSharedPreferences("login_sharedPreference", Context.MODE_PRIVATE)
        val token = sharedPreference.getString("login_sharedPreference", "null")
        if (token != "null") return true
        else return false
    }

    fun getUserToken(): String? {
        val sharedPreference = getSharedPreferences("login_sharedPreference", Context.MODE_PRIVATE)
        val token = sharedPreference.getString("login_sharedPreference", "null")
        if (token == "null") return null
        else return token
    }
}