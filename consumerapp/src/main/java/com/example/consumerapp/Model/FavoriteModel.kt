package com.example.consumerapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteModel (
            var login: String = "",
            var avatar_url: String? = "",
            var favorite: String? = ""
    ) : Parcelable
