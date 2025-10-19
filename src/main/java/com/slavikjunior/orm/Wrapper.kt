package com.slavikjunior.orm

data class Wrapper(
    val columnName: String,
    val intValue: Int? = null,
    val stringValue: String? = null,
    val isContainsStringValue: Boolean = false
)