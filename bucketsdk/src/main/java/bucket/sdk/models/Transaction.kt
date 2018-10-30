package bucket.sdk.models

// Bucket Packages:
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import bucket.sdk.*
import bucket.sdk.annotations.*
import bucket.sdk.callbacks.*
import bucket.sdk.extensions.*
import bucket.sdk.models.responses.CreateTransactionResponse
import bucket.sdk.retrofit.BucketService
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.json.JSONObject
import java.net.URL

class Transaction(var clientTransactionId: String, var amount: Double, var totalTransactionAmount: Double? = null) {

    // This is the primary key for the transaction in our db, as annotated:
    @PrimaryKey var bucketTransactionId : String? = null
    var customerCode                    : String?  = null
    var qrCodeContent                   : URL? = null

    // The rest of these fields are specified by the retailer:
    var locationId                      : String? = null

//    private fun updateWith(updateJSON: JSONObject?) {
//
//        if (updateJSON.isNil) return
//
//        this.customerCode = updateJSON!!.optString("customerCode", null)
//        this.locationId = updateJSON.optString("locationId", null)
//        this.bucketTransactionId = updateJSON.optInt("bucketTransactionId", 0)
//        this.qrCodeContent = updateJSON.getURL("qrCodeContent")
//        this.totalTransactionAmount = updateJSON.optDouble("totalTransactionAmount", 0.0)
//    }

    private fun updateWith(transactionResponse: CreateTransactionResponse?) {
        if (transactionResponse.isNil) return

        customerCode = transactionResponse?.customerCode
        locationId = transactionResponse?.locationId
        bucketTransactionId = transactionResponse?.bucketTransactionId
        qrCodeContent = URL(transactionResponse?.qrCodeContent)
        totalTransactionAmount = transactionResponse?.amount ?: 0.00
    }

    fun toJSON(): JSONObject {

        val obj = JSONObject()

        // We will always set the amount & clientTransactionId when sending the JSON:
        obj.put("amount", amount)
        obj.put("clientTransactionId", clientTransactionId)
        obj.put("totalTransactionAmount", totalTransactionAmount)

        // The fields that may not be set:
        if (!locationId.isNil) { obj.put("locationId", locationId!!) }
        if (!customerCode.isNil) { obj.put("customerCode", customerCode!!) }
        if (!qrCodeContent.isNil) { obj.put("qrCodeContent", qrCodeContent!!) }


        return obj
    }

    companion object {
        @SuppressLint("CheckResult")
        @JvmStatic fun deleteBy(customerCode: String, callback: DeleteTransaction?) {

            // Get the client id & client secret for this retailer:
            val retailerCode = Credentials.retailerCode()
            val terminalSecret = Credentials.terminalSecret()
            val countryCode = Credentials.countryCode()

            var shouldIReturn = false
            if (retailerCode.isNullOrEmpty() || terminalSecret.isNullOrEmpty()) {
                shouldIReturn = true
                callback?.didError(Error.unauthorized)
            }
            if (countryCode.isNullOrEmpty()) {
                shouldIReturn = true
                callback?.didError(Error.invalidCountryCode)
            }

            if (shouldIReturn) return

//            val url = Bucket.environment.transaction.appendPath(customerCode).build().toString()
//
//            url.httpDelete()
//                    .header(Pair("x-functions-key", terminalSecret!!))
//                    .header(Pair("retailerId", retailerCode!!))
//                    .header(Pair("countryId", countryCode!!))
//                    .header(Pair("terminalId", Build.SERIAL)).responseJson {
//                        _, response, result ->
//                        when (result) {
//                            is Result.Success -> {
//                                callback?.transactionDeleted()
//                            }
//                            is Result.Failure -> {
//                                callback?.didError(response.bucketError)
//                            }
//                        }
//                    }

            BucketService.retrofit.deleteTransaction(
                    terminalSecret = terminalSecret!!,
                    retailerId = retailerCode!!,
                    countryId = countryCode!!,
                    terminalId = Build.SERIAL,
                    customerCode = customerCode)
                    .map { response ->
                        if (response.isSuccessful) {
                            callback?.transactionDeleted()

                        } else {
                            val errorCode = response.body().errorCode
                            val errorMessage = response.body().message
                            callback?.didError(Error(errorMessage ?: "Unknown API Error", errorCode ?: "Unknown Error Code", response.code()))
                        }
                    }

        }
    }


    @SuppressLint("CheckResult")
    fun delete(callback: DeleteTransaction?) {

        // Get the client id & client secret for this retailer:
        val retailerCode = Credentials.retailerCode()
        val terminalSecret = Credentials.terminalSecret()
        val countryCode = Credentials.countryCode()

        var shouldIReturn = false
        if (retailerCode.isNullOrEmpty() || terminalSecret.isNullOrEmpty()) {
            shouldIReturn = true
            callback?.didError(Error.unauthorized)
        }
        if (countryCode.isNullOrEmpty()) {
            shouldIReturn = true
            callback?.didError(Error.invalidCountryCode)
        }

        if (shouldIReturn) return

//        val url = Bucket.environment.transaction.appendPath(customerCode).build().toString()
//
//        url.httpDelete()
//                .header(Pair("x-functions-key", terminalSecret!!))
//                .header(Pair("retailerId", retailerCode!!))
//                .header(Pair("countryId", countryCode!!))
//                .header(Pair("terminalId", Build.SERIAL)).responseJson {
//                    _, response, result ->
//                    when (result) {
//                        is Result.Success -> {
//                            callback?.transactionDeleted()
//                        }
//                        is Result.Failure -> {
//                            callback?.didError(response.bucketError)
//                        }
//                    }
//                }

        BucketService.retrofit.deleteTransaction(
                terminalSecret = terminalSecret!!,
                retailerId = retailerCode!!,
                countryId = countryCode!!,
                terminalId = Build.SERIAL,
                customerCode = customerCode!!)
                .map { response ->
                    if (response.isSuccessful) {
                        callback?.transactionDeleted()

                    } else {
                        val errorCode = response.body().errorCode
                        val errorMessage = response.body().message
                        callback?.didError(Error(errorMessage ?: "Unknown API Error", errorCode ?: "Unknown Error Code", response.code()))
                    }
                }

    }

    @SuppressLint("CheckResult")
    fun create(callback: CreateTransaction?) {

        // Get the client id & client secret for this retailer:
        val retailerCode = Credentials.retailerCode()
        val terminalSecret = Credentials.terminalSecret()
        val countryCode = Credentials.countryCode()

        var shouldIReturn = false
        if (retailerCode.isNullOrEmpty() || terminalSecret.isNullOrEmpty()) {
            shouldIReturn = true
            callback?.didError(Error.unauthorized)
        }
        if (countryCode.isNullOrEmpty()) {
            shouldIReturn = true
            callback?.didError(Error.invalidCountryCode)
        }

        if (shouldIReturn) return

        val jsonBody = this.toJSON()

//        val url = Bucket.environment.transaction.build().toString()
//
//        url.httpPost()
//                .body(jsonBody.toString())
//                .header(Pair("x-functions-key", terminalSecret!!))
//                .header(Pair("retailerId", retailerCode!!))
//                .header(Pair("countryId", countryCode!!))
//                .header(Pair("terminalId", Build.SERIAL)).responseJson {
//                    request, response, result ->
//                    Log.d("bucket.sdk REQUEST: ", request.toString())
//                    when (result) {
//                        is Result.Success -> {
//                            this@Transaction.updateWith(result.value.obj())
//                            callback?.transactionCreated()
//                        }
//                        is Result.Failure -> {
//                            callback?.didError(response.bucketError)
//                        }
//                    }
//                }

        BucketService.retrofit.createTransaction(
                terminalSecret = terminalSecret!!,
                retailerId = retailerCode!!,
                countryId = countryCode!!,
                terminalId = Build.SERIAL,
                transaction = this)
                .map { response ->
                    if (response.isSuccessful) {
                        updateWith(response.body())
                        callback?.transactionCreated()

                    } else {
                        val errorCode = response.body().errorCode
                        val errorMessage = response.body().message
                        callback?.didError(Error(errorMessage ?: "Unknown API Error", errorCode ?: "Unknown Error Code", response.code()))
                    }
                }

    }

}