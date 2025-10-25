package com.slavikjunior.liba.orm

import com.slavikjunior.liba.annotations.CreateMethod
import com.slavikjunior.liba.annotations.DeleteMethod
import com.slavikjunior.liba.annotations.ReadMethod
import com.slavikjunior.liba.annotations.UpdateMethod
import java.sql.SQLException

interface Dao<T> {
    @CreateMethod
    @Throws(SQLException::class)
    fun <E> createEntity(columnsToValues: MutableMap<String, E?>): Boolean

    @ReadMethod
    @Throws(SQLException::class)
    fun <E> readEntityByValues(columnsToValues: MutableMap<String, E?>): T?

    @UpdateMethod
    @Throws(SQLException::class)
    fun <E> updateEntityByValues(id: Int, columnsToValues: MutableMap<String, E?>): Boolean

    @DeleteMethod
    @Throws(SQLException::class)
    fun <E> deleteEntityByValues(columnsToValues: MutableMap<String, E?>): Boolean
}