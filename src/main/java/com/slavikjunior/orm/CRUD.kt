package com.slavikjunior.orm

import com.slavikjunior.annotations.WrappedClass

interface CRUD {

    fun <T : CRUDable> createEntity(entity: T, idIsAutoGenerate: Boolean = true): Boolean?
    fun <T : CRUDable> getEntityById(entityClass: Class<T>, id: Int): T?
    fun <T : CRUDable, E> getEntityByValues(entityClass: Class<T>, columnsToValues: Map<String, @WrappedClass E>): T?
    fun <T : CRUDable, E> updateEntity(entityClass: Class<T>, id: Int, columnsToValues: Map<String, @WrappedClass E>): Boolean
    fun <T : CRUDable, E> updateEntityAndGet(entityClass: Class<T>, id: Int, columnsToValues: Map<String, @WrappedClass E>): T?
    fun <T : CRUDable> delete(entity: T?)
}