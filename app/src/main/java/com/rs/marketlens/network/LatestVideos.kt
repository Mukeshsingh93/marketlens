package com.rs.marketlens.network

import com.squareup.moshi.Json

data class LatestVideos(
    @Json(name="id")
    var id: String? = null,

    @Json(name="banner")
    var banner: String? = null,

    @Json(name="videourl")
    var videourl: String? = null
)