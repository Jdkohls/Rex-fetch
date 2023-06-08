package com.example.reciperetriever

//import android.hardware.biometrics.BiometricManager.Strings
import android.os.Bundle
//import android.provider.Settings.Global
//import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.reciperetriever.databinding.NewUserBinding
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

//import android.widget.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

class NewUser : Fragment() {

    private var _binding: NewUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = NewUserBinding.inflate(inflater, container, false)
        reInitRecyclerView()
        return binding.root
    }

    //create a new user if we have all the appropriate data
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For safety - Bug Fix
        Globals.loggedIn = false

        reInitRecyclerView()
        binding.createUserButton.setOnClickListener {
            if(binding.newUsername.text != null && binding.newUserPassword.text != null && binding.verifyPassword.text != null) {
                val username = binding.newUsername.text.toString()
                val password = binding.newUserPassword.text.toString()
                val verify = binding.verifyPassword.text.toString()
                if(username == "" || password == "" || verify == "") {
                    Toast.makeText(context, "Enter a username and password", Toast.LENGTH_SHORT).show()
                } else {
                    if(password != verify) {
                        //passwords don't match
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    } else {
                        //passwords match. Try to insert username-password pair into Globals

                        //Globals.new_user(username, password)

                        val success = Globals.addKeyValToDict(username, password, Globals.loginDict)
                        if(!success) {
                            //username already exists
                            Toast.makeText(context, "Username already exists. Please try again", Toast.LENGTH_LONG).show()
                        } else {
                            val displayString = "Successfully created user: $username"
                            Toast.makeText(context, displayString, Toast.LENGTH_LONG).show()
                            // TODO: Push Username-Password && Allergen Array to Backend -----------------------------------------------------------------------
                            findNavController().navigate(R.id.action_new_user_to_login)
                        }
                    }
                }
            } else {
                //do nothing
                Toast.makeText(context, "Enter a username and password", Toast.LENGTH_SHORT).show()
            }
        }
        binding.addAllergen.setOnClickListener {
            // Implement Adding Allergen
            if (binding.textInputFoodItem.text != null && binding.textInputFoodItem.text.toString() != "") {
                val textInp = binding.textInputFoodItem.text.toString()
                // Append to array - i.e. Store locally
                val success = Globals.addStrToArray(textInp, Globals.allergenArray)
                // Create a textview
                if (success) {
                    reInitRecyclerView()
                } else {
                    Toast.makeText(context, "Duplicate Error: You've already entered this allergen.", Toast.LENGTH_SHORT).show()
                }
                // Reset TextBox
                binding.textInputFoodItem.text = null
            } else {
                Toast.makeText(context, "Null Error: I think you forgot to type an allergen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reInitRecyclerView() {
        val allergenRecyclerView = binding.newUserRecyclerView
        allergenRecyclerView.adapter = GlobalAdapter(Globals.allergenArray)
        allergenRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}