package com.slavikjunior.liba.orm

interface Crud {

    fun <T : Entity> create(entity: T): Boolean
    fun <T : Entity> getById(entityClass: Class<T>, id: Int): List<T>?
    fun <T : Entity> getByValues(entityClass: Class<T>, columnsToValues: Map<String, Any?>): List<T>?
    fun <T : Entity> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>): Boolean
    fun <T : Entity> updateAndGet(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>): List<T>?
    fun <T : Entity> deleteByValues(entityClass: Class<T>, columnsToValues: Map<String, Any?>): Boolean
    fun <T : Entity> deleteById(entityClass: Class<T>, id: Int): Boolean
    fun <T : Entity> deleteByEntity(entity: T): Boolean
}