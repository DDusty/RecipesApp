package com.example.recipeapp.ui.home

import android.app.ActionBar
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.recipeapp.MainActivity
import com.example.recipeapp.OnSwipeTouchListener
import com.example.recipeapp.R
import com.example.recipeapp.data.database.DatabaseViewModel
import com.example.recipeapp.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers.Main


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

        setHasOptionsMenu(true)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel::class.java)

        account = auth.currentUser

        root.animation_view_recipe.visibility = View.VISIBLE
        root.linear_layout.visibility = View.GONE

        if (randomRecipe == null) {
            getNewRandomRecipe()
        } else {
            setView()
        }

        root.setOnTouchListener(object: OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                clickNo()
            }
            override fun onSwipeRight() {
                clickYes(randomRecipe!!, account!!)
            }
        })

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        // clicked on logout button
        R.id.logout_menu -> {
            signOut()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this.requireContext(), "signed out", Toast.LENGTH_LONG).show()

        // go back to main activity
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun clickYes(recipe: Recipe.Result, acc: FirebaseUser) {
        // add recipe to list in firebase with the id as the key
        database.child("users").child(acc.uid).child("recipeList").child(recipe.id.toString()).setValue(recipe)
        Toast.makeText(this.requireContext(), "Added to list", Toast.LENGTH_SHORT).show()
        getNewRandomRecipe()
        showLessInfo()
    }

    private fun clickNo() {
        getNewRandomRecipe()
        showLessInfo()
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
        animation_view_recipe.visibility = View.GONE
        linear_layout.visibility = View.VISIBLE

        btn_no.setOnClickListener {
            clickNo()
        }

        btn_yes.setOnClickListener {
            clickYes(randomRecipe!!, account!!)
        }

        txt_showMore.setOnClickListener {
            showExtraInfo()
        }

        Glide.with(this)
            .load(randomRecipe!!.imageUrl)
            .into(iv_recipe_home!!)
        txt_title!!.text = randomRecipe!!.title
        txt_time!!.text = randomRecipe!!.readyInMinutes.toString()

        var dietString = ""
        for (diet in randomRecipe!!.diets) {
            dietString += "$diet, "
        }

        var typeDishString = ""
        for (type in randomRecipe!!.dishTypes) {
            typeDishString += "$type, "
        }

        txt_allergies!!.text = dietString
        txt_typedish!!.text = typeDishString

        // sets the underline of the textview
        txt_showMore.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private fun showExtraInfo() {
        txt_showMore.visibility = View.GONE
        extra_info.visibility = View.VISIBLE

        txt_showLess.setOnClickListener {
            showLessInfo()
        }

        // sets the underline of the textview
        txt_showLess.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        var ingredientsString = ""
        for (ingredient in randomRecipe!!.Ingredients) {
            ingredientsString += "${ingredient.name} \n"
        }

        txt_ingredients_home.text = ingredientsString

        // makes the textview take html body
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            txt_intructions_home.text = Html.fromHtml(randomRecipe!!.intructions, Html.FROM_HTML_MODE_LEGACY)
        } else {
            txt_intructions_home.text = Html.fromHtml(randomRecipe!!.intructions)
        }

    }

    private fun showLessInfo() {
        extra_info.visibility = View.GONE
        txt_showMore.visibility = View.VISIBLE
    }
}
