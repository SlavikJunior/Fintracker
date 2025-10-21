package com.slavikjunior.dao;

import com.slavikjunior.annotations.*;
import com.slavikjunior.models.Person;
import com.slavikjunior.secrets.Keys;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static com.slavikjunior.db_manager.DbConnectionManagerKt.getConnection;

@Database(name = Keys.databaseName)
@Table(name = Keys.tableName)
public class PersonDao {

    private Connection connection = getConnection(Keys.databaseName, Keys.user, Keys.password);

    @CreateMethod
    public boolean createUser(String firstName, String lastName, String email, String gender, String ipAddress, String country) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

        var ps = connection.prepareStatement(
                """
                        INSERT INTO  %s (
                            firstname, lastname, email, gender, ipaddress, country
                            ) VALUES (?, ?, ?, ?, ?, ?)
                        """.formatted(Keys.tableName)
        );
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, email);
        ps.setString(4, gender);
        ps.setString(5, ipAddress);
        ps.setString(6, country);
        return ps.execute();
    }

    // todo возможно заменить id на long
    @ReadMethod
    public @Nullable Person readUserById(int id) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

        var ps =
                connection.prepareStatement("select * from persons where id = ?;");
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        Person person = createPerson(rs);
        rs.close();
        ps.close();
        return person;
    }

    @ReadMethodByColumnsAndValues
    public <E> @Nullable Person readUserByColumnsAndValues(Map<String, @WrappedClass E> columnsToValues) throws SQLException {
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

        rs.close();
        ps.close();
        return person;
    }

    // todo возможно заменить id на long
    @UpdateMethod
    public <E> boolean updateUser(int id, Map<String, @WrappedClass E> columnsToValues) throws SQLException {
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

    private boolean isConnectionEstablished() {
        if (connection != null)
            return true;
        else
            return false;
    }

    private @Nullable Person createPerson(ResultSet rs) throws SQLException {
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