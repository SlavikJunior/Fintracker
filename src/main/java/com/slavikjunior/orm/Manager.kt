package com.slavikjunior.orm

import com.slavikjunior.annotations.*
import com.slavikjunior.util.toAnnotatedFieldsValuesListWithoutAnnotation
import java.lang.reflect.Method

object Manager : CRUD {

    private const val DAO_CLASSES_PATH = "com.slavikjunior.dao."
    val name = "absa"
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

        // получаем полное имя дао класса для сущности
        val daoClassName = "$DAO_CLASSES_PATH${entity?.javaClass?.simpleName}Dao"
        // получаем дао класс сущности
        val daoClass = Class.forName(daoClassName)
        // забираем все методы дао класса
        val methods = daoClass.methods
        // ищем create метод
        val method = methods.find { method -> method.isAnnotationPresent(CreateMethod::class.java) } as Method
        // инвокаем его на параметрах
        return properties.let { method.invoke(daoClass.newInstance(), *it.toTypedArray()) } as Boolean?
    }

    override fun <T : CRUDable> getEntityById(entityClass: Class<T>, id: Int): T? {
        // получаем полное имя дао класса для сущности
        val daoClassName = "$DAO_CLASSES_PATH${entityClass.simpleName}Dao"
        // получаем дао класс сущности
        val daoClass = Class.forName(daoClassName)
        // забираем все методы дао класса
        val methods = daoClass.methods
        // ищем read метод
        val method = methods.find { method -> method.isAnnotationPresent(ReadMethod::class.java) }
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), id) as T?
    }

    override fun <T : CRUDable, E> getEntityByPairsOfColumnsAndValues(
        entityClass: Class<T>,
        columnsToValues: Map<String, @WrappedClass E>
    ): T? {
        // получаем дао класс сущности
        val daoClass = Class.forName("$DAO_CLASSES_PATH${entityClass.simpleName}Dao")
        // забираем все методы дао класса
        val methods = daoClass.methods
        // ищем create метод
        val method = methods.find { method -> method.isAnnotationPresent(ReadMethodByColumnsAndValues::class.java) }
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), columnsToValues) as T?
    }

    override fun <T : CRUDable> update(entity: T?) {
        TODO("Not yet implemented")
    }

    override fun <T : CRUDable> delete(entity: T?) {
        TODO("Not yet implemented")
    }
}