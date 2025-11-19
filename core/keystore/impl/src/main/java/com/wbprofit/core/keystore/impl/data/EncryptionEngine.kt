package com.wbprofit.core.keystore.impl.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

internal class EncryptionEngine(
    private val keyStoreAlias: String = DEFAULT_KEY_ALIAS,
    private val transformation: String = TRANSFORMATION,
    private val tagLengthBits: Int = GCM_TAG_LENGTH_BITS,
) {
    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        runCatching { load(null) }.getOrElse { throw IllegalStateException("Failed to load AndroidKeyStore", it) }
    }

    fun encrypt(plainBytes: ByteArray): EncryptedPayload {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, provideSecretKey())
        val cipherBytes = cipher.doFinal(plainBytes)
        return EncryptedPayload(
            initializationVector = cipher.iv,
            cipherText = cipherBytes,
        )
    }

    fun decrypt(payload: EncryptedPayload): ByteArray {
        val cipher = Cipher.getInstance(transformation)
        val gcmParameterSpec = GCMParameterSpec(tagLengthBits, payload.initializationVector)
        cipher.init(Cipher.DECRYPT_MODE, provideSecretKey(), gcmParameterSpec)
        return cipher.doFinal(payload.cipherText)
    }

    @Synchronized
    private fun provideSecretKey(): SecretKey {
        val existingKey = keyStore.getKey(keyStoreAlias, null) as? SecretKey
        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val parameterSpec = KeyGenParameterSpec
            .Builder(
                keyStoreAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(parameterSpec)
        return keyGenerator.generateKey()
    }

    companion object {
        private const val GCM_TAG_LENGTH_BITS = 128
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val DEFAULT_KEY_ALIAS = "wb_secure_storage_master_key"
    }
}
