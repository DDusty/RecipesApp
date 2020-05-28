package com.example.recipeapp.data.spoonacular
import com.example.recipeapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpoonacularApi {
    companion object {
        // The base url off the api.
        private var baseUrl =
            "https://api.spoonacular.com/"

        fun createApi(): SpoonacularApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            val shopifyApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return shopifyApi.create(SpoonacularApiService::class.java)
        }
    }
}