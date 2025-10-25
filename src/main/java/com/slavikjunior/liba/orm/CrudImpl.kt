package com.slavikjunior.liba.orm

import com.slavikjunior.liba.annotations.CreateMethod
import com.slavikjunior.liba.annotations.DeleteMethod
import com.slavikjunior.liba.annotations.ReadMethod
import com.slavikjunior.liba.annotations.UpdateMethod
import com.slavikjunior.liba.dao.UniversalDao
import com.slavikjunior.liba.db_manager.DbConnectionManager
import com.slavikjunior.liba.utils.toFieldMapByColumnNames
import java.lang.reflect.Method

class CrudImpl(
    private val daoClassesPath: String = "com.slavikjunior.liba.dao.",
    private val dbConnectionManager: DbConnectionManager
): Crud {

    override fun <T : Entity> create(entity: T, idIsAutoGenerate: Boolean): Boolean {
        // получаем дао класс сущности
        val daoClass = getDaoClass(entity::class.java)
        // ищем create метод
        val method = getAnnotatedMethod(daoClass.methods, CreateMethod::class.java)
        // инвокаем его на параметрах
        return method?.invoke(daoClass.constructors[0].newInstance(entity::class.java), entity.toFieldMapByColumnNames()) as Boolean
    }

    override fun <T : Entity> getById(entityClass: Class<T>, id: Int) = getByValues(entityClass, mapOf("id" to id))

    override fun <T : Entity> getByValues(
        entityClass: Class<T>,
        columnsToValues: Map<String, Any?>?
    ): T? {
        // получаем дао класс сущности
        val daoClass = getDaoClass(entityClass)
        // ищем create метод
        val method = getAnnotatedMethod(daoClass.methods, ReadMethod::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.constructors[0].newInstance(entityClass), columnsToValues) as T?
    }

    override fun <T : Entity> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>?): Boolean {
        val daoClass = getDaoClass(entityClass)
        val method = getAnnotatedMethod(daoClass.methods, UpdateMethod::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.constructors[0].newInstance(entityClass), id, columnsToValues) as Boolean
    }

    override fun <T : Entity> updateAndGet(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>?) =
        if (update(entityClass, id, columnsToValues))
            getById(entityClass, id)
        else null

    override fun <T : Entity> deleteByValues(entityClass: Class<T>, columnsToValues: Map<String, Any?>?): Boolean {
        // todo удаление по полям, потом по id,
        //  которое будет вызывать удаление по полям но чисто с id и возможно удаление по объекту
        val daoClass = getDaoClass(entityClass)
        val method = getAnnotatedMethod(daoClass.methods, DeleteMethod::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.constructors[0].newInstance(entityClass), columnsToValues) as Boolean
    }

    override fun <T : Entity> deleteById(entityClass: Class<T>, id: Int) = deleteByValues(entityClass, mapOf("id" to id))

    override fun <T : Entity> deleteByEntity(entity: T) = deleteByValues(entity::class.java, entity.toFieldMapByColumnNames())

    private fun <T> getDaoClass(entityClass: Class<T>): Class<*> {
//        // получаем полное имя дао класса для сущности
//        val daoClassName = "$daoClassesPath${entityClass.simpleName}Dao"
//        // получаем дао класс сущности
//        return Class.forName(daoClassName)
        return UniversalDao::class.java
    }

    private fun getAnnotatedMethod(methods: Array<Method>, annotationClass: Class<out Annotation>) =
        methods.find { method -> method.isAnnotationPresent(annotationClass) }
}