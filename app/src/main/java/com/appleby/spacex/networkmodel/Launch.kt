package com.appleby.spacex.networkmodel

data class Launch(
    val links : Links,
    val success : Boolean,
    val details : String,
    val date_utc : String,
    val date_unix : Int,
    val upcoming : Boolean
)