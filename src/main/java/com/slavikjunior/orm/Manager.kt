package com.slavikjunior.orm

import com.slavikjunior.annotations.*
import com.slavikjunior.util.toAnnotatedFieldsValuesListWithoutAnnotation
import java.lang.reflect.Method

object Manager : CRUD {

    // todo заменить потом на параметр приходящий снаружи,
    // возможно в функции init получать его или в констукторе или фабрике
    private const val DAO_CLASSES_PATH = "com.slavikjunior.dao."
    fun init() {

    }

    fun destroy() {

    }

    override fun <T : CRUDable> createEntity(entity: T, idIsAutoGenerate: Boolean): Boolean? {
        // если id генерится автоматически то мы раскатываем объект
        // в лист пропертей, но без id, иначе -> с ним
        val properties = if (idIsAutoGenerate)
            entity.toAnnotatedFieldsValuesListWithoutAnnotation(Id::class)
//      пока не предусмотрена возможность занесения сущности в таблицу с id,
//      но надо написать соответсвующий метод в дао и всё
//        else entity.toFieldValuesList()
        else return false

        // получаем дао класс сущности
        val daoClass = getDaoClass(entity::class.java)
        // ищем create метод
        val method = getAnnotatedMethod(daoClass.methods, CreateMethod::class.java)
        // инвокаем его на параметрах
        return properties.let { method?.invoke(daoClass.newInstance(), *it.toTypedArray()) } as Boolean?
    }

    override fun <T : CRUDable> getEntityById(entityClass: Class<T>, id: Int): T? {
        // получаем дао класс сущности
        val daoClass = getDaoClass(entityClass)
        // ищем read метод
        val method = getAnnotatedMethod(daoClass.methods, ReadMethod::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), id) as T?
    }

    override fun <T : CRUDable, E> getEntityByPairsOfColumnsAndValues(entityClass: Class<T>, columnsToValues: Map<String, @WrappedClass E>): T? {
        // получаем дао класс сущности
        val daoClass = Class.forName("$DAO_CLASSES_PATH${entityClass.simpleName}Dao")
        // ищем create метод
        val method = getAnnotatedMethod(daoClass.methods, ReadMethodByColumnsAndValues::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), columnsToValues) as T?
    }

    override fun <T : CRUDable, E> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, @WrappedClass E>): Boolean {
        val daoClass = getDaoClass(entityClass)
        val method = getAnnotatedMethod(daoClass.methods, UpdateMethod::class.java)
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), id, columnsToValues) as Boolean
    }

    override fun <T : CRUDable, E> updateAndGet(entityClass: Class<T>, id: Int, columnsToValues: Map<String, @WrappedClass E>): T? {
        TODO("Not yet implemented")
    }

    override fun <T : CRUDable> delete(entity: T?) {
        TODO("Not yet implemented")
    }

    private fun <T> getDaoClass(entityClass: Class<T>): Class<*> {
        // получаем полное имя дао класса для сущности
        val daoClassName = "$DAO_CLASSES_PATH${entityClass.simpleName}Dao"
        // получаем дао класс сущности
        return Class.forName(daoClassName)
    }

    private fun getAnnotatedMethod(methods: Array<Method>, annotationClass: Class<out Annotation>) = methods.find { method -> method.isAnnotationPresent(annotationClass) }
}