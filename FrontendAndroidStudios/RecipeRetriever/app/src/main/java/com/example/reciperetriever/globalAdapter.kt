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
class GlobalAdapter(private val myList: MutableList<String>) : RecyclerView.Adapter<GlobalAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val removeButton: Button = itemView.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val currentView = inflater.inflate(R.layout.adapter_name_and_button, parent, false)
        // Return a new holder instance
        return ViewHolder(currentView)
    }

    override fun onBindViewHolder(viewHolder: GlobalAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val item: String = myList[position]
        // Set item views based on your views and data model
        val textView = viewHolder.nameTextView
        textView.text = item
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        val button = viewHolder.removeButton
        button.text = "Remove"
        // Sets a tag that I can use later for my own purposes (removing from the array)
        button.tag = position

        button.setOnClickListener { view -> //create your own action
            // using view.getTag() you can get the position of item clicked
            val itemIdx = view.tag as Int
            // Yeah... I don't normally do logs but I kinda like it? So... Here we go. Also, TOAST requires a context and contexts are... tricky
            Log.d("=======", itemIdx.toString())
            myList.removeAt(itemIdx)
            // Updates the ScrollList... Theoretically
            notifyItemRemoved(itemIdx)
            notifyItemRangeChanged(itemIdx, itemCount)
        }
    }

    override fun getItemCount(): Int {
        return myList.size
    }
}