package com.example.reciperetriever

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reciperetriever.databinding.FridgeBinding
import kotlin.text.Regex

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

class FridgePage : Fragment() {
    private fun isValid(str: String): Boolean {
        //validates if the input contains only lowercase characters. Logic from ChatGPT
        if(str.length < 3) {
            //probably an invalid ingredient
            return false
        }
        val regex = Regex("[a-z]+")
        return regex.matches(str)
    }

    private var _binding: FridgeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FridgeBinding.inflate(inflater, container, false)
        reInitRecyclerView()
        return binding.root

    }

    // Button Functionality
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reInitRecyclerView()

        binding.addFood.setOnClickListener {
            // Implement Adding Food
            if (binding.textInputFoodItem.text != null && binding.textInputFoodItem.text.toString() != "") {
                val textInp = binding.textInputFoodItem.text.toString()
                if(!isValid(textInp)) {
                    //something weird was given. Make a toast
                    Toast.makeText(
                            context,
                            "Ingredients must be lowercase, longer than 3 characters, and contain only letters",
                            Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Append to array - i.e. Store locally
                    val success = Globals.addStrToArray(textInp, Globals.fridgeArray)
                    // Create a textview
                    if (success) {
                        reInitRecyclerView()
                    } else {
                        Toast.makeText(
                                context,
                                "Duplicate Error: Item is already in the fridge.",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                    // Reset TextBox
                    binding.textInputFoodItem.text = null
                }
            } else {
                Toast.makeText(
                    context,
                    "Null Error: I think you forgot to type a food item",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.moveFood.setOnClickListener {
            // Implement Moving Food
            if (binding.textInputFoodItem.text != null && binding.textInputFoodItem.text.toString() != "") {
                val textInp = binding.textInputFoodItem.text.toString()
                // Find index in foodArray (And add to grocery)
                val indexToRemove = Globals.mvStrFromArr1ToArr2(textInp, Globals.fridgeArray, Globals.groceryArray)
                // Remove a textview
                if (indexToRemove != -1) {
                    reInitRecyclerView()
                } else {
                    Toast.makeText(context, "Move Error: Item to move not found", Toast.LENGTH_LONG).show()
                }
                // Reset TextBox
                binding.textInputFoodItem.text = null
            } else {
                Toast.makeText(context, "Null Error: Nothing was typed in the text input", Toast.LENGTH_LONG).show()
            }
        }
        // Navigation
        binding.homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_fridge_to_home)
        }
    }

    private fun reInitRecyclerView() {
        val fridgeRecyclerView = binding.fridgeRecyclerView
        fridgeRecyclerView.adapter = GlobalAdapter(Globals.fridgeArray)
        fridgeRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
