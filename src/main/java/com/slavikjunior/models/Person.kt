package com.slavikjunior.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.slavikjunior.annotations.*
import com.slavikjunior.orm.CRUDable

@Entity
@Table(shema = "json", name = "person")
data class Person(
    @Id
    @GeneratedValue
    @JsonProperty(value = "id")
    val id: Int? = null,
    @JsonProperty(value = "first_name")
    val firstName: String? = null,
    @JsonProperty(value = "last_name")
    val lastName: String? = null,
    @JsonProperty(value = "email")
    val email: String? = null,
    @JsonProperty(value = "gender")
    val gender: String? = null,
    @JsonProperty(value = "ip_address")
    val ipAddress: String? = null,
    @JsonProperty(value = "country")
    val country: String? = null
): CRUDable