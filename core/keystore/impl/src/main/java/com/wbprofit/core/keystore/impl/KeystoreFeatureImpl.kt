package com.wbprofit.core.keystore.impl

import com.wbprofit.core.keystore.api.KeystoreFeature
import com.wbprofit.core.keystore.impl.domain.KeystoreInteractor

internal class KeystoreFeatureImpl(
    private val interactor: KeystoreInteractor,
) : KeystoreFeature {
    override fun save(key: String, value: String): Boolean = interactor.save(key, value)

    override fun read(key: String): String? = interactor.read(key)

    override fun remove(key: String): Boolean = interactor.remove(key)

    override fun clear() = interactor.clear()

    override fun contains(key: String): Boolean = interactor.contains(key)
}
