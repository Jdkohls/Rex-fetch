package com.example.reciperetriever

import com.google.gson.annotations.SerializedName

data class GsonRecipe (
    @SerializedName("recipe name" ) var recipeName: String? = null,
    @SerializedName("ingredients" ) var ingredients: ArrayList<String> = arrayListOf(),
    @SerializedName("prep time" ) var prepTime: Int? = null,
    @SerializedName("total time" ) var totalTime: Int? = null,
    @SerializedName("steps" ) var steps: ArrayList<String> = arrayListOf()
) {
    fun addToGlobals() {
        val newRecipe = Recipe(this.recipeName!!, this.ingredients, this.prepTime!!, this.totalTime!!, this.steps)
        //Globals.recipeArray.value!!.add(newRecipe)
    }
}