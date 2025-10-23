package com.slavikjunior.models

import com.slavikjunior.liba.annotations.Column
import com.slavikjunior.liba.orm.Entity
import com.slavikjunior.liba.utils.SupportedTypes

data class Person(
    @Column(name = "id", type = SupportedTypes.Integer, nullable = true)
    var id: Int? = null,
    @Column(name = "firstname", type = SupportedTypes.String, nullable = true)
    var firstName: String? = null,
    @Column(name = "lastname", type = SupportedTypes.String, nullable = true)
    var lastName: String? = null,
    @Column(name = "email", type = SupportedTypes.String, nullable = true)
    var email: String? = null,
    @Column(name = "gender", type = SupportedTypes.String, nullable = true)
    var gender: String? = null,
    @Column(name = "ipaddress", type = SupportedTypes.String, nullable = true)
    var ipAddress: String? = null,
    @Column(name = "country", type = SupportedTypes.String, nullable = true)
    var country: String? = null
): Entity