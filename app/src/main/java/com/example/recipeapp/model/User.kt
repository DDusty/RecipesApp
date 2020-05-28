package com.example.recipeapp.model

import java.io.Serializable

data class User (
    var id: String,
    var recipeList: List<Recipe.Result> = listOf()
) : Serializable