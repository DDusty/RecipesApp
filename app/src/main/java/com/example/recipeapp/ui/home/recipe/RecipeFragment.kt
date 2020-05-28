package com.example.recipeapp.ui.home.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.model.User
import kotlinx.android.synthetic.main.fragment_recipe.*
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import kotlinx.android.synthetic.main.recipe_card.view.*

class RecipeFragment : Fragment() {
    var selectedRecipe: Recipe.Result? = null
    private var loggedInUser: User? = null
    private var isLoggedIn = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_recipe, container, false)

        // if loggedin then remove log in button else display and remove other
        if(isLoggedIn){
            root.imageView.visibility = View.GONE
            root.scrollview.visibility = View.GONE
        } else {
            root.imageView.visibility = View.VISIBLE
            root.scrollview.visibility = View.VISIBLE

            // login ritual and set boolean to true
        }

        Glide.with(root).load("https://spoonacular.com/recipeImages/" + selectedRecipe!!.imageUrl).into(root.imageView)
        root.txt_page_title.text = selectedRecipe!!.title


        return root
    }

}