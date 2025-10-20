package com.slavikjunior.orm

import com.slavikjunior.annotations.WrappedClass

interface CRUD {

    fun <T : CRUDable> createEntity(entity: T, idIsAutoGenerate: Boolean = true): Boolean?
    fun <T : CRUDable> getEntityById(entityClass: Class<T>, id: Int): T?
    fun <T : CRUDable, E> getEntityByPairsOfColumnsAndValues(entityClass: Class<T>, columnsToValues: Map<String, @WrappedClass E>): T?
    fun <T : CRUDable> update(entity: T?)
    fun <T : CRUDable> delete(entity: T?)
}