package com.rs.marketlens.network

import com.squareup.moshi.Json

data class ResponseModel(


    @Json(name="status")
    var status: Boolean? = null,
    @Json(name="message")
    var message: String? = null,
    @Json(name="userid")
    var userid: Int? = null,
    @Json(name="is_active")
    var is_active: Int? = null,

    @Json(name="orderid")
    var orderid: String? = null,

    var access_token: String? = null,

    @Json(name="sliders")
    var sliders: MutableList<HomeSlider>? = null,

    @Json(name="latestvideos")
    var latestvideos: MutableList<OurServiceVideos>? = null,

    @Json(name="featuredvideos")
    var featuredvideos: MutableList<FeaturedVideos>? = null,

    @Json(name="packagepricing")
    var packagepricing: MutableList<MontlyPlan>? = null,

    @Json(name="mlenscalculator")
    var mlenscalculator: MutableList<MarketLensCalculator>? = null,

    @Json(name="packagedetails")
    var packagedetails: PackageDetails? = null,

    @Json(name="userdata")
    var userdata: UserData? = null

)