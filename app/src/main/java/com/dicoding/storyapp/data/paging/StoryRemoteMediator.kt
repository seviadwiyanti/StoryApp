package com.dicoding.storyapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.paging.entity.RemoteKeyEntity
import com.dicoding.storyapp.data.paging.entity.StoryDatabase
import com.dicoding.storyapp.data.paging.entity.StoryEntity
import com.dicoding.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.first

//@OptIn(ExperimentalPagingApi::class)
//class StoryRemoteMediator(
//    private val userPreference: UserPreference,
//    private val database: StoryDatabase
//) : RemoteMediator<Int, StoryEntity>() {
//
//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, StoryEntity>
//    ): MediatorResult {
//        val page = when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
//            }
//
//            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                val prevKey = remoteKeys?.prevKey
//                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                prevKey
//            }
//
//            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                val nextKey = remoteKeys?.nextKey
//                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                nextKey
//            }
//        }
//
//        return try {
//            val getToken = userPreference.getSession().first()
//            val apiService = ApiConfig.getApiService(getToken.token)
//            val response = apiService.getStory(page, state.config.pageSize)
//            val stories = response.listStory
//            val storiesList = stories.map { story ->
//                StoryEntity(
//                    story.id,
//                    story.name,
//                    story.description,
//                    story.photoUrl,
//                    story.createdAt,
//                    story.lat,
//                    story.lon
//                )
//            }
//
//            val endOfPaginationReached = response.listStory.isEmpty()
//
//            database.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    database.remoteKeysDao().deleteRemoteKeys()
//                    database.storyDao().deleteAllStories()
//                }
//                val prevKey = if (page == 1) null else page - 1
//                val nextKey = if (endOfPaginationReached) null else page + 1
//                val keys = response.listStory.map {
//                    RemoteKeyEntity(
//                        id = it.id,
//                        prevKey = prevKey,
//                        nextKey = nextKey
//                    )
//                }
//                database.remoteKeysDao().insertAll(keys)
//                database.storyDao().insertStories(storiesList)
//            }
//            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
//        } catch (e: Exception) {
//            MediatorResult.Error(e)
//        }
//    }
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
//        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
//            database.remoteKeysDao().getRemoteKeysId(data.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
//        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
//            database.remoteKeysDao().getRemoteKeysId(data.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { id ->
//                database.remoteKeysDao().getRemoteKeysId(id)
//            }
//        }
//    }
//
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//    }
//}

//@OptIn(ExperimentalPagingApi::class)
//class StoryRemoteMediator(
//    private val userPreference: UserPreference,
//    private val database: StoryDatabase
//) : RemoteMediator<Int, StoryEntity>() {
//
//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, StoryEntity>
//    ): MediatorResult {
//        val page = when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
//            }
//
//            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                val prevKey = remoteKeys?.prevKey
//                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                prevKey
//            }
//
//            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                val nextKey = remoteKeys?.nextKey
//                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                nextKey
//            }
//        }
//
//        return try {
//            val getToken = userPreference.getSession().first()
//            val apiService = ApiConfig.getApiService(getToken.token)
//            val response = apiService.getStory(page, state.config.pageSize)
//            val stories = response.listStory
//            val storiesList = stories.map { story ->
//                StoryEntity(
//                    story.id ?: "",
//                    story.name ?: "",
//                    story.description ?: "",
//                    story.photoUrl ?: "",
//                    story.createdAt ?: "",
//                    story.lat ?: 0.0,
//                    story.lon ?: 0.0
//                )
//            }
//
//            val endOfPaginationReached = response.listStory.isEmpty()
//
//            database.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    database.remoteKeysDao().deleteRemoteKeys()
//                    database.storyDao().deleteAllStories()
//                }
//                val prevKey = if (page == 1) null else page - 1
//                val nextKey = if (endOfPaginationReached) null else page + 1
//                val keys = response.listStory.map {
//                    RemoteKeyEntity(
//                        id = it.id ?: "",
//                        prevKey = prevKey,
//                        nextKey = nextKey
//                    )
//                }
//                database.remoteKeysDao().insertAll(keys)
//                database.storyDao().insertStories(storiesList)
//            }
//            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
//        } catch (e: Exception) {
//            MediatorResult.Error(e)
//        }
//    }
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
//        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
//            database.remoteKeysDao().getRemoteKeysId(data.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
//        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
//            database.remoteKeysDao().getRemoteKeysId(data.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { id ->
//                database.remoteKeysDao().getRemoteKeysId(id)
//            }
//        }
//    }
//
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//    }
//}


@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val userPreference: UserPreference,
    private val database: StoryDatabase
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val getToken = userPreference.getSession().first()
            val apiService = ApiConfig.getApiService(getToken.token)
            val response = apiService.getStories(page, state.config.pageSize)
            val stories = response.listStory
            val storiesList = stories.map { story ->
                StoryEntity(
                    story.id ?: "",
                    story.name ?: "",
                    story.description ?: "",
                    story.photoUrl ?: "",
                    story.createdAt ?: "",
                    story.lat ?: 0.0,
                    story.lon ?: 0.0
                )
            }

            val endOfPaginationReached = response.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAllStories()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = response.listStory.map {
                    RemoteKeyEntity(
                        id = it.id ?: "",
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertStories(storiesList)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
