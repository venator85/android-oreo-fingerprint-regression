package com.example.alessio.fingerprinttest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;

import javax.crypto.Cipher;

@SuppressLint("GetInstance") // don't warn about ECB
public class MainActivity extends AppCompatActivity {

	private static final String TAG = "Fingerprint";

	private static final String KEY_NAME = "MyKey";

	private static final String CIPHER_ALGORITHM_SPEC = "RSA/ECB/PKCS1Padding";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void regenerateKey(View view) {
		try {
			generateKey(KEY_NAME);
			Toast.makeText(MainActivity.this, "Key generated", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.e(TAG, "Error regenerating key", e);
		}
	}

	public void verifyKey(View view) {
		try {
			KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
			keyStore.load(null);

			if (!keyStore.containsAlias(KEY_NAME)) {
				Toast.makeText(MainActivity.this, "Key not found", Toast.LENGTH_LONG).show();
				return;
			}

			createDecryptCipher(keyStore, KEY_NAME);

			Toast.makeText(MainActivity.this, "Key is still valid", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
			Log.e(TAG, "Error verifying key", e);
		}
	}

	private void generateKey(String keyName) throws Exception {
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");

		KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
				.setUserAuthenticationRequired(true)
				.setKeySize(2048)
				.setBlockModes(KeyProperties.BLOCK_MODE_ECB)
				.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);

		keyGenerator.initialize(builder.build());
		keyGenerator.generateKeyPair();
	}

	@NonNull
	private static Cipher createDecryptCipher(KeyStore keyStore, String keyName) throws Exception {
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyName, null);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_SPEC);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher;
	}

}
