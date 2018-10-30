package bucket.sdk.models.responses

import java.io.Serializable

data class CreateTransactionResponse(
        val customerCode: String?,
        val qrCodeContent: String?,
        val bucketTransactionId: String?,
        val amount: Double?,
        val intervalId: String?,
        val locationId: String?,
        val clientTransactionId: String?,
        val terminalId: String?,
        val errorCode: String?,
        val message: String?)
    : Serializable