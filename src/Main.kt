/*
Concept Note: https://docs.google.com/document/d/1wk01w4CUkNXCvizfFDzaocq1x0OpOTGvsyAuYIm0ppw/edit?usp=sharing

Group members:
166010 — Cirhuza Mulumeoderhwa John
168837 — Mary Mutobu
171369 — Abdalla Zulekha Omar
192401 — Trizah Bosibori
167297 — Bryton Marxmillian
*/

fun main() {
    println("========================================================\n" +
            "Investment and Saving Simulator \n" +
            "=========================================================")



    println("Enter your name: ")
    var user_name = readLine()

    val user_id = "001"
    var user_balance: Double = 50_000.0
    val platform1 = "MMFs" //Money Market Funds
    val platform2 = "SS" //SACCO Shares
    val platform3 = "NSES" //NSE Stocks
    val platform4 = "FD" //Fixed Deposits
    val platform5 = "TB" //Treasury Bonds
    val platform6 = "HRSS" //High-Risk Scam Simulation
    var platform_chosen = ""
    var fund1 = "Fund A"
    var annual_yield = "10.5%"
    var management_fees = 0.001 // 1.0%
    var risk = "Low"
    var min_investemt = 1_000.0
    var treasury_bills = 0.5 // 50%
    var bank_deposits = 0.3 // 30%
    var corporation_deposits = 0.4 // 40%
    var current_ivestemnt_value = user_balance
    var growth_rate = 0.2 //20%


    do {
        println("Enter the name of platform: (either `MMFs` for Money Market Funds or `SS` SACCO Shares or `NSES` for NSE Stocks or `FD` for Fixed Deposits or `TB` Treasury Bonds or HRSS for High-Risk Scam Simulation)")
        platform_chosen = readln()
    } while (!(platform_chosen.lowercase() == platform1.lowercase() || platform_chosen.lowercase() == platform2.lowercase() || platform_chosen.lowercase() == platform3.lowercase() || platform_chosen.lowercase() == platform4.lowercase() || platform_chosen.lowercase() == platform5.lowercase() || platform_chosen.lowercase() == platform6.lowercase()))

    if (platform_chosen.lowercase() == platform1.lowercase()) {
        platform_chosen = "Money Market Funds"


        println("--------------------------------------\n" +
                "Simulator\n" +
                "--------------------------------------\n" +
                "User name: $user_name \n" +
                "Initial Balance: $user_balance \n" +
                "Simulated Platform: $platform_chosen\n" +
                "Name of the Fund: $fund1\n" +
                "Annual Yield: $annual_yield\n\n"
                )
        for (month in 1..12 step 2) {
            current_ivestemnt_value = (user_balance * month * growth_rate) - (user_balance * management_fees)
            println("Month: $month")
            println("Current Investment Value: $current_ivestemnt_value")

        }
        println("---------------------------------------")

    } else if (platform_chosen.lowercase() == platform2.lowercase()) {
        platform_chosen = "SACCO Shares"


        println("--------------------------------------\n" +
                "Simulator\n" +
                "--------------------------------------\n" +
                "User name: $user_name \n" +
                "Initial Balance: $user_balance \n" +
                "Simulated Platform: $platform_chosen\n\n"
        )
        for (month in 1..12 step 2) {
            current_ivestemnt_value = (user_balance * month * growth_rate)
            println("Month: $month")
            println("Current Investment Value: $current_ivestemnt_value")

        }
        println("---------------------------------------")

    } else if (platform_chosen.lowercase() == platform3.lowercase()) {
        platform_chosen = "NSE Stocks"


        println("--------------------------------------\n" +
                "Simulator\n" +
                "--------------------------------------\n" +
                "User name: $user_name \n" +
                "Initial Balance: $user_balance \n" +
                "Simulated Platform: $platform_chosen\n" +
                "Annual Yield: $annual_yield\n"
        )
        for (month in 1..12 step 2) {
            current_ivestemnt_value = (user_balance * month * growth_rate)
            println("Month: $month")
            println("Current Investment Value: $current_ivestemnt_value")

        }
        println("---------------------------------------")

    } else if (platform_chosen.lowercase() == platform4.lowercase()) {
        platform_chosen = "Fixed Depositss"


        println("--------------------------------------\n" +
                "Simulator\n" +
                "--------------------------------------\n" +
                "User name: $user_name \n" +
                "Initial Balance: $user_balance \n" +
                "Simulated Platform: $platform_chosen\n\n"
        )
        for (month in 1..12 step 2) {
            current_ivestemnt_value = (user_balance * month * growth_rate) - (user_balance * management_fees)
            println("Month: $month")
            println("Current Investment Value: $current_ivestemnt_value")

        }
        println("---------------------------------------")

    } else if (platform_chosen.lowercase() == platform5.lowercase()) {
        platform_chosen = "Treasury Bonds"


        println("--------------------------------------\n" +
                "Simulator\n" +
                "--------------------------------------\n" +
                "User name: $user_name \n" +
                "Initial Balance: $user_balance \n" +
                "Simulated Platform: $platform_chosen\n" +
                "Annual Yield: $annual_yield\n\n"
        )
        for (month in 1..12 step 2) {
            current_ivestemnt_value = (user_balance * month * growth_rate) - (user_balance * management_fees)
            println("Month: $month")
            println("Current Investment Value: $current_ivestemnt_value")

        }
        println("---------------------------------------")

    }


}
