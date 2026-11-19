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

data class FixedDepositOption(
    val name: String,
    val rateDisplay: String,
    val annualRatePercent: Double,
    val lockPeriodMonths: Int,
    val minInvestment: Double
)

class FixedDepositsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_list)

        val userId = intent.getIntExtra("USER_ID", -1)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAssetList)
        toolbar.title = "Fixed Deposits"
        toolbar.setNavigationOnClickListener { finish() }

        val rv = findViewById<RecyclerView>(R.id.rvFilteredAssets)

        val fdOptions = listOf(
            FixedDepositOption("3-Month Fixed Deposit", "10.5% p.a. • Locked 3 mos", 10.5, 3, 5000.0),
            FixedDepositOption("6-Month Fixed Deposit", "11.5% p.a. • Locked 6 mos", 11.5, 6, 5000.0),
            FixedDepositOption("12-Month Fixed Deposit", "12.5% p.a. • Locked 12 mos", 12.5, 12, 10000.0)
        )

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mmf, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val option = fdOptions[position]
                holder.itemView.findViewById<TextView>(R.id.tvFundName).text = option.name
                holder.itemView.findViewById<TextView>(R.id.tvFundYield).text = "Rate: ${option.rateDisplay}"

                holder.itemView.setOnClickListener {
                    val intent = Intent(this@FixedDepositsActivity, FixedDepositDetailActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("FD_NAME", option.name)
                    intent.putExtra("ANNUAL_RATE", option.annualRatePercent)
                    intent.putExtra("LOCK_MONTHS", option.lockPeriodMonths)
                    intent.putExtra("MIN_INVESTMENT", option.minInvestment)
                    startActivity(intent)
                }
            }

            override fun getItemCount() = fdOptions.size
        }
    }
}