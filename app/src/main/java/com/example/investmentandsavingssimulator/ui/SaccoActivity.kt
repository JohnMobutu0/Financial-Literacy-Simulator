
package com.example.investmentandsavingssimulator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.example.investmentandsavingssimulator.data.local.SaccoEntity
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class SaccoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sacco)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarSacco)
        toolbar.title = "SACCO Shares"
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        toolbar.setNavigationOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.rvSacco)
        val tvEmpty = findViewById<TextView>(R.id.tvSaccoEmpty)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val database = AppDatabase.getDatabase(applicationContext)

        lifecycleScope.launch {
            try {
                val saccos = database.saccoDao().getAllSaccos()

                if (saccos.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.adapter = SaccoAdapter(saccos)
                }
            } catch (e: Exception) {
                tvEmpty.text = "Unable to load SACCO products. Please try again."
                tvEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }
    }

    private class SaccoAdapter(
        private val saccoList: List<SaccoEntity>
    ) : RecyclerView.Adapter<SaccoAdapter.SaccoViewHolder>() {

        class SaccoViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            val name: TextView = itemView.findViewById(R.id.tvSaccoName)
            val returnRate: TextView =
                itemView.findViewById(R.id.tvSaccoReturn)
            val price: TextView =
                itemView.findViewById(R.id.tvSaccoPrice)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SaccoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sacco, parent, false)

            return SaccoViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: SaccoViewHolder,
            position: Int
        ) {
            val sacco = saccoList[position]

            holder.name.text = sacco.name
            holder.returnRate.text =
                "Annual dividend: ${sacco.annualDividendRate}%"
            holder.price.text =
                "Share price: KES ${"%,.2f".format(sacco.sharePrice)}"
        }

        override fun getItemCount(): Int = saccoList.size
    }
}
