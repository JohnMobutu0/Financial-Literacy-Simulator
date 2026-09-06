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
    var user_balance: Double = 50.0




    //Money Market Funds
    fun MMFs () {

        var platform_chosen = "Money Market Funds"
        var fund1 = "abc Fund"
        var annual_yield = "10.5%"
        var management_fees = 0.001 // 1.0%
        var risk = "Low"
        var min_investemt = 1_000.0
        var treasury_bills = 0.5 // 50%
        var bank_deposits = 0.3 // 30%
        var corporation_deposits = 0.4 // 40%
        var growth_rate = 0.2 //20%

        var t_bill = 1_000.0
        var fixed_deposit = 500.0
        var company_debt = 900.0
        var certificate_of_debt = 500.0 // Certificates of deposit
        var fund_size = t_bill + fixed_deposit + company_debt +certificate_of_debt // total number of unit is 800/5000
        var profit = 0.0
        var profit_rate = user_balance / fund_size
        var Totalprofit = 0.0



        println("--------------------------------------\n" +
                "Simulator\n" +
                "--------------------------------------\n" +
                "User name: $user_name \n" +
                "Simulated Platform: $platform_chosen\n" +
                "Name of the Fund: $fund1\n" +
                "Initial Balance: $user_balance\n" +
                "Fund size: $fund_size\n" +
                "Annual Yield: $annual_yield\n\n"

        )
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug")
        for (month in months.indices step 2) {
            var t_bill_profit = 0.2
            var fixed_deposit_profit = 0.2
            var company_debt_profit = 0.001
            var certificate_of_debt_profit = 0.001

            profit = (((t_bill * t_bill_profit) + (fixed_deposit * fixed_deposit_profit) + (company_debt * company_debt_profit) + (certificate_of_debt * certificate_of_debt_profit)) * profit_rate)
            println("${months[month]} 2026")
            Totalprofit = Totalprofit + profit
            println("Profit: ${Totalprofit + user_balance}")


        }
        user_balance = Totalprofit + user_balance
        println("Balance: $user_balance")
        println("---------------------------------------")
    }


    //SACCO Shares
    fun SS(){
        //
    }


    //NSE Stocks
    fun NSES() {
        //
    }


    //Fixed Deposits
    fun FD() {
        //
    }


    //Treasury Bonds
    fun TB() {
        //
    }


    //High-Risk Scam Simulation
    fun HRSS() {
        //
    }










    // names of the various platforms
    val platform1 = "MMFs" //Money Market Funds
    val platform2 = "SS" //SACCO Shares
    val platform3 = "NSES" //NSE Stocks
    val platform4 = "FD" //Fixed Deposits
    val platform5 = "TB" //Treasury Bonds
    val platform6 = "HRSS" //High-Risk Scam Simulation
    var platform_chosen = ""



    do {
        println("Enter the name of platform: (either `$platform1` for Money Market Funds or `$platform2` SACCO Shares or `$platform3` for NSE Stocks or `$platform4` for Fixed Deposits or `$platform5` Treasury Bonds or '$platform6' for High-Risk Scam Simulation)")
        platform_chosen = readln()
    } while (!(platform_chosen.lowercase() == platform1.lowercase() || platform_chosen.lowercase() == platform2.lowercase() || platform_chosen.lowercase() == platform3.lowercase() || platform_chosen.lowercase() == platform4.lowercase() || platform_chosen.lowercase() == platform5.lowercase() || platform_chosen.lowercase() == platform6.lowercase()))






    // launch of the simulation of the platforms
    if (platform_chosen.lowercase() == platform1.lowercase()) {
        MMFs()

    } else if (platform_chosen.lowercase() == platform2.lowercase()) {
        SS()

    } else if (platform_chosen.lowercase() == platform3.lowercase()) {
        NSES()

    } else if (platform_chosen.lowercase() == platform4.lowercase()) {
        FD()

    } else if (platform_chosen.lowercase() == platform5.lowercase()) {
        TB()

    }  else if (platform_chosen.lowercase() == platform6.lowercase()) {
        HRSS()

    }


}
