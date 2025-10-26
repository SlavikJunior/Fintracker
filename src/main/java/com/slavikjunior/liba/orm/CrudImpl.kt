package com.slavikjunior.liba.orm

import com.slavikjunior.liba.dao.UniversalDao
import com.slavikjunior.liba.utils.toFieldMapByColumnNames

internal object CrudImpl : Crud {

    private var dao: Dao<*>? = null

    override fun <T : Entity> create(entity: T): Boolean {
        return getDaoInstance(entity::class.java).createEntity(entity.toFieldMapByColumnNames())
    }

    override fun <T : Entity> getById(entityClass: Class<T>, id: Int) = getByValues(entityClass, mapOf("id" to id))

    override fun <T : Entity> getByValues(
        entityClass: Class<T>,
        columnsToValues: Map<String, Any?>
    ) = getDaoInstance(entityClass).readEntityByValues(columnsToValues) as List<T>?


    override fun <T : Entity> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>) =
        getDaoInstance(entityClass).updateEntityByValues(id, columnsToValues)


    override fun <T : Entity> updateAndGet(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>) =
        if (update(entityClass, id, columnsToValues))
            getById(entityClass, id) as T?
        else null

    override fun <T : Entity> deleteByValues(entityClass: Class<T>, columnsToValues: Map<String, Any?>) =
        getDaoInstance(entityClass).deleteEntityByValues(columnsToValues)

    override fun <T : Entity> deleteById(entityClass: Class<T>, id: Int) =
        deleteByValues(entityClass, mapOf("id" to id))

    override fun <T : Entity> deleteByEntity(entity: T) =
        deleteByValues(entity::class.java, entity.toFieldMapByColumnNames())

    private fun getDaoInstance(entityClass: Class<*>): Dao<*> {
        if (dao == null)
            dao = UniversalDao(entityClass)
        return dao as Dao<*>
    }
}