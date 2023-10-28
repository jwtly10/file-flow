package com.jwtly10.databaseservice.dao;

import java.util.List;
import java.util.Optional;

public interface UserDAO<T> {

    List<T> list();

    void create(T t);

    Optional<T> get(String username);

    int update(T t, int id);

    int delete(int id);

}
