package com.example.recipeapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.spoonacular.SpoonacularApiRepository
import com.example.recipeapp.model.Recipe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val repository = SpoonacularApiRepository()
    val recipe = MutableLiveData<Recipe>()
    val error = MutableLiveData<String>()

    fun getRecipe() {
        repository.getRecipes().enqueue(object : Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    recipe.value = response.body()
                }
                else {error.value = "An error occurred: ${response.errorBody().toString()}" }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                error.value = t.message
                Log.e("error", error.value)
            }
        })
    }

}