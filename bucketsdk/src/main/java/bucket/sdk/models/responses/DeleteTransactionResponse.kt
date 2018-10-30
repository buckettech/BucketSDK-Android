package bucket.sdk.models.responses

import java.io.Serializable

data class DeleteTransactionResponse(
        val result: String?,
        val errorCode: String?,
        val message: String?)
    : Serializable