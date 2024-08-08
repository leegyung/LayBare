package com.project.data.mapper

import com.project.data.model.AddressComponentDto
import com.project.data.model.SearchAddressDto
import com.project.domain.entity.AddressComponentEntity
import com.project.domain.entity.SearchAddressEntity

object AddressDataMapper {
    fun getAddressEntity(dto : SearchAddressDto?) : SearchAddressEntity {
        val components = arrayListOf<AddressComponentEntity>()
        var fullAddress = ""

        dto?.results?.getOrNull(0)?.let{ data ->
            data.address_components?.let {
                for(comp in it){
                    components.add(getComponentEntity(comp))
                }
            }

            fullAddress = data.formatted_address?:""
        }

        return SearchAddressEntity(
            fullAddress,
            components
        )
    }

    private fun getComponentEntity(dto : AddressComponentDto) : AddressComponentEntity {
        return AddressComponentEntity(
            dto.long_name?:"",
            dto.short_name?:"",
            dto.types?: arrayListOf()
        )
    }
}