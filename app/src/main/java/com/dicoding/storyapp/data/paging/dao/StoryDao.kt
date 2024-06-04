package com.dicoding.storyapp.data.paging.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.*
import com.dicoding.storyapp.data.paging.entity.StoryEntity


@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(story: List<StoryEntity>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story")
    suspend fun deleteAllStories()
}