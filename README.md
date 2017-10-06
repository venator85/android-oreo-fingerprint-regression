Android 8 Oreo - Fingerprint test
====

A sample to debug a regression in Android 8 Oreo Keystore management: KeyPermanentlyInvalidatedException is no longer called when initializing a Cipher in DECRYPT mode after a new fingerprint is enrolled.

Steps to reproduce
----

1. [System] Register a fingerprint in system. 
2. [App] Generate a key by clicking the "Regenerate key" button. The generated key has `setUserAuthenticationRequired(true)` but not `setUserAuthenticationValidityDurationSeconds()`
3. [App] Verify that a Cipher in DECRYPT mode can be initialized with the key by clicking the "Verify key invalidated" button
4. [System] Add another fingerprint in system settings. The key should have been invalidated.
5. [App] Click the "Verify key invalidated" button again.

Expected: key is invalidated, and a toast with the `KeyPermanentlyInvalidatedException` exception is shown by the app.

Actual: `KeyPermanentlyInvalidatedException` is not thrown

On Android 7.1, the `KeyPermanentlyInvalidatedException` is correctly thrown.