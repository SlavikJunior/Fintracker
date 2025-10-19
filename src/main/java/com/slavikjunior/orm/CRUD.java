package com.slavikjunior.orm;

import com.slavikjunior.annotations.Entity;
import kotlin.Pair;

import java.util.List;

public interface CRUD {

    <T extends CRUDable> void create(T entity, List<Object> properties);
    <T extends CRUDable> void read(T entity);
    <T extends CRUDable> void update(T entity);
    <T extends CRUDable> void delete(T entity);
}
