package com.slavikjunior.orm

import com.slavikjunior.annotations.CreateMethod
import com.slavikjunior.annotations.DeleteMethod
import com.slavikjunior.annotations.ReadMethod
import com.slavikjunior.annotations.ReadMethodByColumnAndValue
import com.slavikjunior.annotations.UpdateMethod
import com.slavikjunior.models.Person

object Manager : CRUD {

    private const val DAO_CLASSES_PATH = "com.slavikjunior.dao."
    val name = "absa"
    fun init() {

    }

    fun destroy() {

    }

    fun <T : CRUDable?> managment(entity: T?, properties: List<Any?>, action: DataAction) {
        // получаем полное имя дао класса для сущности
        val daoClassName = "$DAO_CLASSES_PATH${entity?.javaClass?.simpleName}Dao"
        // получаем дао класс сущности
        val daoClass = Class.forName(daoClassName)
        // забираем поля, которые надо заполнять, исключаем id-шник
        // val fields = entity?.javaClass?.declaredFields?.filter { field -> !field.isAnnotationPresent(GeneratedValue::class.java) }?.map { field -> field.name }
        // забираем все методы дао класса
        val methods = daoClass.methods
        // ищем create метод
        val method = methods.find { method -> method.isAnnotationPresent(peekAnnotationClassForMethodWithAction(action)) }
        // инвокаем его на переданных параметрах
        method?.invoke(daoClass.newInstance(), *properties.toTypedArray())
    }

    override fun <T : CRUDable?> create(entity: T?, properties: List<Any?>) {
        // получаем полное имя дао класса для сущности
        val daoClassName = "$DAO_CLASSES_PATH${entity?.javaClass?.simpleName}Dao"
        // получаем дао класс сущности
        val daoClass = Class.forName(daoClassName)
        // забираем поля, которые надо заполнять, исключаем id-шник
        // val fields = entity?.javaClass?.declaredFields?.filter { field -> !field.isAnnotationPresent(GeneratedValue::class.java) }?.map { field -> field.name }
        // забираем все методы дао класса
        val methods = daoClass.methods
        // ищем create метод
        val method = methods.find { method -> method.isAnnotationPresent(CreateMethod::class.java) }
        // инвокаем его на переданных параметрах
        method?.invoke(daoClass.newInstance(), *properties.toTypedArray())
    }

    override fun <T : CRUDable?> read(entity: T?, id: Int?): T? {
        // получаем полное имя дао класса для сущности
        val daoClassName = "$DAO_CLASSES_PATH${entity?.javaClass?.simpleName}Dao"
        // получаем дао класс сущности
        val daoClass = Class.forName(daoClassName)
        // забираем поля, которые надо заполнять, исключаем id-шник
        // val fields = entity?.javaClass?.declaredFields?.filter { field -> !field.isAnnotationPresent(GeneratedValue::class.java) }?.map { field -> field.name }
        // забираем все методы дао класса
        val methods = daoClass.methods
        // ищем create метод
        val method = methods.find { method -> method.isAnnotationPresent(ReadMethod::class.java) }
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), id) as T
    }

    // пока просто враппер приходит из параметров, позже можно тоже как-то упростить жизнь юзеру, допустим enum
    fun <T : CRUDable?> readWithColumnAndValue(entity: T?, wrapper: Wrapper): T? {
        // получаем полное имя дао класса для сущности
        val daoClassName = "$DAO_CLASSES_PATH${entity?.javaClass?.simpleName}Dao"
        // получаем дао класс сущности
        val daoClass = Class.forName(daoClassName)
        // забираем поля, которые надо заполнять, исключаем id-шник
        // val fields = entity?.javaClass?.declaredFields?.filter { field -> !field.isAnnotationPresent(GeneratedValue::class.java) }?.map { field -> field.name }
        // забираем все методы дао класса
        val methods = daoClass.methods
        // ищем create метод
        val method = methods.find { method -> method.isAnnotationPresent(ReadMethodByColumnAndValue::class.java) }
        // инвокаем его на переданных параметрах
        return method?.invoke(daoClass.newInstance(), wrapper) as T
    }

    override fun <T : CRUDable?> update(entity: T?) {
        TODO("Not yet implemented")
    }

    override fun <T : CRUDable?> delete(entity: T?) {
        TODO("Not yet implemented")
    }

    private fun peekAnnotationClassForMethodWithAction(action: DataAction) =
        when (action) {
            DataAction.CREATE -> CreateMethod::class.java
            DataAction.READ -> ReadMethod::class.java
            DataAction.UPDATE -> UpdateMethod::class.java
            DataAction.DELETE -> DeleteMethod::class.java
        }
}