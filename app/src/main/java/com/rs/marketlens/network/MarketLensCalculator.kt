package com.rs.marketlens.network

import com.squareup.moshi.Json

data class MarketLensCalculator(
    @Json(name="id")
    var id: String? = null,

    @Json(name="duration")
    var duration: String? = null,

    @Json(name="r1")
    var r1: String? = null,

    @Json(name="r2")
    var r2: String? = null,

    @Json(name="pivot")
    var pivot: String? = null,

    @Json(name="s1")
    var s1: String? = null,

    @Json(name="s2")
    var s2: String? = null,

    @Json(name="notes")
    var notes: String? = null

)