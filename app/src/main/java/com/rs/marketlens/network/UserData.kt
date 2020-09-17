package com.rs.marketlens.network

import com.squareup.moshi.Json

data class UserData(
    @Json(name="fname")
    var fname: String? = null,
    @Json(name="lname")
    var lname: String? = null,
    @Json(name="email")
    var email: String? = null
)