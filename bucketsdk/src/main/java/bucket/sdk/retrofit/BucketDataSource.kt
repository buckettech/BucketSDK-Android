package bucket.sdk.retrofit

import bucket.sdk.models.RegisterTerminalBody
import bucket.sdk.models.responses.CreateTransactionResponse
import bucket.sdk.models.Transaction
import bucket.sdk.models.responses.BillDenoms
import bucket.sdk.models.responses.DeleteTransactionResponse
import bucket.sdk.models.responses.RegisterTerminalResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

/**
 * Bucket datasource - Retrofit tagged
 */
interface BucketDataSource {

    /** TRANSACTION **/

    @POST("transaction")
    fun createTransaction(@Header("x-functions-key") terminalSecret: String? = null,
                          @Header("retailerId") retailerId: String,
                          @Header("terminalId") terminalId: String,
                          @Header("countryId") countryId: String,
                          @Body transaction: Transaction)
            : Single<Response<CreateTransactionResponse>>

    @DELETE("transaction/{customerCode}")
    fun deleteTransaction(@Header("x-functions-key") terminalSecret: String,
                          @Header("retailerId") retailerId: String,
                          @Header("terminalId") terminalId: String,
                          @Header("countryId") countryId: String,
                          @Path("customerCode") customerCode: String)
            : Single<Response<DeleteTransactionResponse>>

    /** TERMINAL **/

    @POST("registerterminal")
    fun registerTerminal(@Header("x-functions-key") terminalSecret: String? = null,
                         @Header("retailerId") retailerId: String,
                         @Header("countryId") countryId: String,
                         @Body registerTerminalBody: RegisterTerminalBody)
            : Single<Response<RegisterTerminalResponse>>

    /** BILL DENOMINATIONS **/

    @POST("billDenoms")
    fun billDenominations(@Header("x-functions-key") terminalSecret: String,
                          @Header("countryId") countryId: String)
            : Single<Response<BillDenoms>>

}
