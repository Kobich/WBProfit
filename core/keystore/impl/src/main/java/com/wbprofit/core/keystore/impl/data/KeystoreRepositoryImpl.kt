package com.wbprofit.core.keystore.impl.data

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.wbprofit.core.keystore.impl.domain.KeystoreRepository

private const val TAG = "KeystoreRepository"
internal const val KEYSTORE_PREFERENCES_FILE = "wb_secure_storage"

internal class KeystoreRepositoryImpl(
    private val preferences: SharedPreferences,
    private val encryptionEngine: EncryptionEngine,
) : KeystoreRepository {
    override fun save(key: String, value: String): Boolean = runCatching {
        val payload = encryptionEngine.encrypt(value.toByteArray(Charsets.UTF_8))
        preferences.edit { putString(key, payload.encode()) }
    }.onFailure { throwable ->
        Log.e(TAG, "Failed to save secure value for key: $key", throwable)
    }.isSuccess

    override fun read(key: String): String? {
        if (!preferences.contains(key)) {
            return null
        }

        val serializedPayload = preferences.getString(key, null)
        val payload = serializedPayload?.let { EncryptedPayload.decode(it) }

        return when {
            payload == null -> {
                if (serializedPayload != null) {
                    Log.w(TAG, "Encrypted payload is corrupted for key: $key, removing entry")
                    preferences.edit { remove(key) }
                }
                null
            }
            else -> runCatching {
                val decryptedBytes = encryptionEngine.decrypt(payload)
                String(decryptedBytes, Charsets.UTF_8)
            }.onFailure { throwable ->
                Log.e(TAG, "Failed to decrypt secure value for key: $key", throwable)
                preferences.edit { remove(key) }
            }.getOrNull()
        }
    }

    override fun remove(key: String): Boolean = runCatching {
        preferences.edit { remove(key) }
    }.onFailure { throwable ->
        Log.e(TAG, "Failed to remove secure value for key: $key", throwable)
    }.isSuccess

    override fun clear() {
        runCatching { preferences.edit { clear() } }.onFailure { throwable ->
            Log.e(TAG, "Failed to clear secure storage", throwable)
        }
    }

    override fun contains(key: String): Boolean = preferences.contains(key)
}
