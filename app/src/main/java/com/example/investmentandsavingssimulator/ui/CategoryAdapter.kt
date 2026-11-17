package com.example.investmentandsavingssimulator.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.investmentandsavingssimulator.R

class CategoryAdapter(
    private val categories: List<String>,
    private val icons: List<String>,
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val tvIcon: TextView = itemView.findViewById(R.id.tvCategoryIcon)

        init {
            itemView.setOnClickListener {
                onCategoryClick(categories[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.tvName.text = categories[position]
        holder.tvIcon.text = icons[position]
    }

    override fun getItemCount(): Int = categories.size
}