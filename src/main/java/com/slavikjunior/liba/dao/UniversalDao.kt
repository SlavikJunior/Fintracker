package com.slavikjunior.liba.dao

import com.slavikjunior.liba.annotations.CreateMethod
import com.slavikjunior.liba.annotations.DeleteMethod
import com.slavikjunior.liba.annotations.ReadMethod
import com.slavikjunior.liba.annotations.UpdateMethod
import com.slavikjunior.liba.db_manager.DbConnectionManager
import com.slavikjunior.liba.exceptions.DbAccessException
import com.slavikjunior.liba.exceptions.NotNullableColumnException
import com.slavikjunior.liba.orm.Dao
import com.slavikjunior.liba.utils.isNullableColumn
import com.slavikjunior.secrets.Keys
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

class UniversalDao<T: Any>(
    private val typeParameterClass: Class<T>
) : Dao<T> {
    private val connection = DbConnectionManager(
        "testdb", "postgres", "7913"
    ).useConnection()

    @CreateMethod
    @Throws(DbAccessException::class, NotNullableColumnException::class)
    override fun <E> createEntity(columnsToValues: MutableMap<String, E?>): Boolean {
        val columnsList: MutableList<String?> = ArrayList<String?>()
        val valuesList: MutableList<Any?> = ArrayList<Any?>()

        for (entry in columnsToValues.entries) {
            val column = entry.key
            val value: Any? = entry.value

            // ⚠️ Не вставляем автоинкрементное поле id
            if (column.equals("id", ignoreCase = true) && value == null) continue

            columnsList.add(column)
            valuesList.add(value)
        }

        val columns: Array<Any?> = columnsList.toTypedArray()
        val values = valuesList.toTypedArray()

        var i = 0
        val sb = StringBuilder("insert into " + Keys.tableName + " (")
        for (column in columns) {
            sb.append(column)
            if (i < columnsList.size - 1) sb.append(", ")
            i++
        }
        sb.append(") values (")

        var j = 0
        while (j < values.size - 1) {
            sb.append("?").append(", ")
            j++
        }
        sb.append("?").append(");")

        try {
            var index = 0
            val ps = connection.prepareStatement(sb.toString())
            for (value in values) {
                if (value == null) {
                    val isNullableColumn = typeParameterClass.isNullableColumn(
                        columns[index].toString()
                    )
                    if (isNullableColumn) ps.setNull(index + 1, Types.NULL)
                    else throw NotNullableColumnException("Column " + columns[index] + " is not nullable")
                } else if (value is String) ps.setString(index + 1, value)
                else if (value is Int) ps.setInt(index + 1, value)
                else ps.setObject(index + 1, value)
                index++
            }
            return ps.executeUpdate() > 0
        } catch (e: SQLException) {
            e.printStackTrace()
            throw DbAccessException("Database access error occurs")
        }
    }

    @ReadMethod
    @Throws(SQLException::class)
    override fun <E> readEntityByValues(columnsToValues: MutableMap<String, E?>): T? {
        var i = 0
        val sb = StringBuilder("select * from " + Keys.tableName + " where ")
        for (entry in columnsToValues.entries) {
            val columnName = entry.key
            val value = entry.value

            // todo сейчас поддержка null, String и Integer, возможно добавить др типы в будующем
            if (value == null) {
                sb.append(columnName).append(" is %s")
                if (i < columnsToValues.size - 1) sb.append(" and ")
            } else {
                val valueClass: Class<*> = value.javaClass
                if (valueClass == String::class.java) {
                    sb.append(columnName).append(" = '%s'")
                    if (i < columnsToValues.size - 1) sb.append(" and ")
                } else if (valueClass == Int::class.java) {
                    sb.append(columnName).append(" = %d")
                    if (i < columnsToValues.size - 1) sb.append(" and ")
                }
            }
            i++
        }
        sb.append(';')

        val values = columnsToValues.values.stream().toArray()
        val ps = connection.prepareStatement(
            sb.toString().format(*values)
        )

        val rs = ps.executeQuery()
        val instance = createInstanceByResultSet(rs)

        rs.close()
        ps.close()
        return instance as T?
    }

    // todo возможно заменить id на long
    @UpdateMethod
    @Throws(SQLException::class)
    override fun <E> updateEntityByValues(id: Int, columnsToValues: MutableMap<String, E?>): Boolean {
        var i = 0
        val sb = StringBuilder("update " + Keys.tableName + "\nset ")
        for (entry in columnsToValues.entries) {
            val columnName = entry.key
            val value = entry.value

            //todo сейчас поддержка String и Integer, возможно добавить др типы в будующем
            // добавить обработку null
            if (value != null) {
                val valueClass: Class<*> = value::class.java
                if (valueClass == String::class.java) {
                    sb.append(columnName).append(" = '%s'")
                    if (i < columnsToValues.size - 1) sb.append(",\n")
                } else if (valueClass == Int::class.java) {
                    sb.append(columnName).append(" = %d")
                    if (i < columnsToValues.size - 1) sb.append(",\n")
                }
            }
            i++
        }
        sb.append("\nwhere id = %d;".format(id))

        val values = columnsToValues.values.stream().toArray()
        val ps = connection.prepareStatement(
            sb.toString().format(*values)
        )

        return ps.executeUpdate() > 0
    }

    @DeleteMethod
    @Throws(DbAccessException::class)
    override fun <E> deleteEntityByValues(columnsToValues: MutableMap<String, E?>): Boolean {
        var i = 0
        val sb = StringBuilder("delete from " + Keys.tableName + "\nwhere ")
        for (entry in columnsToValues.entries) {
            val columnName = entry.key
            val value = entry.value

            // todo сейчас поддержка String и Integer, возможно добавить др типы в будующем
            //  добавить обработку null
            if (value != null) {
                val valueClass: Class<*> = value::class.java
                if (valueClass == String::class.java) {
                    sb.append(columnName).append(" = '%s'")
                    if (i < columnsToValues.size - 1) sb.append(" and ")
                } else if (valueClass == Int::class.java) {
                    sb.append(columnName).append(" = %d")
                    if (i < columnsToValues.size - 1) sb.append(" and ")
                }
            }
            i++
        }
        sb.append(';')

        var ps: PreparedStatement? = null
        try {
            val values = columnsToValues.values.stream().toArray()
            ps = connection.prepareStatement(
                sb.toString().format(*values)
            )
        } catch (e: SQLException) {
            throw DbAccessException("database access error occurs")
        }

        var cntOfChangedRows = 0
        try {
            cntOfChangedRows = ps.executeUpdate()
            ps.close()
        } catch (e: SQLException) {
            throw DbAccessException("database access error occurs or driver has determined that the timeout value")
        }
        return cntOfChangedRows > 0
    }

    fun createInstanceByResultSet(rs: ResultSet): Any? {
        val rsmd = rs.metaData
        val columCount = rsmd.columnCount

        val initArgs = mutableListOf<Any?>()
        // todo добавить поддержку возврата множества объектов
        while (rs.next()) {
//            val initArgs = mutableListOf<Any?>()
            for (i in 1..columCount) {
                val type = rsmd.getColumnClassName(i)
                val label = rsmd.getColumnName(i)

                // todo добавить обработку типов
                initArgs += when (type) {
                    String::class.java.name -> rs.getString(label)
                    Integer::class.java.name -> rs.getInt(label)
                    Object::class.java.name -> rs.getObject(label)
                    else -> {}
                }
            }
//            return typeParameterClass.constructors[0].newInstance(initArgs)
        }
        return if (initArgs.size == columCount)
            typeParameterClass.constructors[0].newInstance(*initArgs.toTypedArray())
        else return null
    }
}