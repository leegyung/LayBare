package com.project.domain.usecase

import com.project.domain.entity.SearchAddressEntity
import com.project.domain.repository.SearchAddressRepository
import com.project.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(private val mRepository: SearchAddressRepository) {
    suspend fun getAddress(key : String, geocode : String) : Flow<ApiResult<SearchAddressEntity>> {
        return mRepository.getAddressData(key, geocode)
    }
}