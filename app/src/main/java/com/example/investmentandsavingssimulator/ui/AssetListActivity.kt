package com.example.investmentandsavingssimulator.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class AssetListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_list)

        val userId = intent.getIntExtra("USER_ID", -1)
        val category = intent.getStringExtra("CATEGORY") ?: "Assets"

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAssetList)
        toolbar.title = category
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        toolbar.setNavigationOnClickListener { finish() }

        val rvAssets = findViewById<RecyclerView>(R.id.rvFilteredAssets)
        val db = AppDatabase.getDatabase(applicationContext)

        // Setup Adapter
        val adapter = AssetAdapter(emptyList()) { clickedAsset ->
            val intent = Intent(this, TradeActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("ASSET_NAME", clickedAsset.name)
            intent.putExtra("ASSET_PRICE", clickedAsset.price)
            startActivity(intent)
        }
        rvAssets.layoutManager = LinearLayoutManager(this)
        rvAssets.adapter = adapter

        // Fetch assets matching the category from Room
        lifecycleScope.launch {
            val allAssets = db.assetDao().getAllAssets()
            // Map UI category to database type
            val dbType = when(category) {
                "NSE Stocks" -> "Stock"
                "Bonds" -> "Bond"
                else -> category
            }
            val filtered = allAssets.filter { it.type.equals(dbType, ignoreCase = true) }
            adapter.updateData(filtered)
        }
    }
}