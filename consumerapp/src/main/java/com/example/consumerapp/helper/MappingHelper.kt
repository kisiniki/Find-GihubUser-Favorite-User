package com.example.consumerapp.helper

import android.database.Cursor
import com.example.consumerapp.Model.FavoriteModel
import com.example.consumerapp.database.DatabaseContract
import java.util.*

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<FavoriteModel> {
        val favoriteList = ArrayList<FavoriteModel>()

        notesCursor?.apply {
            while (moveToNext()) {
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN))
                val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                val favorite = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FAVORITE))
                favoriteList.add(
                    FavoriteModel(
                        login,
                        avatar_url,
                        favorite
                    )
                )
            }
        }
        return favoriteList
    }
}