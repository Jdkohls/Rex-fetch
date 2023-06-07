package com.example.reciperetriever

//import android.hardware.biometrics.BiometricManager.Strings
//import com.example.reciperetriever.data binding.ContentMainBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reciperetriever.Repo.LoginRepo
import com.example.reciperetriever.databinding.LoginBinding

//import android.widget.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

class Login : Fragment() {

    private var _binding: LoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LoginBinding.inflate(inflater, container, false)

        return binding.root

    }

    //    Button Functionality - Mainly for adding foodItems to foodArray
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Globals.addKeyValToDict("admin","password", Globals.loginDict)//just makin testing faster
        // Bug Fix
        Globals.loggedIn = false

        binding.loginButton.setOnClickListener {

            if (binding.editTextUsername.text != null ||
                binding.editTextPassword.text != null
            ) {
                // Get Username and Password
                val username = binding.editTextUsername.text.toString()
                val password = binding.editTextPassword.text.toString()
                if(username == "" || password == "") {
                    //do nothing
                } else {
                    // Check login info.
                    val success = Globals.checkUsername(username, Globals.loginDict)
                    if (success) {
                        //username exists. Check if password was correct.
                        if (Globals.checkPassword(username, password, Globals.loginDict)) {
                            //username and password were correct
                            Toast.makeText(
                                context,
                                "Welcome, %s".format(username),
                                Toast.LENGTH_LONG
                            ).show()
                            Globals.name = username
                            Globals.loggedIn = true
                            // TODO: Load Saved User Data into the local save Globals -- Will make the frontend take over all the displaying & reduce calls to server.
                            findNavController().navigate(R.id.action_login_to_home)
                        } else {
                            //incorrect password
                            Toast.makeText(context, "Incorrect Password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "User not found.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                //No data inputted
                Toast.makeText(context, "Enter a valid username and password", Toast.LENGTH_SHORT).show()
            }
        }
        binding.newUserButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_new_user)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
