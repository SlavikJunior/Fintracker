//package com.slavikjunior.servises
//
//import com.fasterxml.jackson.core.type.TypeReference
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.google.common.collect.ImmutableList
//import com.google.common.io.Resources
//import com.slavikjunior.models.Person
//import java.sql.Connection
//
//fun getPersons(): ImmutableList<Person> {
//    val objectMapper = ObjectMapper()
//    val inputStream = Resources.getResource("persons.json").openStream()
//    val persons = objectMapper.readValue(inputStream, object : TypeReference<List<Person>>() {})
//
//    return ImmutableList.copyOf(persons)
//}
//
//fun insertPerson(connection: Connection, tableName: String, person: Person) {
//    val sql = """
//        INSERT INTO $tableName (
//            firstName,
//            lastName,
//            email,
//            gender,
//            ipAddress,
//            country
//        )
//        VALUES (?, ?, ?, ?, ?, ?);
//    """.trimIndent()
//
//    connection.prepareStatement(sql).use { statement ->
//        statement.setString(1, person.firstName)
//        statement.setString(2, person.lastName)
//        statement.setString(3, person.email)
//        statement.setString(4, person.gender)
//        statement.setString(5, person.ipAddress)
//        statement.setString(6, person.country)
//        statement.executeUpdate()
//    }
//}
//
//fun createTable(connection: Connection, tableName: String) {
//    //todo подстановку заменить на ? для избежания инъекции
//    val sql = """
//                CREATE TABLE IF NOT EXISTS $tableName (
//                    id SERIAL PRIMARY KEY,
//                    firstName VARCHAR(255),
//                    lastName VARCHAR(255),
//                    email VARCHAR(255),
//                    gender VARCHAR(255),
//                    ipAddress VARCHAR(255),
//                    country VARCHAR(255)
//                );
//                """.trimIndent()
//    connection.prepareStatement(sql).execute()
//}