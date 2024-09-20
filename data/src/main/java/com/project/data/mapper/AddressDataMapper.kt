package com.project.data.mapper

import com.project.data.model.AddressComponentDto
import com.project.data.model.SearchAddressDto
import com.project.domain.entity.AddressComponentEntity
import com.project.domain.entity.SearchAddressEntity


fun SearchAddressDto.toSearchAddress() : SearchAddressEntity {
    val components = arrayListOf<AddressComponentEntity>()
    var fullAddress = ""

    results?.getOrNull(0)?.let{ data ->
        data.address_components?.let {
            for(comp in it){
                components.add(comp.toAddressComponent())
            }
        }

        fullAddress = data.formatted_address?:""
    }

    return SearchAddressEntity(
        fullAddress,
        components
    )
}

fun AddressComponentDto.toAddressComponent() : AddressComponentEntity {
    return AddressComponentEntity(
        long_name = long_name?:"",
        short_name = short_name?:"",
        types = types?: arrayListOf()
    )
}

