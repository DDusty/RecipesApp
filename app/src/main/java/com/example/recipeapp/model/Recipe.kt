package com.example.recipeapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Recipe(
    @SerializedName("recipes") var results: List<Result>
) : Serializable {
    data class Result(
        @SerializedName("id") var id: Int = 0,
        @SerializedName("title") var title: String = "",
        @SerializedName("readyInMinutes") var readyInMinutes: Int = 0,
        @SerializedName("servings") var servings: Int = 0,
        @SerializedName("image") var imageUrl: String = "",
        @SerializedName("summary") var summary: String= "",
        @SerializedName("instructions") var intructions: String = "",
        @SerializedName("extendedIngredients") var Ingredients: List<Ingredient> = listOf(),
        @SerializedName("dishTypes") var dishTypes: List<String> = listOf(),
        @SerializedName("diets") var diets: List<String> = listOf(),
        @SerializedName("healthScore") var healthScore: Double = 0.0
    ) : Serializable {
        data class Ingredient(
            @SerializedName("original") var name: String = ""
        ) : Serializable
    }
}