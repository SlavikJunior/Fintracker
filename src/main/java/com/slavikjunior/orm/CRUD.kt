package com.slavikjunior.orm

interface CRUD {

    fun <T : CRUDable> create(entity: T, idIsAutoGenerate: Boolean = true)
    fun <T : CRUDable> read(entity: T?, id: Int?): T?
    fun <T : CRUDable> update(entity: T?)
    fun <T : CRUDable> delete(entity: T?)
    fun <T : CRUDable> execute(entity: T, action: DataAction): Boolean
}