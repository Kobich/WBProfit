package com.wbprofit.core.network.api

import kotlin.reflect.KClass

interface NetworkFeature {
    fun <T : Any> createApi(
        apiClass: KClass<T>,
        baseUrl: String? = null,
    ): T
}

inline fun <reified T : Any> NetworkFeature.create(baseUrl: String? = null): T =
    createApi(T::class, baseUrl)
