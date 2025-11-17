package com.wbprofit.core.keystore.impl.domain

internal class KeystoreInteractor(
    private val repo: KeystoreRepository,
) {
    fun save(key: String, value: String) = repo.save(key, value)

    fun read(key: String) = repo.read(key)

    fun remove(key: String) = repo.remove(key)

    fun clear() = repo.clear()

    fun contains(key: String) = repo.contains(key)
}
