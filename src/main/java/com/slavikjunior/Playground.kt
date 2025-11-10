package com.slavikjunior

import com.slavikjunior.deorm.db_manager.DbConnectionManager
import com.slavikjunior.deorm.orm.EntityManager
import com.slavikjunior.models.User

fun main() {
    DbConnectionManager.initialize()

    EntityManager.queryBuilder()
        .select("*")
        .from("users")
        .execute()
}