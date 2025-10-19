package com.slavikjunior.dao;

import com.slavikjunior.annotations.*;
import com.slavikjunior.models.Person;
import com.slavikjunior.orm.Wrapper;
import com.slavikjunior.secrets.Keys;
import kotlin.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.slavikjunior.db_manager.DbConnectionManagerKt.getConnection;

@Database(name = Keys.databaseName)
@Table(name = Keys.tableName)
public class PersonDao {

    private Connection connection = getConnection(Keys.databaseName, Keys.user, Keys.password);

    @CreateMethod
    public void createUser(String firstName, String lastName, String email, String gender, String ipAddress, String country) throws SQLException {
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
        ps.executeUpdate();
    }

    @ReadMethod
    public Person readUserById(int id) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

        var ps = connection.prepareStatement(
                """
                        select * from persons where id = ?
                        """
        );
        ps.setInt(1, id);

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

    @ReadMethodByColumnAndValue
    public Person readUserByColumnAndValue(Wrapper wrapper) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

        if (wrapper == null)
            return null;

        String columnName = wrapper.getColumnName();

        String stringValue = null;
        Integer intValue = null;
        if (wrapper.isContainsStringValue())
            stringValue = wrapper.getStringValue();
        else
            intValue = wrapper.getIntValue();


        var ps = connection.prepareStatement(
                """
                        select * from %s where %s = ?
                        """.formatted(Keys.tableName, columnName)
        );

        if (wrapper.isContainsStringValue())
            ps.setString(1, stringValue);
        else
            ps.setInt(1, intValue);

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

    private boolean isConnectionEstablished() {
        if (connection != null)
            return true;
        else
            return false;
    }
}
