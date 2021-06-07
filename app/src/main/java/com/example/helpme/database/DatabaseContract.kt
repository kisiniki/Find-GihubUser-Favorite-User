package com.example.helpme.database

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    companion object {
        const val AUTHORITY = "com.example.helpme"
        const val SCHEME = "content"
    }

    internal class UserColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val FAVORITE = "favorite"

            val CONTENT_URI: Uri = Uri.Builder()
                    .scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }

}