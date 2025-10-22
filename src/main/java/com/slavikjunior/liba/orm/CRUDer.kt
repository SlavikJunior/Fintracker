package com.slavikjunior.liba.orm

import com.slavikjunior.liba.annotations.CreateMethod
import com.slavikjunior.liba.annotations.DeleteMethod
import com.slavikjunior.liba.annotations.ReadMethod
import com.slavikjunior.liba.annotations.UpdateMethod
import com.slavikjunior.liba.annotations.WrappedClass
import com.slavikjunior.liba.dao.UniversalDao
import com.slavikjunior.liba.db_manager.DbConnectionManager
import com.slavikjunior.liba.utils.toFieldMapByColumnNames
import java.lang.reflect.Method

class CRUDer(
    private val daoClassesPath: String = "com.slavikjunior.liba.dao.",
    private val dbConnectionManager: DbConnectionManager
): CRUD {

    override fun <T : CRUDable> create(entity: T, idIsAutoGenerate: Boolean): Boolean {
        // получаем дао класс сущности
        val daoClass = getDaoClass(entity::class.java)
        // ищем create метод
        val method = getAnnotatedMethod(daoClass.methods, CreateMethod::class.java)
        // инвокаем его на параметрах
        return method?.invoke(UniversalDao(entity::class.java), entity.toFieldMapByColumnNames()) as Boolean
    }

    override fun <T : CRUDable> getById(entityClass: Class<T>, id: Int) = getByValues(entityClass, mapOf("id" to id))

    override fun <T : CRUDable, E> getByValues(
        entityClass: Class<T>,
        columnsToValues: Map<String, @WrappedClass E>
    ): T? {
        // получаем дао класс сущности
        val daoClass = getDaoClass(entityClass)
        // ищем create метод
        val method = getAnnotatedMethod(daoClass.methods, ReadMethod::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), columnsToValues) as T?
    }

    override fun <T : CRUDable, E> update(
        entityClass: Class<T>,
        id: Int,
        columnsToValues: Map<String, @WrappedClass E>
    ): Boolean {
        val daoClass = getDaoClass(entityClass)
        val method = getAnnotatedMethod(daoClass.methods, UpdateMethod::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), id, columnsToValues) as Boolean
    }

    override fun <T : CRUDable, E> updateAndGet(
        entityClass: Class<T>,
        id: Int,
        columnsToValues: Map<String, @WrappedClass E>
    ) =
        if (update(entityClass, id, columnsToValues))
            getById(entityClass, id)
        else null

    override fun <T : CRUDable, E> deleteByValues(entityClass: Class<T>, columnsToValues: Map<String, @WrappedClass E>): Boolean {
        // todo удаление по полям, потом по id,
        //  которое будет вызывать удаление по полям но чисто с id и возможно удаление по объекту
        val daoClass = getDaoClass(entityClass)
        val method = getAnnotatedMethod(daoClass.methods, DeleteMethod::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), columnsToValues) as Boolean
    }

    override fun <T : CRUDable> deleteById(entityClass: Class<T>, id: Int) = deleteByValues(entityClass, mapOf("id" to id))

    override fun <T : CRUDable> deleteByEntity(entity: T) = deleteByValues(entity::class.java, entity.toFieldMapByColumnNames())

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