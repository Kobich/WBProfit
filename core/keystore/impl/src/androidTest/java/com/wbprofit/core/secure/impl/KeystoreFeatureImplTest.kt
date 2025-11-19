package com.wbprofit.core.secure.impl

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.wbprofit.core.keystore.api.KeystoreFeature
import com.wbprofit.core.keystore.impl.KeystoreFeatureImpl
import com.wbprofit.core.keystore.impl.data.EncryptionEngine
import com.wbprofit.core.keystore.impl.data.KEYSTORE_PREFERENCES_FILE
import com.wbprofit.core.keystore.impl.data.KeystoreRepositoryImpl
import com.wbprofit.core.keystore.impl.domain.KeystoreInteractor
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KeystoreFeatureImplTest {

    private lateinit var context: Context
    private lateinit var storage: KeystoreFeature

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val preferences = context.getSharedPreferences(KEYSTORE_PREFERENCES_FILE, Context.MODE_PRIVATE)
        val repository = KeystoreRepositoryImpl(
            preferences = preferences,
            encryptionEngine = EncryptionEngine(),
        )
        val interactor = KeystoreInteractor(repository)
        storage = KeystoreFeatureImpl(interactor)
        storage.clear()
    }

    @After
    fun tearDown() {
        storage.clear()
    }

    @Test
    fun saveReadAndRemoveValue() {
        val testKey = "wb_api_key"
        val expectedValue = "eyJhbGciOiJFUzI1NiIsImtpZCI6IjIwMjUwOTA0djEiLCJ0eXAiOiJKV1QifQ.eyJhY2MiOjEsImVudCI6MSwiZXhwIjoxNzc3ODcxODc3LCJpZCI6IjAxOWE0NTkzLTVmNGUtNzc2Mi1iNDRlLTdkMGM4M2NmMDIxZSIsImlpZCI6MTI2NDUxOTEsIm9pZCI6MjUwMDA2MDU5LCJzIjoxMDczNzUzMzQyLCJzaWQiOiJkYTNjM2Y1Zi0xYmFjLTQ4MTAtYjlmYi0yYjA3ODUxZDEzNjAiLCJ0IjpmYWxzZSwidWlkIjoxMjY0NTE5MX0.kxGX3z7BXBapTd_iR_r38c4GWn1VW1tyZ93cVrRcPyQ82ueCkh5IpD1wdu9gmMhCG9Ms0O6Vg4vSSlL9rPMl-g"

        val saveResult = storage.save(testKey, expectedValue)
        assertTrue(saveResult)
        assertTrue(storage.contains(testKey))

        val encodedPayload = context
            .getSharedPreferences(KEYSTORE_PREFERENCES_FILE, Context.MODE_PRIVATE)
            .getString(testKey, null)
        Log.i(TAG, "Encrypted payload for '$testKey': $encodedPayload")

        val storedValue = storage.read(testKey)
        Log.i(TAG, "Decrypted value for '$testKey': $storedValue")
        assertEquals(expectedValue, storedValue)

        val removeResult = storage.remove(testKey)
        assertTrue(removeResult)
        assertFalse(storage.contains(testKey))
        assertNull(storage.read(testKey))
    }

    companion object {
        private const val TAG = "SecureStorageTest"
    }
}
