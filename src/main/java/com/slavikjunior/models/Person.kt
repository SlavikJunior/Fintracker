package com.slavikjunior.models

import com.slavikjunior.liba.annotations.Column
import com.slavikjunior.liba.orm.CRUDable
import com.slavikjunior.liba.utils.SupportedTypes

data class Person(
    @Column(name = "id", type = SupportedTypes.Integer, nullable = true)
    val id: Int? = null,
    @Column(name = "firstname", type = SupportedTypes.String, nullable = true)
    val firstName: String? = null,
    @Column(name = "lastname", type = SupportedTypes.String, nullable = true)
    val lastName: String? = null,
    @Column(name = "email", type = SupportedTypes.String, nullable = true)
    val email: String? = null,
    @Column(name = "gender", type = SupportedTypes.String, nullable = true)
    val gender: String? = null,
    @Column(name = "ipaddress", type = SupportedTypes.String, nullable = true)
    val ipAddress: String? = null,
    @Column(name = "country", type = SupportedTypes.String, nullable = true)
    val country: String? = null
): CRUDable