package com.project.domain.repository

import com.project.domain.entity.SearchAddressEntity
import com.project.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface SearchAddressRepository {
    suspend fun getAddressData(key : String, geocode : String) : SearchAddressEntity
}