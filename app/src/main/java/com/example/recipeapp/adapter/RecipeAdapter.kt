package com.example.recipeapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.model.Recipe
import kotlinx.android.synthetic.main.recipe_card.view.*


class RecipeAdapter(
    private val recipes: List<Recipe.Result>,
    private val onClickListener: (View, Recipe.Result) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

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
            onClickListener.invoke(view, recipes[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipe: Recipe.Result) {
            Glide.with(itemView).load("https://spoonacular.com/recipeImages/" + recipe.imageUrl).into(itemView.iv_recipe)
            itemView.txt_title.text = recipe.title
        }
    }
}