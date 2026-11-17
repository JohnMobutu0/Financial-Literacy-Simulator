package com.example.investmentandsavingssimulator.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.local.AssetEntity

class AssetAdapter(
    private var assets: List<AssetEntity>,
    private val onItemClick: (AssetEntity) -> Unit
) : RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {

    inner class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvAssetName)
        val tvType: TextView = itemView.findViewById(R.id.tvAssetType)
        val tvReturn: TextView = itemView.findViewById(R.id.tvAssetReturn)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(assets[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return AssetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = assets[position]
        holder.tvName.text = asset.name
        holder.tvType.text = "Type: ${asset.type}"
        holder.tvReturn.text = "Return: ${asset.expectedReturn}"
    }

    override fun getItemCount(): Int = assets.size

    fun updateData(newAssets: List<AssetEntity>) {
        assets = newAssets
        notifyDataSetChanged()
    }
}