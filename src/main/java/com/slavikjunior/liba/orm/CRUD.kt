package com.slavikjunior.liba.orm

import com.slavikjunior.liba.annotations.WrappedClass

interface CRUD {

    fun <T : CRUDable> create(entity: T, idIsAutoGenerate: Boolean = true): Boolean
    fun <T : CRUDable> getById(entityClass: Class<T>, id: Int): T?
    fun <T : CRUDable, E> getByValues(entityClass: Class<T>, columnsToValues: Map<String, @WrappedClass E>): T?
    fun <T : CRUDable, E> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, @WrappedClass E>): Boolean
    fun <T : CRUDable, E> updateAndGet(entityClass: Class<T>, id: Int, columnsToValues: Map<String, @WrappedClass E>): T?
    fun <T : CRUDable, E> deleteByValues(entityClass: Class<T>, columnsToValues: Map<String, @WrappedClass E>): Boolean
    fun <T : CRUDable> deleteById(entityClass: Class<T>, id: Int): Boolean
    fun <T : CRUDable> deleteByEntity(entity: T): Boolean
}