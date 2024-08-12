package com.project.data.repositoryImpl

import com.project.data.api.SearchAddressApi
import com.project.data.mapper.AddressDataMapper
import com.project.domain.entity.SearchAddressEntity
import com.project.domain.repository.SearchAddressRepository
import javax.inject.Inject

class SearchAddressRepositoryImpl @Inject constructor(private val mApiService : SearchAddressApi) : SearchAddressRepository {
    override suspend fun getAddressData(key: String, geocode: String): SearchAddressEntity {

        val response = mApiService.getAddressData(geocode, key)
        return AddressDataMapper.getAddressEntity(response)

    }

}