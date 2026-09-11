package com.example.aiworkoutlifeassistant.core.data.repository

import com.example.aiworkoutlifeassistant.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseRepository {

    protected fun <T> safeFlow(
        errorMessage: String = "Terjadi kesalahan",
        block: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(block()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: errorMessage))
        }
    }
}