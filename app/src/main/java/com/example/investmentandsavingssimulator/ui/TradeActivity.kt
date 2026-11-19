package com.example.investmentandsavingssimulator.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.example.investmentandsavingssimulator.data.local.TransactionEntity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.util.Locale

class TradeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trade)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Get data sent from AssetListActivity
        val userId = intent.getIntExtra("USER_ID", -1)
        val assetName = intent.getStringExtra("ASSET_NAME") ?: "Asset"
        val assetPrice = intent.getDoubleExtra("ASSET_PRICE", 0.0)

        // 2. Toolbar back button
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarTrade)
        toolbar.setNavigationOnClickListener { finish() }

        // 3. Database
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(db.userDao(), db.transactionDao())

        // 4. Views
        val tvName = findViewById<TextView>(R.id.tvTradeAssetName)
        val tvPrice = findViewById<TextView>(R.id.tvTradeAssetPrice)
        val etShares = findViewById<TextInputEditText>(R.id.etShares)
        val tvTotal = findViewById<TextView>(R.id.tvTotalCost)
        val btnBuy = findViewById<Button>(R.id.btnBuy)
        val tvMessage = findViewById<TextView>(R.id.tvTradeMessage)

        tvName.text = assetName
        tvPrice.text = "Price per share: KES ${String.format(Locale.getDefault(), "%,.2f", assetPrice)}"

        // 5. Update the total cost while the user types
        etShares.doAfterTextChanged {
            val shares = it.toString().toIntOrNull() ?: 0
            tvTotal.text = "Total cost: KES ${String.format(Locale.getDefault(), "%,.2f", shares * assetPrice)}"
        }

        fun showMessage(text: String, isSuccess: Boolean) {
            tvMessage.text = text
            tvMessage.setTextColor(if (isSuccess) Color.parseColor("#2E7D32") else Color.RED)
            tvMessage.visibility = View.VISIBLE
        }

        // 6. Buy button
        btnBuy.setOnClickListener {
            val shares = etShares.text.toString().toIntOrNull()
            if (shares == null || shares <= 0) {
                showMessage("Enter a whole number of shares", false)
                return@setOnClickListener
            }
            if (assetPrice <= 0) {
                showMessage("This stock has no valid price", false)
                return@setOnClickListener
            }

            val totalCost = shares * assetPrice

            lifecycleScope.launch {
                val user = repository.getUserById(userId)
                if (user == null) {
                    showMessage("User not found", false)
                    return@launch
                }
                if (totalCost > user.virtualCashBalance) {
                    showMessage(
                        "Insufficient funds! Your wallet balance is KES ${String.format(Locale.getDefault(), "%,.2f", user.virtualCashBalance)}",
                        false
                    )
                    return@launch
                }

                // Deduct from wallet
                repository.updateUser(user.copy(virtualCashBalance = user.virtualCashBalance - totalCost))

                // Save the transaction record
                repository.insertTransaction(
                    TransactionEntity(
                        userId = user.id,
                        assetName = assetName,
                        amountInvested = totalCost
                    )
                )

                showMessage("Bought $shares shares of $assetName", true)
                etShares.text?.clear()
            }
        }
    }
}