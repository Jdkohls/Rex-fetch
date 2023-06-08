package com.example.reciperetriever

import androidx.lifecycle.MutableLiveData
import com.example.reciperetriever.Repo.LoginRepo
import com.example.reciperetriever.Repo.RecipeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

@Serializable
data class JWT (var jwt: String)

object Globals {
    var fridgeArray: MutableList<String> = ArrayList()
    var filteredArray: MutableList<String> = ArrayList()
    var groceryArray: MutableList<String> = ArrayList()
    var loginDict = mutableMapOf<String, String>()
    var recipeArray =  MutableLiveData<MutableList<RecipeBrief>>()
    var recipeSearchReturn = ArrayList<RecipeBrief>()
    var api_calls_made = 0
    var recipe_id = 0 //id of the specific recipe that was clicked by user to cook
    var recipe_details = MutableLiveData<RecipeDetailed>() //specific recipe details gotten from API
    var exampleRecipeBrief = RecipeBrief(75610, "example", "lol.com", "jaypeg")

    var randomizeArray: MutableList<Recipe> = ArrayList()
    var exampleRecipe: Recipe = Recipe("Example Recipe", mutableListOf("Green Eggs", "Ham"), 5, 60, mutableListOf("Call Poison Control", "Die?"))
    var currentRecipe: Recipe = exampleRecipe
    var loggedIn = false
    var user_jwt = JWT("") //variable to save the JWT that the backend returns when we login/create user
    var userId = 0

    // User Profile & Data (Most of this stuff will go backend when developed)
    var allergenArray: MutableList<String> = ArrayList()
    var substitutes = 2
    var name = "User"

    // Adds a string to an array if it's not already in it. Return true if successfully added and false if it's not added
    fun addStrToArray(str: String, arr: MutableList<String>): Boolean {
        return if (arr.indexOf(str) == -1) {
            arr += str
            true
        } else {
            false
        }
    }

    // Removes a string from an array of strings. Returns int: either index of the removed item or -1 if item not in array
    fun remStrFromArray(str: String, arr: MutableList<String>): Int {
        val returnedIndex = arr.indexOf(str)
        if (returnedIndex != -1) {
            arr.remove(str)
        }
        return returnedIndex
    }

    // Moves a string from array1 to array2. Returns removed string's index in arr1 if successful, else, -1.
    fun mvStrFromArr1ToArr2(
        str: String,
        arr1: MutableList<String>,
        arr2: MutableList<String>
    ): Int {
        val success = remStrFromArray(str, arr1)
        if (success != -1) {
            addStrToArray(str, arr2)
        }
        return success
    }

    fun rndStrFromArr(arr: MutableList<Recipe>): String {
        return if (arr.size > 0) {
            var ind = Random.nextInt(0, arr.size)
            if (ind == arr.size) {
                ind -= 1
            } // I fear for the safety of my precious program
            currentRecipe = arr[ind]
            arr[ind].name
        } else {
            "-1"
        }
    }

    // Deep Copy an array... theoretically
    fun deepCopy(src: MutableList<String>, dst: MutableList<String>) {
        for (item in src) {
            addStrToArray(item, dst)
        }
        /* I dislike the fact that I had to even make this */
    }

    // Dictionaries
    // Adds a (Key: Value) Pair to the dictionary
    fun addKeyValToDict(k: String, v: String, dict: MutableMap<String, String>): Boolean {
        return if (k in dict) {
            false
        } else {
            dict[k] = v
            true
        }
    }

    // Checks if the (Key: Value) Pair is in the dictionary
    fun keyValInDict(k: String, v: String, dict: MutableMap<String, String>): Boolean {
        // Who decided it was a good idea to make contains == containsKey and just not implement any actual function for contains. Brain dead fr
        if (dict.containsKey(k)) {
            if (dict[k] == v) {
                return true
            }
        }
        return false
    }
    fun checkUsername(username: String, dict: MutableMap<String, String>): Boolean {
        return dict.containsKey(username)
    }
    fun checkPassword(username: String, password: String, dict: MutableMap<String, String>): Boolean {
        if(checkUsername(username, dict)) {
            if(dict[username] == password) return true
            return false
        }
        return false
    }
    fun authenticationLoginDict(username: String, password: String): Boolean {
        // Regex Verification (For later)
        // Jackson type stuff here - Check if valid before sending it off to backend for speed.
        // If username/password in LoginDict - return true, else if username in LoginDict but password not (ret false), else if none in loginDict, add it in (Register)
        return if (keyValInDict(username, password, loginDict)) {
            true // Correct Login
        } else if (loginDict.containsKey(username)) {
            false // Wrong Password
        } else {
            loginDict[username] = password
            true
        }
    }

    // Adds a Recipe to the Randomizer Array || Returns -1 if removed or 1 if added
    fun addRemoveToRandomizer(recipe: Recipe): Int {
        for (item: Recipe in randomizeArray) {
            if (item.name == recipe.name) {
                randomizeArray.remove(item)
                return -1
            }
        }
        randomizeArray.add(recipe)
        return 1
    }

    //functions that send requests/data to backend
    fun login(username: String, password: String) {
        //send_login_request returns a JSON object of the form: {"jwt":"something"}
        GlobalScope.launch(Dispatchers.IO) {
            try {
                user_jwt = LoginRepo().send_login_request(username, password)
            }
            catch (e: Exception){
                //HTTP request error happened. Make a toast or something?
            }
        }
    }
    fun new_user(username: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                user_jwt = LoginRepo().send_create_user_request(username, password)
            }
            catch (e: Exception){
                //HTTP request error happened. Make a toast or something?
            }
        }
    }
    fun update_username(username: String, password: String, new_username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                user_jwt = LoginRepo().send_update_user_request(username, password, new_username)
            }
            catch (e: Exception){
                //HTTP request error happened. Make a toast or something?
            }
        }
    }
    fun stringify(arr: MutableList<String>): String {
        //get stringified version of the arraylist (of ingredients or allergens)
        var ret = ""
        for(i in 0 until arr.size) {
            ret += arr[i]
            if(i+1 != arr.size) {
                ret+=","
            }
        }
        return ret
    }

    fun getRecipes() {
        //launch thread for API call, done through RecipeRepo
        //using spoonacular's "complex search" endpoint with specific parameters

        GlobalScope.launch(Dispatchers.IO) {
            try {
                var jsonString = RecipeRepo().getRecipesFromIngredients(stringify(filteredArray), stringify(allergenArray))
                api_calls_made += 1
                val jsonObject = JSONObject(jsonString)
                val resultsArray: JSONArray = jsonObject.getJSONArray("results")
                if(resultsArray.length() == 0) {
                    //no recipes returned
                    throw Exception("No recipes found for these ingredients.")
                }
                for (i in 0 until resultsArray.length()) {
                    val recipeObject = resultsArray.getJSONObject(i)
                    val id = recipeObject.getInt("id")
                    val title = recipeObject.getString("title")
                    val image = recipeObject.getString("image")
                    val imageType = recipeObject.getString("imageType")
                    // Create a RecipeBrief object with the extracted data
                    val recipeBrief = RecipeBrief(id, title, image, imageType)
                    recipeSearchReturn.add(recipeBrief) //store the returned recipe
                }
                //Log.i("GetRecipes", "after parsing api return, we have this object: $recipeSearchReturn")
                //Let's go all that above shit works.
                recipeArray.postValue(recipeSearchReturn)
                if(recipeSearchReturn[0].title == "") {
                    recipeArray.postValue(arrayListOf(exampleRecipeBrief))
                }

            }
            catch (e: Exception){
                //HTTP request error happened. Make a toast or something?
                e.printStackTrace()
            }
        }
    }
    fun getSpecificRecipe() {
        GlobalScope.launch(Dispatchers.IO){
            try {
                var jsonString = RecipeRepo().getSpecificRecipe(recipe_id)
                val jsonObject = JSONObject(jsonString)
                //extract the things we need and create a RecipeDetailed object from them, then
                //postValue on Globals.recipe_details to display it
                val prepMins = jsonObject.getInt("preparationMinutes")
                val cookingMins = jsonObject.getInt("cookingMinutes")
                val readyInMins = jsonObject.getInt("readyInMinutes")
                val ingr_arr: JSONArray = jsonObject.getJSONArray("extendedIngredients")
                val title = jsonObject.get("title") as String
                val summary = jsonObject.get("summary") as String
                val instructions = jsonObject.get("instructions") as String
                var ingr_arr_str = ""
                for (i in 0 until ingr_arr.length()) {
                    val ingr_obj = ingr_arr[i] as JSONObject
                    val name = ingr_obj.get("name")
                    ingr_arr_str += name
                    if(i != ingr_arr.length() - 1) {
                        ingr_arr_str += ", "
                    }
                }
                //extracted all the data we need
                val recipeDetails = RecipeDetailed(prepMins, cookingMins, readyInMins,
                        ingr_arr_str, title, summary, instructions)

                //Log.i("GetRecipes", "after parsing api return, we have this object: recipeDetails")
                //this works too.
                recipe_details.postValue(recipeDetails)

            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }
}