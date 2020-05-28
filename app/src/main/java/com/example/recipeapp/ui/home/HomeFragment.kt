package com.example.recipeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.adapter.RecipeAdapter
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.ui.home.recipe.RecipeFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.txt_title
import kotlinx.android.synthetic.main.recipe_card.view.*

/**
 * This is the controller behind the home screen.
 */
class HomeFragment : Fragment() {
    var account: FirebaseUser? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mDatabase: DatabaseReference
    private var randomRecipe: Recipe.Result? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        root.animation_view_product.visibility = View.VISIBLE
        root.linear_layout.visibility = View.GONE

        getNewRandomRecipe()

        homeViewModel.recipe.observe(viewLifecycleOwner, Observer {
            randomRecipe = it.results[0] // 0 because it has always 1 return, so the first item of the list

            root.animation_view_product.visibility = View.GONE
            root.linear_layout.visibility = View.VISIBLE

            /**
             * populate the view
             */
            Glide.with(root).load("https://spoonacular.com/recipeImages/" + randomRecipe!!.imageUrl).into(root.iv_recipe_home)
            root.txt_title.text = randomRecipe!!.title
            root.txt_time.text = randomRecipe!!.readyInMinutes.toString()

            var dietString = ""
            for (diet in randomRecipe!!.diets){
                dietString += "$diet, "
            }

            var typeDishString = ""
            for (type in randomRecipe!!.dishTypes){
                typeDishString += "$type, "
            }

            root.txt_allergies.text = dietString
            root.txt_items.text = randomRecipe!!.healthScore.toString()
            root.txt_typedish.text = typeDishString

            root.btn_no.setOnClickListener {
                clickNo()
            }

            root.btn_yes.setOnClickListener {
                clickYes(randomRecipe!!)
            }

            root.iv_recipe_home.setOnClickListener {
                clickRecipe(randomRecipe!!)
            }
        })

        return root
    }

    private fun clickRecipe(recipe: Recipe.Result) {
        // this starts the transaction
        val transaction = fragmentManager!!.beginTransaction()
        val recipeFragment = RecipeFragment()

        // set the selectedRecipe variable to recipe
        recipeFragment.selectedRecipe = recipe

        // replace the fragment
        transaction.replace(R.id.nav_host_fragment, recipeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun clickYes(recipe: Recipe.Result) {
        getNewRandomRecipe()

        //TODO safe to firebase
    }

    private fun clickNo() {
        getNewRandomRecipe()
    }

    private fun getNewRandomRecipe() {
        homeViewModel.getRecipe()
    }
}
