package com.dicoding.storyapp.data.paging.entity

import androidx.room.*

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
