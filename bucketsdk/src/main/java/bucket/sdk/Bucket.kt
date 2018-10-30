package bucket.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log.e
import bucket.sdk.callbacks.*
import bucket.sdk.models.*
import bucket.sdk.extensions.bucketError
import bucket.sdk.retrofit.BucketService
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.json.JSONObject
import java.util.*

class Bucket {

    companion object {

        @JvmStatic var appContext : Context? = null

        @JvmStatic var environment = DeploymentEnvironment.Development
        @JvmStatic private var denoms : List<Double> = listOf(100.00, 50.00, 20.00, 10.00, 5.00, 2.00, 1.00)
        @JvmStatic private var usesNaturalChangeFunction : Boolean
            get() { return Credentials.sharedPrefs?.getBoolean("USES_NATURAL_CHANGE", false) ?: false }
            set(value) {
                val editor = Credentials.sharedPrefs?.edit()
                editor?.putBoolean("USES_NATURAL_CHANGE", value)
                editor?.apply()
            }

        @JvmStatic fun bucketAmount(changeDueBack: Double): Double {
            var bucketAmount = changeDueBack
            if (usesNaturalChangeFunction) {
                // Make sure this is ordered by the amount
                val filteredDenoms = denoms.filter { it <= changeDueBack }

                // These values should already be descended from 10000 down to 100.
                filteredDenoms.forEach { denomination ->
                    bucketAmount = (bucketAmount % denomination)
                }

            } else {
                while (bucketAmount > 1.00) bucketAmount = (bucketAmount % 1.00)

            }
            return bucketAmount
        }

        @SuppressLint("CheckResult")
        @JvmStatic fun fetchBillDenominations(callback: BillDenomination?) {

            var shouldReturn = false
            val retailerCode = Credentials.retailerCode()
            val terminalSecret = Credentials.terminalSecret()

            if (retailerCode.isNullOrEmpty() || terminalSecret.isNullOrEmpty()) {
                shouldReturn = true
                callback?.didError(Error.unauthorized)
            }

            val countryCode = Credentials.countryCode()
            if (countryCode.isNullOrEmpty()) {
                shouldReturn = true
                callback?.didError(Error.invalidCountryCode)
            }

            if (shouldReturn) return

//            val theURL = environment.billDenoms.build().toString()

//            theURL.httpPost().header(Pair("countryId", countryCode!!)).responseJson {
//                _, response, result ->
//
//                when (result) {
//                    is Result.Success -> {
//                        val responseJson = result.value.obj()
//                        val denominations = responseJson.optJSONArray("denominations")
//                        usesNaturalChangeFunction = responseJson.optBoolean("usesNaturalChangeFunction", false)
//                        denominations?.let {
//                            // Create our list of denominations:
//                            val theDenoms : MutableList<Double> = ArrayList()
//                            for (i in 0..(it.length()-1)) {
//                                theDenoms[i] = it.getDouble(i)
//                            }
//                            Bucket.denoms = theDenoms
//                        }
//                        callback?.setBillDenoms()
//                    }
//                    is Result.Failure -> {
//                        val error = response.bucketError
//                        callback?.didError(error)
//                    }
//                }
//            }

            BucketService.retrofit.billDenominations(
                    terminalSecret = terminalSecret!!,
                    countryId = countryCode!!)
                    .map { response ->
                        if (response.isSuccessful) {
                            val denominations = response.body().denominations
                            usesNaturalChangeFunction = response.body().usesNaturalChangeFunction
                            denominations.let {
                                // Create our list of denominations:
                                val theDenoms : MutableList<Double> = ArrayList()
                                for (i in 0..(it.size-1)) {
                                    theDenoms[i] = it[i]
                                }
                                Bucket.denoms = theDenoms
                            }
                            callback?.setBillDenoms()

                        } else {
                            val errorCode = response.body().errorCode
                            val errorMessage = response.body().message
                            callback?.didError(Error(errorMessage ?: "Unknown API Error", errorCode ?: "Unknown Error Code", response.code()))
                        }
                    }
        }

        @SuppressLint("CheckResult")
        @JvmStatic fun registerTerminal(countryCode : String, callback: RegisterTerminal?) {

            val retailerCode = Credentials.retailerCode()

            if (retailerCode.isNullOrEmpty()) {
                callback?.didError(Error("Please check your retailer id", "InvalidRetailer", 401))
            }

            BucketService.retrofit.registerTerminal(
                    countryId = countryCode,
                    retailerId = retailerCode!!,
                    registerTerminalBody = RegisterTerminalBody(terminalId = Build.SERIAL))
                    .map { response ->
                        if (response.isSuccessful) {
                            val apiKey = response.body().apiKey
                            val isApproved = response.body().isApproved
                            // Set the terminal secret:
                            Credentials.setCountryCode(countryCode)
                            Credentials.setTerminalSecret(apiKey)
                            callback?.success(isApproved)

                        } else {
                            val errorCode = response.body().errorCode
                            val errorMessage = response.body().message
                            callback?.didError(Error(errorMessage ?: "Unknown API Error", errorCode ?: "Unknown Error Code", response.code()))
                        }
                    }

//            val json = JSONObject().apply { put("terminalId", terminalId) }
//
//            val theURL = environment.registerTerminal.build().toString()

//            theURL.httpPost().body(json.toString()).header(Pair("retailerId",retailerCode!!)).header(Pair("countryId", countryCode)).responseJson {
//                _, response, result ->
//
//                when (result) {
//                    is Result.Failure -> {
//                        val error = response.bucketError
//                        callback?.didError(error)
//                    }
//                    is Result.Success -> {
//                        val responseJson = result.value.obj()
//                        val apiKey = responseJson.getString("apiKey")
//                        val isApproved = responseJson.getBoolean("isApproved")
//                        // Set the terminal secret:
//                        Credentials.setCountryCode(countryCode)
//                        Credentials.setTerminalSecret(apiKey)
//                        callback?.success(isApproved)
//
//                    }
//                }
//            }
        }
    }
}