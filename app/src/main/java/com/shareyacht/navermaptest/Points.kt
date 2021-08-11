package com.shareyacht.navermaptest

import com.google.gson.annotations.SerializedName

data class Points(
    @SerializedName("data")
    val data: List<Point>
)

data class Point(
    @SerializedName("index")
    val index: Int,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String
)