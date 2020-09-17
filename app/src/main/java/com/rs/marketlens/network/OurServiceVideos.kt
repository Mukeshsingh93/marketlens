package com.rs.marketlens.network

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import java.io.Serializable

data class OurServiceVideos(

    @Json(name="id")
    var id: String? = null,
    @Json(name="video_title")
    var video_title: String? = null,
    @Json(name="video_url")
    var video_url: String? = null,
    @Json(name="short_desc")
    var short_desc: String? = null,
    @Json(name="banner")
    var banner: String? = null,
    @Json(name="long_desc")
    var long_desc: String? = null,
    @Json(name="validity_number")
    var validity_number: String? = null,
    @Json(name="time_period")
    var time_period: String? = null,
    @Json(name="price")
    var price: String? = null,
    @Json(name="is_paid")
    var is_paid: Int? = 0

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(video_title)
        parcel.writeString(video_url)
        parcel.writeString(short_desc)
        parcel.writeString(banner)
        parcel.writeString(long_desc)
        parcel.writeString(validity_number)
        parcel.writeString(time_period)
        parcel.writeString(price)
        parcel.writeValue(is_paid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OurServiceVideos> {
        override fun createFromParcel(parcel: Parcel): OurServiceVideos {
            return OurServiceVideos(parcel)
        }

        override fun newArray(size: Int): Array<OurServiceVideos?> {
            return arrayOfNulls(size)
        }
    }
}