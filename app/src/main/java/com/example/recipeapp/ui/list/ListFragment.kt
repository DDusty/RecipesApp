package com.example.recipeapp.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapter.RecipeAdapter
import com.example.recipeapp.data.database.DatabaseViewModel
import com.example.recipeapp.data.database.OnGetDataListener
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {
    private lateinit var databaseViewModel: DatabaseViewModel
    var account: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth

    // recipe adapter
    private val recipes = arrayListOf<Recipe.Result>()
    private val recipeAdapter = RecipeAdapter(recipes, onClickListener = this::clickRecipe)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        databaseViewModel =
            ViewModelProviders.of(this).get(DatabaseViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val rvList: RecyclerView = root.findViewById(R.id.rv_list)

        // Attach the adapters to the recyclerviews
        rvList.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvList.adapter = recipeAdapter

        auth = FirebaseAuth.getInstance()
        account = auth.currentUser

        if(account != null) loadData(account) else Log.d("listfragment ", "account is null")

        return root
    }

    fun loadData(acc: FirebaseUser?) {
        // empties the array
        recipes.clear()

        databaseViewModel.readDataOnce("users", object :
            OnGetDataListener {
            override fun onSuccess(@NonNull data: DataSnapshot) {
                val children = data.children
                children.forEach {
                    // for every record check if the key exists or not, if not save new user
                    if (it.key.equals(acc!!.uid)) {
                        // if the table recipeList exists
                        if(it.child("recipeList").exists()){
                            // for every record inside recipeList add it to recipes
                            it.child("recipeList").children.forEach{
                                recipes.add(it.getValue(Recipe.Result::class.java)!!)
                                recipeAdapter.notifyDataSetChanged()
                            }
                        } else {
                            Toast.makeText(this@ListFragment.requireContext(), "No recipes liked", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailed(databaseError: DatabaseError) {
                Log.d("NO DATA ERROR: ", databaseError.message)
            }

        })


    }

    private fun clickRecipe(view: View, recipe: Recipe.Result) {
        Toast.makeText(this.requireContext(), "clicked on ${recipe.title}", Toast.LENGTH_SHORT).show()
    }
}
