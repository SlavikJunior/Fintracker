package com.slavikjunior.liba.orm

import com.slavikjunior.liba.db_manager.DbConnectionManager

object EntityManager {

    // todo убрать потом
    private val crudOperations = CrudImpl(
        dbConnectionManager = DbConnectionManager("testdb", "postgres", "7913")
    )

    fun <T : Entity> create(entity: T, idIsAutoGenerate: Boolean = true) =
        crudOperations.create(entity, idIsAutoGenerate)

    fun <T : Entity> get(entityClass: Class<T>, id: Int) = crudOperations.getById(entityClass, id)

    fun <T : Entity> get(entityClass: Class<T>, columnsToValues: Map<String, Any?>?) =
        crudOperations.getByValues(entityClass, columnsToValues)

    fun <T : Entity> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>?, get: Boolean = false) =
        if (get) crudOperations.updateAndGet(entityClass, id, columnsToValues)
        else crudOperations.update(entityClass, id, columnsToValues)

    fun <T : Entity> delete(entityClass: Class<T>, columnsToValues: Map<String, Any?>?) =
        crudOperations.deleteByValues(entityClass, columnsToValues)

    fun <T : Entity> delete(entityClass: Class<T>, id: Int) =
        crudOperations.deleteById(entityClass, id)

    fun <T : Entity> delete(entity: T) =
        crudOperations.deleteByEntity(entity)
}