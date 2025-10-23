package com.slavikjunior.liba.orm;

import com.slavikjunior.liba.annotations.*;
import org.jetbrains.annotations.Nullable;
import java.sql.SQLException;
import java.util.Map;

public interface DaoOperations<T> {

    @CreateMethod
    <E> boolean createEntity(Map<String, @WrappedClass E> columnsToValues) throws SQLException;

    @ReadMethod
    <E> @Nullable T readEntityByValues(Map<String, @WrappedClass E> columnsToValues) throws SQLException;

    @UpdateMethod
    <E> boolean updateEntityByValues(int id, Map<String, @WrappedClass E> columnsToValues) throws SQLException;

    @DeleteMethod
    <E> boolean deleteEntityByValues(Map<String, @WrappedClass E> columnsToValues) throws SQLException;
}
