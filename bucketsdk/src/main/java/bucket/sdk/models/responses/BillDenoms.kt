package bucket.sdk.models.responses

import java.io.Serializable

data class BillDenoms(
        val usesNaturalChangeFunction: Boolean,
        val denominations: List<Double>,
        val errorCode: String?,
        val message: String?)
    : Serializable