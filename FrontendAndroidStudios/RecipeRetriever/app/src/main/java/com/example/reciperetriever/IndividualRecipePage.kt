package com.example.reciperetriever

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reciperetriever.databinding.SpecificRecipeBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class IndividualRecipePage : Fragment() {

    private var _binding: SpecificRecipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SpecificRecipeBinding.inflate(inflater, container, false)
        reInitRecyclerViewFood()
        return binding.root

    }

//    Note: Read comment on ./FridgePage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Globals.recipe_details.observe(viewLifecycleOwner) { recipes ->
            // Update the RecyclerView adapter with the new recipe array
            reInitRecyclerViewFood()
            binding.RecipePageHeader.text = Globals.recipe_details.value?.title ?: "hello world"
            binding.displayStepsText.text = Globals.recipe_details.value?.instructions ?: "hello world"
            binding.prepTimeText.text = "Prep Time: " + Globals.recipe_details.value?.preparationMinutes.toString() + " minutes"
            binding.cookTimeText.text = "Total Time: " + Globals.recipe_details.value?.cookingMinutes.toString() + " minutes"
        }

        super.onViewCreated(view, savedInstanceState)
        // Sets up Current Recipe
        reInitRecyclerViewFood()
        binding.RecipePageHeader.text = "Example"
        binding.prepTimeText.text = "Prep Time: " + Globals.recipe_details.value?.preparationMinutes.toString() + " minutes"
        binding.cookTimeText.text = "Total Time: " + Globals.recipe_details.value?.cookingMinutes.toString() + " minutes"
        binding.displayStepsText.text = Globals.recipe_details.value?.instructions ?: "hello world"
        // Navigation
        binding.recipesButton.setOnClickListener {
            findNavController().navigate(R.id.action_specific_recipe_to_recipes)
        }
    }

    private fun reInitRecyclerViewFood() {
        val ingredientsRecyclerView = binding.ingredientsRecyclerView
        ingredientsRecyclerView.adapter = Globals.recipe_details.value?.let { InfoAdapter(it.extendedIngredients) }
        ingredientsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}