package com.project.domain.entity

data class SearchAddressEntity (
    val fullAddress : String,
    val components : ArrayList<AddressComponentEntity>
)

data class AddressComponentEntity(
    val long_name : String,
    val short_name : String,
    val types : ArrayList<String>
)

