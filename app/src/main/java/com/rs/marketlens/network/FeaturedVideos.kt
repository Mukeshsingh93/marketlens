package com.rs.marketlens.network

import com.squareup.moshi.Json

data class FeaturedVideos(
    @Json(name="id")
    var id: String? = null,

    @Json(name="video_title")
    var video_title: String? = null,

    @Json(name="short_desc")
    var short_desc: String? = null,

    @Json(name="banner")
    var banner: String? = null
)