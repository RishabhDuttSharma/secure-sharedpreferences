# secure-sharedpreferences
SharedPreferences secured with KeyStore mechanism.

## Overview
Persists Key-Value pairs in secure-form, and retrieves them.
Uses AndroidKeyStore to generate and store Keys for Encoding and Decoding.

## Mechanism
Manages generation of SecretKey for Android versions below API 23 and API 23+.

**Below Marshmallow**
* Generates 2048-bit RSA Keys

**Marshmallow & above**
* Generates 128-bit Secret Key

## Internals
 * Keys are encoded using One-Way Fixed-Length Hash Algorithms.
 * Values are encoded using a combination of Key-Value Serialization followed by AES-Transformation.
 
## Implementation
**Initialization (use of singleton pattern is recommended)**
```
val prefs = SecureSharedPreferences(this)
```
**Saving a key-value pair**
```
prefs.edit().putString(key, value).commit()
```
**Retrieving value for a key**
```
prefs.getString(key, defValue)
```
