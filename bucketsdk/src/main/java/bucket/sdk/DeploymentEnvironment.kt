package bucket.sdk

import android.net.Uri

enum class DeploymentEnvironment {

    // Cases: (Production & Development for now)
    Production, Development, Staging;

    // Case URL Endpoint:
//    private fun bucketBaseUri(): Uri.Builder {
//        val builder = Uri.Builder()
//        builder.scheme("https")
//        when (this) {
//            Production -> builder.authority("prod.bucketthechange.com")
//            Development -> builder.authority("dev.bucketthechange.com")
//            Staging -> builder.authority("staging.bucketthechange.com")
//        }
//        builder.appendPath("api")
//        builder.appendPath("v1")
//        return builder
//    }

    // Case URL Endpoint:
//    private fun bucketBaseUri() = Uri.Builder().apply {
//        scheme(URI_SCHEME)
//        authority(when(this@DeploymentEnvironment) {
//            Production -> URI_AUTHORITY_PRODUCTION
//            Development -> URI_AUTHORITY_DEVELOPMENT
//            Staging -> URI_AUTHORITY_STAGING
//        })
//        appendPath("api")
//        appendPath(API_VERSION)
//    }

    // PRE-BUILT ENDPOINT PATHS:
//    var transaction : Uri.Builder
//        get() = bucketBaseUri().appendPath("transaction")
//        private set(value) {}
//
//    var registerTerminal : Uri.Builder
//        get() = bucketBaseUri().appendPath("registerterminal")
//        private set(value) {}
//
//    var billDenoms : Uri.Builder
//        get() = bucketBaseUri().appendPath("billDenoms")
//        private set(value) {}

}