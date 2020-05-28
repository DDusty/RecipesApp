package com.example.recipeapp.data.spoonacular
import com.example.recipeapp.BuildConfig
import com.example.recipeapp.model.Recipe
import retrofit2.Call
import retrofit2.http.*

interface SpoonacularApiService {
    @GET("/recipes/random?number=1&apiKey=" + BuildConfig.API_KEY)
    fun getRecipes() : Call<Recipe>
}