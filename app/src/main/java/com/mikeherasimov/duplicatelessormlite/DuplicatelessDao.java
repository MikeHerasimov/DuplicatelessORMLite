package com.mikeherasimov.duplicatelessormlite;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public interface DuplicatelessDao<T, ID> extends Dao<T, ID> {

    void insertIfNotExists(T data) throws SQLException;
    ID indexOf(T data) throws SQLException;

}
