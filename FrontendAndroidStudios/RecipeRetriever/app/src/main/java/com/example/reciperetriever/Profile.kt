package com.example.reciperetriever

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reciperetriever.databinding.ProfileBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

class Profile : Fragment() {

    private var _binding: ProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileBinding.inflate(inflater, container, false)
        reInitRecyclerView()
        return binding.root
    }

    // Button Functionality
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.usersProfileText.text = Globals.name + "\'s Profile"
        binding.substitutionsText.text = "Substitutions: " + Globals.substitutes
        binding.textInputName.hint = Globals.name

        // Init RecyclerView
        reInitRecyclerView()

        binding.addAllergen.setOnClickListener {
            // Implement Adding Allergen
            if (binding.textInputFoodItem.text != null && binding.textInputFoodItem.text.toString() != "") {
                val textInp = binding.textInputFoodItem.text.toString()
                // Append to array - i.e. Store locally
                val success = Globals.addStrToArray(textInp, Globals.allergenArray)
                // Create a textview
                if (success) {
                    // If success, Then reinit the recyclerView
                    reInitRecyclerView()
                } else {
                    Toast.makeText(context, "Duplicate Error: You've already entered this allergen.", Toast.LENGTH_SHORT).show()
                }
                // Reset TextBox
                binding.textInputFoodItem.text = null
            } else {
                //do nothing if empty
            }
        }
        binding.saveName.setOnClickListener {
            // Save the user's name -- To be used later?
            if (binding.textInputName.text != null && binding.textInputName.text.toString() != "") {

                //Globals.update_username(Globals.name, Globals.loginDict[Globals.name]!!,
                        //binding.textInputName.text.toString())

                // Save to global variable
                Globals.name = binding.textInputName.text.toString()
                // Dynamically changes anything that contains username to change to newUsername
                binding.usersProfileText.text = Globals.name + "\'s Profile"
                binding.textInputName.hint = Globals.name
                // Reset Text Box
                binding.textInputName.text = null
            } else {
                // Error Message
                Toast.makeText(context, "Please input a valid name.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.addSub.setOnClickListener {
            if(Globals.substitutes == 99) {
                Toast.makeText(context, "You can't have more than 99 substitutions", Toast.LENGTH_SHORT).show()
            } else {
                Globals.substitutes += 1
                binding.substitutionsText.text = "Substitutions: " + Globals.substitutes
            }
        }
        binding.subSub.setOnClickListener {
            if(Globals.substitutes == 0) {
                Toast.makeText(context, "You can't have less than 0 substitutions", Toast.LENGTH_SHORT).show()
            } else {
                Globals.substitutes -= 1
                binding.substitutionsText.text = "Substitutions: " + Globals.substitutes
            }
        }
        // Navigation
        binding.returnToHomePage.setOnClickListener {
            // TODO: Send the new userProfile to backend
            findNavController().navigate(R.id.action_profile_to_home)
        }
    }

    private fun reInitRecyclerView() {
        val allergenRecyclerView = binding.profileRecyclerView
        allergenRecyclerView.adapter = GlobalAdapter(Globals.allergenArray)
        allergenRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
