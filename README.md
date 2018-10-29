[![](https://jitpack.io/v/buckettech/BucketSDK-Android.svg)](https://jitpack.io/#buckettech/BucketSDK-Android)

# BucketSDK-Android
This is the BucketSDK for Android.  Here is where you can create transactions & other important features of the Bucket API pre-wrapped for Android.

## Requirements
You will need to be registered with Bucket & have obtained a Retailer Account.  You will need to have received a clientId & clientSecret in order to access Bucket's API.

## Installation

BucketSDK is available through [JitPack](https://jitpack.io). To install
it, simply add the following line to your project-level gradle file:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

And, this to your app-level gradle file:

```gradle
implementation "com.github.buckettech:BucketSDK-Android:$bucketSDKVersion"
```

## Usage
Using the BucketSDK, you will be able to use either Java or Kotlin to access the functions that you will need.
### Setting the app context & app environment
```Java
// Java:
Bucket.setAppContext(BucketApp.appContext);
if (!BuildConfig.DEBUG) {
    Bucket.setEnvironment(DeploymentEnvironment.Production);
}
```
```Kotlin
// Kotlin
if (!BuildConfig.DEBUG) {
    Bucket.environment = DeploymentEnvironment.Production
}
Bucket.appContext = theAppContext
```

### Setting & retrieving your retailer id & retailer secret:
````Java
// Java:
// Setter:
Credentials.setRetailerCode("RetailerId");
Credentials.setTerminalSecret("TerminalSecret");

// Getter:
String retailerCode = Credentials.retailerCode();
String terminalSecret = Credentials.terminalSecret();
````

```kotlin
// Kotlin:
// Setter:
Credentials.setRetailerCode("RetailerId")
Credentials.setTerminalSecret("RetailerSecret")

// Getter:
val retailerId = Credentials.retailerCode()
val retailerSecret = Credentials.terminalSecret()
```

### Setting your currency code:
SGD (Singapore) & USD (USA) currencies are currently supported.
```Java
// Java:
Credentials.setCountryCode("USD");
Bucket.fetchBillDenominations(new BillDenomination() {
    @Override public void setBillDenoms() {
        
    }
    @Override public void didError(Error error) {
        
    }
});
```

```kotlin
// Kotlin:
Credentials.setCountryCode("USD")
Bucket.fetchBillDenominations(object : BillDenomination() {
    override fun setBillDenoms() {
    
    }
    override fun didError(error: Error?) {

    }
})
```

### Getting the Bucket Amount
```Java
// Java:
long bucketAmount = Bucket.bucketAmount(789);
```
```kotlin
// Kotlin:
val bucketAmount = Bucket.bucketAmount(789)
```

### Creating a transaction
You will need to use the bucketAmount function to set the transaction amount here.
```Java
// Java:
Transaction tr = new Transaction("XCFRTDSFGGOL", 0.60, 5.60);
tr.create(new CreateTransaction() {
    @Override public void transactionCreated() {
        // The transaction was successfully created!
    }
    @Override public void didError(VolleyError volleyError) {
        // There was an error.
    }
});
```
```Kotlin
// Kotlin:
val transaction = Transaction("XCFRTDSFGGOL", 0.60, 5.60)
transaction.create(object : CreateTransaction() {
    override fun transactionCreated() {
        // Yay the transaction was successfully created.
    }
    override fun didError(error: VolleyError?) {
        // Oh no - we had an error :(
    }
})
```

## Author
Ryan Coyne, ryan@buckettechnologies.com

## License

BucketSDK is available under the MIT license. See the LICENSE file for more info.
