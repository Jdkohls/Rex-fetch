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
class FilterAdapter(private val myList: MutableList<String>) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.text)
        val myCheckBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val currentView = inflater.inflate(R.layout.adapter_text_and_checkbox, parent, false)
        // Return a new holder instance
        return ViewHolder(currentView)
    }

    override fun onBindViewHolder(viewHolder: FilterAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val item: String = myList[position]
        // Set item views based on your views and data model
        val textView = viewHolder.nameTextView
        textView.text = item
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        val aCheckBox = viewHolder.myCheckBox
//        aCheckBox.text = ""
        // Sets a tag that I can use later for my own purposes (removing from the array)
        aCheckBox.tag = item // tag = "eggs" (Used to remove by itemName)
        aCheckBox.isChecked = item in Globals.filteredArray
        Log.d("===Checkbox====", aCheckBox.isChecked.toString())

        aCheckBox.setOnClickListener { view -> //create your own action
            val foodItem = view.tag as String

            if (foodItem in Globals.filteredArray) {
                // aCheckBox.isChecked = false -- Should be done automatically.
                val success = Globals.remStrFromArray(foodItem, Globals.filteredArray)
                Log.d("=======", success.toString())
                Log.d("===Success====", Globals.fridgeArray.size.toString())
                Log.d("===Success====", Globals.filteredArray.size.toString())
            } else {
                // aCheckBox.isChecked = true
                val success = Globals.addStrToArray(foodItem, Globals.filteredArray)
                Log.d("===Success====", success.toString())
                Log.d("===Success====", Globals.fridgeArray.size.toString())
                Log.d("===Success====", Globals.filteredArray.size.toString())
            }
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return myList.size
    }
}