package com.rs.marketlens.network

import com.squareup.moshi.Json

data class OurService(
    @Json(name="image")
    var image: String? = null

)