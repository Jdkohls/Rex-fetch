package com.example.reciperetriever

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reciperetriever.databinding.RecipesMainBinding
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ScrollableRecipePage : Fragment(), CallbackInterface {
    private var _binding: RecipesMainBinding? = null
    private var callback: CallbackInterface? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecipesMainBinding.inflate(inflater, container, false)
        syncData(this)
        reInitRecyclerViewFilter()
        reInitRecyclerView(callback)
        return binding.root

    }

//    Note: Read comment on ./FridgePage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Globals.recipeArray.observe(viewLifecycleOwner) { recipes ->
            // Update the RecyclerView adapter with the new recipe array
            reInitRecyclerView(callback)
        }
        reInitRecyclerViewFilter()
        reInitRecyclerView(callback)
        binding.getRecipesButton.setOnClickListener {
            //Log.i("checkBox Check", "items that are checked: ${Globals.filteredArray}")
            //checkbox functionality works
            //WE WANT TO MINIMIZE THE API CALLS

            //1. Check if we have at least 4 ingredients that user has.
            if(Globals.filteredArray.size < 4) {
                Toast.makeText(context, "Too few ingredients to search for recipes.",
                    Toast.LENGTH_LONG).show()
            } else {
                //we have enough ingredients to populate the list,
                //2. call Globals function to get recipes.
                if(Globals.api_calls_made < 10) {
                    try {
                        Globals.getRecipes()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Sorry, too many recipe searches were made. " +
                            "Try again later.", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.pickMyMealButton.setOnClickListener {
            // Choose a recipe randomly from the list gotten from server and show it's details
            if((Globals.recipeArray.value?.size ?: 0) != 0) {
                //if we have some recipes from server, choose for the user
                val randomId = Random.nextInt(0, Globals.recipeArray.value?.size ?: 0)
                Globals.recipe_id = Globals.recipeArray.value?.get(randomId)?.id ?: 0
                Globals.getSpecificRecipe()
                findNavController().navigate(R.id.action_recipes_to_specific_recipe)
            }
        }
//        // Callback Listener
//        callbackInterface.onClickEvent {
//            findNavController().navigate(R.id.action_recipes_to_specific_recipe)
//        }
        // Navigation
        binding.homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_recipe_to_home)
        }
    }

    private fun reInitRecyclerView(mCallback: CallbackInterface?) {
        val recipeRecyclerView = binding.recipeRecyclerView
        //recipeRecyclerView.adapter = RecipeAdapter(Globals.recipeArray, mCallback)
        recipeRecyclerView.adapter = Globals.recipeArray.value?.let { RecipeAdapter(it, mCallback) }
        recipeRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun reInitRecyclerViewFilter() {
        val filterRecyclerView = binding.filterRecyclerView
        filterRecyclerView.adapter = FilterAdapter(Globals.fridgeArray)
        filterRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick() {
        Globals.getSpecificRecipe()
        findNavController().navigate(R.id.action_recipes_to_specific_recipe)
    }

    private fun syncData(mCallback: CallbackInterface) {
        Log.d("=====================================================", "Calling all backs")
        this.callback = mCallback
    }
}