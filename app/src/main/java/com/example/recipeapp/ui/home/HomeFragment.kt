package com.example.recipeapp.ui.home

import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.recipeapp.OnSwipeTouchListener
import com.example.recipeapp.R
import com.example.recipeapp.data.database.DatabaseViewModel
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.ui.home.recipe.RecipeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * This is the controller behind the home screen.
 */
class HomeFragment : Fragment() {
    private var account: FirebaseUser? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var database: DatabaseReference
    var randomRecipe: Recipe.Result? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel::class.java)

        account = auth.currentUser

        root.animation_view_product.visibility = View.VISIBLE
        root.linear_layout.visibility = View.GONE

        if (randomRecipe == null) {
            getNewRandomRecipe()
        } else {
            setView()
        }

        root.animation_view_product.visibility = View.GONE
        root.linear_layout.visibility = View.VISIBLE

        root.btn_no.setOnClickListener {
            clickNo()
        }

        root.btn_yes.setOnClickListener {
            clickYes(randomRecipe!!, account!!)
        }

        root.iv_recipe_home.setOnClickListener {
            clickRecipe(randomRecipe!!)
        }

        root.setOnTouchListener(object: OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                clickYes(randomRecipe!!, account!!)
            }
            override fun onSwipeRight() {
                clickNo()
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

    private fun clickYes(recipe: Recipe.Result, acc: FirebaseUser) {
        // add recipe to list in firebase with the id as the key
        database.child("users").child(acc.uid).child("recipeList").child(recipe.id.toString()).setValue(recipe)
        Toast.makeText(this.requireContext(), "Added to list", Toast.LENGTH_SHORT).show()
        getNewRandomRecipe()
    }

    private fun clickNo() {
        getNewRandomRecipe()
    }

    private fun getNewRandomRecipe() {
        homeViewModel.getRecipe()

        homeViewModel.recipe.observe(viewLifecycleOwner, Observer {
            randomRecipe = it.results[0] // 0 because it has always 1 return, so the first item of the list
            setView()
        })
    }

    /**
     * Populates the view
     */
    private fun setView() {
        Glide.with(this)
            .load(randomRecipe!!.imageUrl)
            .into(this.iv_recipe_home)
        this.txt_title.text = randomRecipe!!.title
        this.txt_time.text = randomRecipe!!.readyInMinutes.toString()

        var dietString = ""
        for (diet in randomRecipe!!.diets) {
            dietString += "$diet, "
        }

        var typeDishString = ""
        for (type in randomRecipe!!.dishTypes) {
            typeDishString += "$type, "
        }

        this.txt_allergies.text = dietString
        this.txt_items.text = randomRecipe!!.healthScore.toString()
        this.txt_typedish.text = typeDishString
    }
}
