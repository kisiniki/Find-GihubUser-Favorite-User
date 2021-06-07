package com.example.helpme.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class User(
    var login: String = "",
    var name: String? = "",
    var avatar_url: String? = "",
    var location: String? = "",
    var company: String? = "",
    var public_repos: Int? = 0,
    var followers: Int? = 0,
    var following: Int? = 0,
) : Parcelable
