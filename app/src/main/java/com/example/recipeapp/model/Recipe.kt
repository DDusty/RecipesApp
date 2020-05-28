package com.example.recipeapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Recipe(
    @SerializedName("recipes") var results: List<Result>
) : Serializable {
    data class Result(
        @SerializedName("id") var id: Int,
        @SerializedName("title") var title: String,
        @SerializedName("readyInMinutes") var readyInMinutes: Int,
        @SerializedName("servings") var servings: Int,
        @SerializedName("image") var imageUrl: String,
        @SerializedName("summary") var summary: String,
        @SerializedName("instructions") var intructions: String,
        @SerializedName("analyzedInstructions") var analyzedInstructions: List<AnalyzedInstructions>,
        @SerializedName("ingredient") var missedIngredient: List<MissedIngredient>,
        @SerializedName("dishTypes") var dishTypes: List<String>,
        @SerializedName("diets") var diets: List<String>,
        @SerializedName("healthScore") var healthScore: Double
    ) : Serializable {
        data class MissedIngredient(
            @SerializedName("original") var original: String
        ) : Serializable
        data class AnalyzedInstructions(
            @SerializedName("steps") var steps: List<Step>
        ) : Serializable {
            data class Step(
                @SerializedName("number") var stepNumber: Int,
                @SerializedName("step") var step: String,
                @SerializedName("ingredients") var ingredients: List<Ingredient>,
                @SerializedName("equipment") var equipment: List<Equipment>
            ) : Serializable {
                data class Ingredient(
                    @SerializedName("name") var ingredientName: String
                ) : Serializable
                data class Equipment(
                    @SerializedName("name") var equipmentName: String
                ) : Serializable
            }
        }
    }
}