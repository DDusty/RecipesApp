package com.example.recipeapp.data.spoonacular

class SpoonacularApiRepository {
    private val spoonacularApiService: SpoonacularApiService = SpoonacularApi.createApi()

    fun getRecipes() = spoonacularApiService.getRecipes()
}