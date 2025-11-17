package com.wbprofit.core.keystore.impl.domain

internal interface KeystoreRepository {
    fun save(key: String, value: String): Boolean

    fun read(key: String): String?

    fun remove(key: String): Boolean

    fun clear()

    fun contains(key: String): Boolean
}
