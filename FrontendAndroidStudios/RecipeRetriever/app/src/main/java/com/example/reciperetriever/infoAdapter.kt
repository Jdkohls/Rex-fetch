package com.example.reciperetriever

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
class InfoAdapter(private val item: String) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {
    //need to create a List<String> out of 'item' using string parsing
    private val myList = item.split(", ").map { it.trim() } //holy shit this worked

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val myText: TextView = itemView.findViewById(R.id.text)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val currentView = inflater.inflate(R.layout.adapter_text, parent, false)
        // Return a new holder instance
        return ViewHolder(currentView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: InfoAdapter.ViewHolder, position: Int) {
        val textView = viewHolder.myText
        // 1. [Text]
        textView.text = (position + 1).toString() + ". " + myList[position]
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return myList.size
    }
}