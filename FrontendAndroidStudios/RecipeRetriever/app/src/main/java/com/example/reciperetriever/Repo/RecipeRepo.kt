package com.example.reciperetriever.Repo

import android.util.Log
import com.example.reciperetriever.Data.OutgoingRecipe
import com.example.reciperetriever.Globals
import com.example.reciperetriever.GsonRecipe
//import com.example.reciperetriever.JWT
//import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import com.google.gson.Gson

const val spoonacular_url = "https://api.spoonacular.com"
const val API_KEY = "ea7e630e9bfa4292b713110fb362978d"
const val RECIPES_TO_FETCH = 10
class RecipeRepo {
    //repo that sends recipe-related requests to backend (get recipes, etc.)

    //send recipe requests directly to spoonacular API and parse the responses in front-end itself
    //I was unable to connect the frontend to our Backend and Database -Venky
    fun getRecipesFromIngredients(ingredients: String, allergens: String) : String{
        //Prereq: 'ingredients' must be a string formatted like 'eggs,chicken,testicles'

        //1. Send HTTPS request to Spoonacular.
        //2. Return the API response JSON to function caller

        //create the URL with the appropriate URL parameters
        var keyParam = "?apiKey=$API_KEY"
        var ingrParam = "&includeIngredients=$ingredients"
        var allergenParam = "&intolerances=$allergens"
        var ignorePantry = "&ignorePantry=true"
        var numParam = "&number=$RECIPES_TO_FETCH"
        var url = "$spoonacular_url/recipes/complexSearch"+keyParam+ingrParam+ignorePantry+numParam
        if(allergens != "") {
            url += allergenParam
        }

        Log.i("RecipeRepo", "ingr list was $ingredients and allergen list was $allergens")
        with(URL(url).openConnection() as HttpsURLConnection) {
            requestMethod = "GET"
            //set the URL parameters

            val response = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
            }
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                //spoonacular API return here. It will correspond with the "RecipeSearchJSON" struct
                //in RecipeClass.kt
                //Log.i("recipeRepo", "response was: $response")
                //this works
                return response.toString()
            }
            throw Exception("Failed to GET from API, responsecode: $responseCode")
        }
        return ""
    }

    fun getSpecificRecipe(recipe_id: Int) : String{
        //Prereq: 'ingredients' must be a string formatted like 'eggs,chicken,testicles'

        //1. Send HTTPS request to Spoonacular.
        //2. Return the API response JSON to function caller

        //create the URL with the appropriate URL parameters

        var keyParam = "?apiKey=$API_KEY"
        var url = "$spoonacular_url/recipes/$recipe_id/information$keyParam"

        with(URL(url).openConnection() as HttpsURLConnection) {
            requestMethod = "GET"

            val response = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
            }
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                //spoonacular API return here. It will correspond with the "RecipeSearchJSON" struct
                //in RecipeClass.kt
                //Log.i("recipeRepo", "response was: $response") this shit works
                return response.toString()
            }
            throw Exception("Failed to GET from API, responsecode: $responseCode")
        }
        return ""
    }
}