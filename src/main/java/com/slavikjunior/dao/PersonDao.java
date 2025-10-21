package com.slavikjunior.dao;

import com.slavikjunior.annotations.*;
import com.slavikjunior.models.Person;
import com.slavikjunior.orm.InterfaceDao;
import com.slavikjunior.secrets.Keys;
import org.jetbrains.annotations.Nullable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import static com.slavikjunior.db_manager.DbConnectionManagerKt.getConnection;

public class PersonDao implements InterfaceDao<Person> {

    private Connection connection = getConnection(Keys.databaseName, Keys.user, Keys.password);

    @CreateMethod
    public <E> boolean createEntity(Map<String, @WrappedClass E> columnsToValues) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

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
        for (var value : values) {
            // todo сейчас поддержка String и Integer, возможно добавить др типы в будующем
            var valueClass = value.getClass();
            if (valueClass.equals(String.class)) {
                sb.append("'%s'");
                if (i < columnsToValues.size() - 1)
                    sb.append(", ");
            }

            else if (valueClass.equals(Integer.class)) {
                sb.append("%d");
                if (i < columnsToValues.size() - 1)
                    sb.append(", ");
            }
            i++;
        }
        sb.append(");");
        return connection.prepareStatement(sb.toString().formatted(values)).executeUpdate() > 0;
    }

    @ReadMethod
    public <E> @Nullable Person readEntityByValues(Map<String, @WrappedClass E> columnsToValues) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

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
            }

            else if (valueClass.equals(Integer.class)) {
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
        return person;
    }

    // todo возможно заменить id на long
    @UpdateMethod
    public <E> boolean updateEntityByValues(int id, Map<String, @WrappedClass E> columnsToValues) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

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
            }

            else if (valueClass.equals(Integer.class)) {
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
    public <E> boolean deleteEntityByValues(Map<String, @WrappedClass E> columnsToValues) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

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
            }

            else if (valueClass.equals(Integer.class)) {
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

        int cntOfChangedRows = ps.executeUpdate();
        ps.close();
        return cntOfChangedRows > 0;
    }

    private boolean isConnectionEstablished() {
        if (connection != null)
            return true;
        else
            return false;
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