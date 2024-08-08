package com.project.data.model

data class SearchAddressDto(
    val results : ArrayList<SearchAddressDetailDto>?
)

data class SearchAddressDetailDto(
    val address_components : ArrayList<AddressComponentDto>?,
    val formatted_address : String?
)

data class AddressComponentDto(
    val long_name : String?,
    val short_name : String?,
    val types : ArrayList<String>?
)