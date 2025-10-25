package com.slavikjunior.liba.orm

object EntityManager {

    fun <T : Entity> create(entity: T, idIsAutoGenerate: Boolean = true) =
        CrudImpl.create(entity, idIsAutoGenerate)

    fun <T : Entity> get(entityClass: Class<T>, id: Int) = CrudImpl.getById(entityClass, id)

    fun <T : Entity> get(entityClass: Class<T>, columnsToValues: Map<String, Any?>?) =
        CrudImpl.getByValues(entityClass, columnsToValues)

    fun <T : Entity> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>?, get: Boolean = false) =
        if (get) CrudImpl.updateAndGet(entityClass, id, columnsToValues)
        else CrudImpl.update(entityClass, id, columnsToValues)

    fun <T : Entity> delete(entityClass: Class<T>, columnsToValues: Map<String, Any?>?) =
        CrudImpl.deleteByValues(entityClass, columnsToValues)

    fun <T : Entity> delete(entityClass: Class<T>, id: Int) =
        CrudImpl.deleteById(entityClass, id)

    fun <T : Entity> delete(entity: T) =
        CrudImpl.deleteByEntity(entity)
}