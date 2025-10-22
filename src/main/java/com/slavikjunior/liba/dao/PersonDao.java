package com.slavikjunior.liba.dao;

import com.slavikjunior.liba.annotations.*;
import com.slavikjunior.liba.db_manager.DbConnectionManager;
import com.slavikjunior.liba.exceptions.DbAccessException;
import com.slavikjunior.liba.exceptions.DbConnectionException;
import com.slavikjunior.liba.orm.InterfaceDao;
import com.slavikjunior.models.Person;
import com.slavikjunior.secrets.Keys;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class PersonDao<T> implements InterfaceDao<T> {

    private Connection connection = new DbConnectionManager("testdb", "postgres", "7913").useConnectionOnLocalHostAndStandardPort();

    public PersonDao() throws DbConnectionException {
    }

    @CreateMethod
    public <E> boolean createEntity(Map<String, @WrappedClass E> columnsToValues) throws SQLException {

        var values = columnsToValues.values().stream().toArray();
        var columns = columnsToValues.keySet().toArray();

        int i = 0;
        StringBuilder sb = new StringBuilder("insert into " + Keys.tableName + " (");
        for (var column : columns) {
            sb.append(column);
            if (i < columnsToValues.size() - 1)
                sb.append(", ");
            i++;
        }
        sb.append(") values (");
        i = 0;
        // todo сейчас поддержка null, String и Integer, возможно добавить др типы в будующем
        for (var value : values) {
            if (value == null) {
//                T.class.getDeclaredFields();
                sb.append("null");
                if (i < columnsToValues.size() - 1)
                    sb.append(", ");
                continue;
            } else {
                var valueClass = value.getClass();
                if (valueClass.equals(String.class)) {
                    sb.append("'%s'");
                    if (i < columnsToValues.size() - 1)
                        sb.append(", ");
                } else if (valueClass.equals(Integer.class)) {
                    sb.append("%d");
                    if (i < columnsToValues.size() - 1)
                        sb.append(", ");
                }
            }
            i++;
        }
        sb.append(");");
        return connection.prepareStatement(sb.toString().formatted(values)).executeUpdate() > 0;
    }

    @ReadMethod
    public <E> @Nullable T readEntityByValues(Map<String, @WrappedClass E> columnsToValues) throws SQLException {
        int i = 0;
        StringBuilder sb = new StringBuilder("select * from " + Keys.tableName + " where ");
        for (var entry : columnsToValues.entrySet()) {
            String columnName = entry.getKey();
            E value = entry.getValue();

            // todo сейчас поддержка String и Integer, возможно добавить др типы в будующем
            var valueClass = value.getClass();
            if (valueClass.equals(String.class)) {
                sb.append(columnName).append(" = '%s'");
                if (i < columnsToValues.size() - 1)
                    sb.append(" and ");
            } else if (valueClass.equals(Integer.class)) {
                sb.append(columnName).append(" = %d");
                if (i < columnsToValues.size() - 1)
                    sb.append(" and ");
            }
            i++;
        }
        sb.append(';');

        var values = columnsToValues.values().stream().toArray();
        var ps = connection.prepareStatement(
                sb.toString().formatted(values)
        );

        ResultSet rs = ps.executeQuery();
        Person person = createInstanceByResultSet(rs);

        rs.close();
        ps.close();
        return (T) person;
    }

    // todo возможно заменить id на long
    @UpdateMethod
    public <E> boolean updateEntityByValues(int id, Map<String, @WrappedClass E> columnsToValues) throws SQLException {
        int i = 0;
        StringBuilder sb = new StringBuilder("update " + Keys.tableName + "\nset ");
        for (var entry : columnsToValues.entrySet()) {
            String columnName = entry.getKey();
            E value = entry.getValue();

            // todo сейчас поддержка String и Integer, возможно добавить др типы в будующем
            var valueClass = value.getClass();
            if (valueClass.equals(String.class)) {
                sb.append(columnName).append(" = '%s'");
                if (i < columnsToValues.size() - 1)
                    sb.append(",\n");
            } else if (valueClass.equals(Integer.class)) {
                sb.append(columnName).append(" = %d");
                if (i < columnsToValues.size() - 1)
                    sb.append(",\n");
            }
            i++;
        }
        sb.append("\nwhere id = %d;".formatted(id));

        var values = columnsToValues.values().stream().toArray();
        var ps = connection.prepareStatement(
                sb.toString().formatted(values)
        );

        return ps.executeUpdate() > 0;
    }

    @DeleteMethod
    public <E> boolean deleteEntityByValues(Map<String, @WrappedClass E> columnsToValues) throws DbAccessException {
        int i = 0;
        StringBuilder sb = new StringBuilder("delete from " + Keys.tableName + "\nwhere ");
        for (var entry : columnsToValues.entrySet()) {
            String columnName = entry.getKey();
            E value = entry.getValue();

            // todo сейчас поддержка String и Integer, возможно добавить др типы в будующем
            var valueClass = value.getClass();
            if (valueClass.equals(String.class)) {
                sb.append(columnName).append(" = '%s'");
                if (i < columnsToValues.size() - 1)
                    sb.append(" and ");
            } else if (valueClass.equals(Integer.class)) {
                sb.append(columnName).append(" = %d");
                if (i < columnsToValues.size() - 1)
                    sb.append(" and ");
            }
            i++;
        }
        sb.append(';');

        PreparedStatement ps = null;
        try {
            var values = columnsToValues.values().stream().toArray();
            ps  = connection.prepareStatement(
                    sb.toString().formatted(values)
            );
        } catch (SQLException e) {
            throw new DbAccessException("database access error occurs");
        }

        int cntOfChangedRows = 0;
        try {
            cntOfChangedRows = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new DbAccessException("database access error occurs or driver has determined that the timeout value");
        }
        return cntOfChangedRows > 0;
    }

    private @Nullable Person createInstanceByResultSet(ResultSet rs) throws SQLException {
        Person person = null;
        while (rs.next()) {
            person = new Person(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)
            );
        }
        return person;
    }
}