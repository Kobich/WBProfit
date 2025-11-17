package com.wbprofit.core.keystore.api

interface KeystoreFeature {
    fun save(key: String, value: String): Boolean

    fun read(key: String): String?

    fun remove(key: String): Boolean

    fun clear()

    fun contains(key: String): Boolean
}
