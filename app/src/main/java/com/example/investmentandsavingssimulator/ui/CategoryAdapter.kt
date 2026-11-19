package com.example.investmentandsavingssimulator.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.investmentandsavingssimulator.R

class CategoryAdapter(
    private val categories: List<String>,
    private val icons: List<Int>, // Changed from List<String> to List<Int> for Drawables
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // 1. Define the ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val ivCategoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
    }

    // 2. Create the View (Inflate the XML layout)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    // 3. Bind the Data to the UI
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryName = categories[position]
        val iconResId = icons[position]

        // Set the text
        holder.tvCategoryName.text = categoryName

        // Set the vector drawable icon
        holder.ivCategoryIcon.setImageResource(iconResId)

        // Handle clicks
        holder.itemView.setOnClickListener {
            onClick(categoryName)
        }
    }

    // 4. Tell the adapter how many items there are
    override fun getItemCount(): Int = categories.size
}