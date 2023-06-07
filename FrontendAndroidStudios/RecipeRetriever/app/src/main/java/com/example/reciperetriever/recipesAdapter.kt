package com.example.reciperetriever

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
class RecipeAdapter(private val myList: MutableList<RecipeBrief>, private var mCallback: CallbackInterface?) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val recipeName: TextView = itemView.findViewById(R.id.text1)
        val goToRecipe: Button = itemView.findViewById(R.id.button2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val currentView = inflater.inflate(R.layout.adapter_two_name_and_two_buttons, parent, false)
        // Return a new holder instance
//        initOnClickInterface(listener)
        return ViewHolder(currentView)

    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: RecipeAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val item: RecipeBrief = myList[position]
//         Set item views based on your views and data model
        val textView = viewHolder.recipeName
        textView.text = item.title
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

        val button2 = viewHolder.goToRecipe
        button2.text = "Cook!"
        // Sets a tag that I can use later for my own purposes (removing from the array)
        button2.tag = position
        button2.setOnClickListener {
            //save the ID of the recipe that was clicked on, then navigate to single recipe page.
            Globals.recipe_id = item.id
            mCallback?.onClick()
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return myList.size
    }
}