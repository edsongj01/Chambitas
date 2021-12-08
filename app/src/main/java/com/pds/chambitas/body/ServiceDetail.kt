package com.pds.chambitas.body

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ServiceDetail (
    var direction: String?,
    var lat: Double?,
    var lng: Double?,
    var service: String?
): Parcelable