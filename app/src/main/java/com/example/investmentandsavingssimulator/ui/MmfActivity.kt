package com.example.investmentandsavingssimulator.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.investmentandsavingssimulator.R
import com.google.android.material.appbar.MaterialToolbar

// Data Class to hold Fund details
data class MmfFund(
    val name: String,
    val yield: String,
    val minInvestment: Double
)

class MmfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_list)

        val userId = intent.getIntExtra("USER_ID", -1)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAssetList)
        toolbar.title = "Money Market Funds"
        toolbar.setNavigationOnClickListener { finish() }

        val rv = findViewById<RecyclerView>(R.id.rvFilteredAssets)

        // Updated list with specific minimums for each fund
        val mmfFunds = listOf(
            MmfFund("CIC Money Market Fund", "12.5% p.a.", 1000.0),
            MmfFund("Sanlam Money Market Fund", "11.8% p.a.", 500.0),
            MmfFund("Britam Money Market Fund", "12.1% p.a.", 2000.0),
            MmfFund("Etica Money Market Fund", "13.0% p.a.", 1000.0)
        )

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mmf, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val fund = mmfFunds[position]
                holder.itemView.findViewById<TextView>(R.id.tvFundName).text = fund.name
                holder.itemView.findViewById<TextView>(R.id.tvFundYield).text = "Yield: ${fund.yield}"

                holder.itemView.setOnClickListener {
                    val intent = Intent(this@MmfActivity, MmfDetailActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("FUND_NAME", fund.name)
                    intent.putExtra("FUND_YIELD", fund.yield)
                    intent.putExtra("MIN_INVESTMENT", fund.minInvestment) // Pass the minimum
                    startActivity(intent)
                }
            }

            override fun getItemCount() = mmfFunds.size
        }
    }
}