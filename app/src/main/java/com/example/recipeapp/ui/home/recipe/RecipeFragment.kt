package com.example.recipeapp.ui.home.recipe

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.model.User
import com.example.recipeapp.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_recipe.*
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import kotlinx.android.synthetic.main.recipe_card.view.*

class RecipeFragment : Fragment() {
    var selectedRecipe: Recipe.Result? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_recipe, container, false)

        Glide.with(root).load(selectedRecipe!!.imageUrl).into(root.iv_recipe_page)
        root.txt_title_page.text = selectedRecipe!!.title
        root.txt_time_page.text = selectedRecipe!!.readyInMinutes.toString()

        var dietString = ""
        for (diet in selectedRecipe!!.diets) {
            dietString += "$diet, "
        }

        var typeDishString = ""
        for (type in selectedRecipe!!.dishTypes) {
            typeDishString += "$type, "
        }

        var ingredientsString = ""
        for (ingredient in selectedRecipe!!.Ingredients) {
            ingredientsString += "${ingredient.name} + \n"
        }

        root.txt_allergies_page.text = dietString
        root.txt_typedish_page.text = typeDishString
        root.txt_ingredients_page.text = ingredientsString

        // makes the textview take html body
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            root.txt_intructions_page.text = Html.fromHtml(selectedRecipe!!.intructions, Html.FROM_HTML_MODE_LEGACY)
        } else {
            root.txt_intructions_page.text = Html.fromHtml(selectedRecipe!!.intructions)
        }

        val homeFragment = HomeFragment()
        homeFragment.randomRecipe = selectedRecipe

        return root
    }

}