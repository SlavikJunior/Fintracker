package com.slavikjunior.dao;

import com.slavikjunior.annotations.CreateMethod;

import java.sql.Connection;
import java.sql.SQLException;

import static com.slavikjunior.db_manager.DbConnectionManagerKt.getConnection;

public class PersonDao {

    private final static String DATABASE_NAME = "testdb";
    private final static String PERSON_TABLE_NAME = "persons";
    private Connection connection = getConnection(DATABASE_NAME, "postgres", "7913");

    @CreateMethod
    public void createUser(String firstName, String lastName, String email, String gender, String ipAddress, String country) throws SQLException {
        if (!isConnectionEstablished())
            throw new SQLException();

        // todo убрать захадкоженное имя таблицы при вставке
        var ps = connection.prepareStatement(
                """
                        INSERT INTO  persons (
                            firstname, lastname, email, gender, ipaddress, country
                            ) VALUES (?, ?, ?, ?, ?, ?)
                        """
        );
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, email);
        ps.setString(4, gender);
        ps.setString(5, ipAddress);
        ps.setString(6, country);
        ps.executeUpdate();
    }

    private boolean isConnectionEstablished() {
        if (connection != null)
            return true;
        else
            return false;
    }
}
