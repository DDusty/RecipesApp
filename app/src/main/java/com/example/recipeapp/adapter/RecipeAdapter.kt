package com.example.recipeapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.model.Recipe
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.recipe_card.view.*

class RecipeAdapter(
    private val recipes: MutableList<Recipe.Result>,
    private val onClickListener: (Recipe.Result) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    private lateinit var database: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recipe_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])

        holder.itemView.setOnClickListener { view ->
            onClickListener.invoke(recipes[position])
        }
    }

    fun removeAt(position: Int, id: String) {
        database = FirebaseDatabase.getInstance().reference

        val recipe = recipes[position]
        recipes.removeAt(position)
        notifyItemRemoved(position)

        // removes the recipe from the list
        database.root.child("users").child(id).child("recipeList").child(recipe.id.toString()).setValue(null)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipe: Recipe.Result) {
            Glide.with(itemView).load(recipe.imageUrl).into(itemView.iv_recipe)
            itemView.txt_title.text = recipe.title
        }
    }
}