package com.example.reciperetriever

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val name: String,
    val ingredients: MutableList<String>,
    val prepTime: Int, val totalTime: Int,
    val steps: MutableList<String>
)

@Serializable
//this holds the recipe data from the first 'search' API call
data class RecipeBrief(
        val id: Int,
        val title: String,
        val image: String,
        val imageType: String
)

//this holds the data for the detailed recipe gotten from second API call. This one is fucked,
//there's so much detail that the spoonacular API returns
@Serializable
data class RecipeDetailed(
        val preparationMinutes: Int,
        val cookingMinutes: Int,
        val readyInMinutes: Int,
        val extendedIngredients: String,
        val title: String,
        val summary: String,
        val instructions: String
)