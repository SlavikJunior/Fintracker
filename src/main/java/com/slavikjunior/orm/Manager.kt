package com.slavikjunior.orm

import com.slavikjunior.annotations.CreateMethod

object Manager: CRUD {

    private const val DAO_CLASSES_PATH = "com.slavikjunior.dao."

    fun init() {

    }

    fun destroy() {

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

    override fun <T : CRUDable?> read(entity: T?) {
        TODO("Not yet implemented")
    }

    override fun <T : CRUDable?> update(entity: T?) {
        TODO("Not yet implemented")
    }

    override fun <T : CRUDable?> delete(entity: T?) {
        TODO("Not yet implemented")
    }
}