package com.example.reciperetriever

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reciperetriever.databinding.HomeBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Home : Fragment() {

    private var _binding: HomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomeBinding.inflate(inflater, container, false)
        return binding.root

    }

//    Note: Read comment on ./FridgePage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Sets Welcome Text
        binding.welcomeTextView.text = "Welcome " + Globals.name
        // Reset Recipe Page Stuff For Ease
        //Globals.recipeArray = ArrayList()
        Globals.randomizeArray = ArrayList()
        Globals.deepCopy(Globals.fridgeArray, Globals.filteredArray) // Creates a DEEP copy (If someone changes this to a shallow copy then this code *will* crash and I blame you)
        // Navigation
        binding.firstPageButton.setOnClickListener {
            // TODO: Create Call To Backend using Globals.filteredArray not Globals.fridgeArray --- IMPORTANT
            //Globals.recipeArray.add(Globals.exampleRecipe) // Can take this out later, just for testing purposes
            //Globals.recipeArray.value?.add(Globals.exampleRecipe) // Can take this out later, just for testing
            // Nav to Recipe Page
            findNavController().navigate(R.id.action_home_to_recipes)
        }
        binding.secondPageButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_fridge)
        }
        binding.thirdPageButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_grocery)
        }
        binding.fourthPageButton.setOnClickListener {
            findNavController().navigate(R.id.action_goto_profile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}