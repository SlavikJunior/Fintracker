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
    @Column(name = "id")
    val id: Int? = null,
    @JsonProperty(value = "first_name")
    @Column(name = "firstname")
    val firstName: String? = null,
    @JsonProperty(value = "last_name")
    @Column(name = "lastname")
    val lastName: String? = null,
    @JsonProperty(value = "email")
    @Column(name = "email")
    val email: String? = null,
    @JsonProperty(value = "gender")
    @Column(name = "gender")
    val gender: String? = null,
    @JsonProperty(value = "ip_address")
    @Column(name = "ipaddress")
    val ipAddress: String? = null,
    @JsonProperty(value = "country")
    @Column(name = "country")
    val country: String? = null
): CRUDable