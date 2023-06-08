package com.example.reciperetriever

//import com.google.android.material.textfield.TextInputEditText
//import kotlinx.android.synthetic.main.fridge.* // Import for useful things such as getting text from a text box   --- Useless -> Deprecated Service\
//import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reciperetriever.databinding.GroceryBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
//I hope this works, this should take the textInput Box and assign it to textInp for future use.
//var textInp = (TextInputEditText)findViewById(R.id.textInput_addFoodItem);
//val calcBtn =  findViewById(R.id.calculateBtn) as Button   -- Correct code from stack overflow

class GroceryPage : Fragment() {

    private var _binding: GroceryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GroceryBinding.inflate(inflater, container, false)
        reInitRecyclerView()
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reInitRecyclerView()
        binding.addFood.setOnClickListener {
            // Implement Adding Food
            if (binding.textInputFoodItem.text != null && binding.textInputFoodItem.text.toString() != "") {
                val textInp = binding.textInputFoodItem.text.toString()
                // Append to array - i.e. Store locally
                val success = Globals.addStrToArray(textInp, Globals.groceryArray)
                // Create a textview
                if (success) {
                    reInitRecyclerView()
                } else {
                    Toast.makeText(context, "Duplicate Error: Item is already in the fridge.", Toast.LENGTH_LONG).show()
                }
                // Reset TextBox
                binding.textInputFoodItem.text = null
            } else {
                Toast.makeText(context, "Null Error: I think you forgot to type a food item", Toast.LENGTH_LONG).show()
            }
        }
        binding.moveFood.setOnClickListener {
            // Implement Moving Food
            if (binding.textInputFoodItem.text != null && binding.textInputFoodItem.text.toString() != "") {
                val textInp = binding.textInputFoodItem.text.toString()
                // Find index in foodArray (And add to grocery)
                val indexToRemove = Globals.mvStrFromArr1ToArr2(textInp, Globals.groceryArray, Globals.fridgeArray)
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
            // TODO: Send the new groceryInfo to backend -- On any page change tbh
            findNavController().navigate(R.id.action_grocery_to_home)
        }
    }

    private fun reInitRecyclerView() {
        val groceryRecyclerView = binding.groceryRecyclerView
        groceryRecyclerView.adapter = GlobalAdapter(Globals.groceryArray)
        groceryRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}