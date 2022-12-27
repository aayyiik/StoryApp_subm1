package com.ari.submission.storyapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stories(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String
): Parcelable
