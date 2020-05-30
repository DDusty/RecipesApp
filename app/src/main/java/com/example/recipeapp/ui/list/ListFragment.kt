package com.example.recipeapp.ui.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.MainActivity
import com.example.recipeapp.R
import com.example.recipeapp.SwipeToDeleteCallback
import com.example.recipeapp.adapter.RecipeAdapter
import com.example.recipeapp.data.database.DatabaseViewModel
import com.example.recipeapp.data.database.OnGetDataListener
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.model.User
import com.example.recipeapp.ui.home.HomeFragment
import com.example.recipeapp.ui.home.recipe.RecipeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.coroutines.Dispatchers

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

        setHasOptionsMenu(true)

        val rvList: RecyclerView = root.findViewById(R.id.rv_list)

        // Attach the adapters to the recyclerviews
        rvList.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvList.adapter = recipeAdapter

        val swipeHandler = object : SwipeToDeleteCallback(this.requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvList.adapter as RecipeAdapter
                adapter.removeAt(viewHolder.adapterPosition, account!!.uid)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvList)

        auth = FirebaseAuth.getInstance()
        account = auth.currentUser

        if(account != null) loadData(account) else Log.d("listfragment ", "account is null")

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
}
