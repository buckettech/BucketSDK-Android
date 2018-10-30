package bucket.sdk.models.responses

import java.io.Serializable

data class RegisterTerminalResponse(
        val isApproved: Boolean,
        val apiKey: String,
        val errorCode: String?,
        val message: String?)
    : Serializable