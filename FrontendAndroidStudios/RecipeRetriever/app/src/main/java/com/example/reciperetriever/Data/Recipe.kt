package com.example.reciperetriever.Data

import com.example.reciperetriever.Recipe

data class OutgoingRecipe(val ingredients: MutableList<String>, val allergens: MutableList<String>, val prepTime: Int = -1, val totalTime: Int = -1, val recipeOptions: Int = -1, val missingIngredients: Int = -1, val substitutes: Int)