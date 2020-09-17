package com.rs.marketlens.network

import com.squareup.moshi.Json
import java.io.Serializable

data class PackageDetails(
    @Json(name="id")
    var id: String? = null,
    @Json(name="name")
    var name: String? = null,
    @Json(name="description")
    var description: String? = null,
    @Json(name="price")
    var price: String? = null,
    @Json(name="validity")
    var validity: String? = null,
    @Json(name="duration")
    var duration: String? = null,
    @Json(name="banner")
    var banner: String? = null,
    @Json(name="is_paid")
    var is_paid: Int? = 0
):Serializable