package com.example.investmentandsavingssimulator.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.investmentandsavingssimulator.R
import com.google.android.material.appbar.MaterialToolbar

class MmfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_list) // Reusing the layout we made earlier

        val userId = intent.getIntExtra("USER_ID", -1)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAssetList)
        toolbar.title = "Money Market Funds"

        val rv = findViewById<RecyclerView>(R.id.rvFilteredAssets)

        // Dummy list of Kenyan MMFs for your presentation
        val mmfFunds = listOf(
            Pair("CIC Money Market Fund", "12.5% p.a."),
            Pair("Sanlam Money Market Fund", "11.8% p.a."),
            Pair("Britam Money Market Fund", "12.1% p.a."),
            Pair("Etica Money Market Fund", "13.0% p.a.")
        )

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mmf, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val fund = mmfFunds[position]
                holder.itemView.findViewById<TextView>(R.id.tvFundName).text = fund.first
                holder.itemView.findViewById<TextView>(R.id.tvFundYield).text = "Yield: ${fund.second}"

                holder.itemView.setOnClickListener {
                    val intent = Intent(this@MmfActivity, MmfDetailActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("FUND_NAME", fund.first)
                    startActivity(intent)
                }
            }

            override fun getItemCount() = mmfFunds.size
        }
    }
}