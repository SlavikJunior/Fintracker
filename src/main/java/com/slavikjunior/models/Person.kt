package com.slavikjunior.models

import com.slavikjunior.annotations.*
import com.slavikjunior.orm.CRUDable

data class Person(
    @Column(name = "id")
    val id: Int? = null,
    @Column(name = "firstname")
    val firstName: String? = null,
    @Column(name = "lastname")
    val lastName: String? = null,
    @Column(name = "email")
    val email: String? = null,
    @Column(name = "gender")
    val gender: String? = null,
    @Column(name = "ipaddress")
    val ipAddress: String? = null,
    @Column(name = "country")
    val country: String? = null
): CRUDable