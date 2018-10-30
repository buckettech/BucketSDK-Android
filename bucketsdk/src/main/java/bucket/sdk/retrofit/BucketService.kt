package bucket.sdk.retrofit

import android.net.Uri
import bucket.sdk.Bucket
import bucket.sdk.DeploymentEnvironment

//import com.buckettechnologies.consumerapp.BuildConfig
//import com.buckettechnologies.consumerapp.repository.bucket.BucketDataSource
//import com.buckettechnologies.consumerapp.repository.bucket.BucketHeader
//import com.buckettechnologies.consumerapp.repository.preferences.UserPreferences
//import com.buckettechnologies.consumerapp.util.retrofit.WebServiceHelper

object BucketService {

    private const val URI_SCHEME = "https"

    private const val URI_AUTHORITY_PRODUCTION = "prod.bucketthechange.com"
    private const val URI_AUTHORITY_DEVELOPMENT = "dev.bucketthechange.com"
    private const val URI_AUTHORITY_STAGING = "staging.bucketthechange.com"

    private const val API_VERSION = "v1"

    @JvmStatic private val env
            get() = Bucket.environment

    private val baseUri get() = Uri.Builder().apply {
        scheme(URI_SCHEME)
        authority(when(env) {
            DeploymentEnvironment.Production -> URI_AUTHORITY_PRODUCTION
            DeploymentEnvironment.Development -> URI_AUTHORITY_DEVELOPMENT
            DeploymentEnvironment.Staging -> URI_AUTHORITY_STAGING
        })
        appendPath("api")
        appendPath(API_VERSION)
    }

    @JvmStatic val retrofit
        get() = WebServiceHelper.createWebService<BucketDataSource>(
                "$baseUri/",
                Pair("Content-type", "application/json"))

}