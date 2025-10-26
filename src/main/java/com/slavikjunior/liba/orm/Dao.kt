package com.slavikjunior.liba.orm

import com.slavikjunior.liba.annotations.CreateMethod
import com.slavikjunior.liba.annotations.DeleteMethod
import com.slavikjunior.liba.annotations.ReadMethod
import com.slavikjunior.liba.annotations.UpdateMethod
import java.sql.SQLException

interface Dao<T> {
    @CreateMethod
    @Throws(SQLException::class)
    fun createEntity(columnsToValues: Map<String, Any?>): Boolean

    @ReadMethod
    @Throws(SQLException::class)
    fun readEntityByValues(columnsToValues: Map<String, Any?>): List<T>?

    @UpdateMethod
    @Throws(SQLException::class)
    fun updateEntityByValues(id: Int, columnsToValues: Map<String, Any?>): Boolean

    @DeleteMethod
    @Throws(SQLException::class)
    fun deleteEntityByValues(columnsToValues: Map<String, Any?>): Boolean
}