package com.rs.marketlens.network

import com.squareup.moshi.Json

data class MontlyPlan(
    @Json(name="id")
    var id: String? = null,

    @Json(name="price")
    var price: String? = null,

    @Json(name="duration")
    var duration: String? = null
)
