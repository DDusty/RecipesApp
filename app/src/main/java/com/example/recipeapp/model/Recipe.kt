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
        @SerializedName("analyzedInstructions") var analyzedInstructions: List<AnalyzedInstructions> = listOf(),
        @SerializedName("ingredient") var missedIngredient: List<MissedIngredient> = listOf(),
        @SerializedName("dishTypes") var dishTypes: List<String> = listOf(),
        @SerializedName("diets") var diets: List<String> = listOf(),
        @SerializedName("healthScore") var healthScore: Double = 0.0
    ) : Serializable {
        data class MissedIngredient(
            @SerializedName("original") var original: String = ""
        ) : Serializable
        data class AnalyzedInstructions(
            @SerializedName("steps") var steps: List<Step> = listOf()
        ) : Serializable {
            data class Step(
                @SerializedName("number") var stepNumber: Int = 0,
                @SerializedName("step") var step: String = "",
                @SerializedName("ingredients") var ingredients: List<Ingredient> = listOf(),
                @SerializedName("equipment") var equipment: List<Equipment> = listOf()
            ) : Serializable {
                data class Ingredient(
                    @SerializedName("name") var ingredientName: String = ""
                ) : Serializable
                data class Equipment(
                    @SerializedName("name") var equipmentName: String = ""
                ) : Serializable
            }
        }
    }
}