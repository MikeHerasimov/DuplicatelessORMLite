package com.mikeherasimov.duplicatelessormlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.mikeherasimov.duplicatelessormlite.field.BaseFieldInfo;
import com.mikeherasimov.duplicatelessormlite.field.FieldInfo;


import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DuplicatelessDaoImpl<T,ID> extends BaseDaoImpl<T, ID> implements DuplicatelessDao<T, ID> {

    private List<FieldInfo> baseFields = new ArrayList<>();
    private Map<FieldInfo, DuplicatelessDao<? super Object, ? super Object>> foreignFieldsMap = new HashMap<>();

    public DuplicatelessDaoImpl(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        for (FieldType fieldType : tableInfo.getFieldTypes()) {
            if (fieldType != tableInfo.getIdField()) {
                if (fieldType.isForeign()){
                    handleForeignField(fieldType);
                } else {
                    baseFields.add(new BaseFieldInfo(fieldType));
                }
            }
        }
    }

    private void handleForeignField(FieldType fieldType) throws SQLException {
        Class<?> c = fieldType.getType();
        Dao<?, ?> dao = DaoManager.createDao(connectionSource, c);
        //noinspection unchecked
        foreignFieldsMap.put(new BaseFieldInfo(fieldType), (DuplicatelessDao<? super Object, ? super Object>) dao);
    }

    @Override
    public void insertIfNotExists(T data) throws SQLException {
        for (Map.Entry<FieldInfo, DuplicatelessDao<? super Object, ? super Object>> entry : foreignFieldsMap.entrySet()) {
            DuplicatelessDao<? super Object, ? super Object> duplicatelessDao =  entry.getValue();
            Object foreignData = entry.getKey().getValue(data);
            duplicatelessDao.insertIfNotExists(foreignData);
        }
        ID index = indexOf(data);
        if (index != null) {
            setIdTo(data, index);
        }
        createIfNotExists(data);
    }

    private void setIdTo(T data, ID value) {
        Field idField = tableInfo.getIdField().getField();
        idField.setAccessible(true);
        try {
            idField.set(data, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ID indexOf(T data) throws SQLException {
        Map<String, Object> foreignFieldIndexes = getForeignFieldIndexes(data);
        if (foreignFieldIndexes == null){
            return null;
        }
        Where<T, ID> where = buildWhere(foreignFieldIndexes, data);
        List<T> results = where.query();
        if (!results.isEmpty()) {
            return extractId(results.get(0));
        }
        return null;
    }

    private Map<String, Object> getForeignFieldIndexes(T data) throws SQLException {
        Map<String, Object> foreignFieldIndexes = new HashMap<>();
        for (Map.Entry<FieldInfo, DuplicatelessDao<? super Object, ? super Object>> entry : foreignFieldsMap.entrySet()) {
            String fieldName = entry.getKey().getColumnName();
            Object fieldData = entry.getKey().getValue(data);
            Object index = entry.getValue().indexOf(fieldData);
            if (index == null){
                return null;
            } else {
                foreignFieldIndexes.put(fieldName, index);
            }
        }
        return foreignFieldIndexes;
    }

    private Where<T, ID> buildWhere(Map<String, Object> foreignFieldIndexes, T data) throws SQLException{
        Where<T, ID> where = queryBuilder().where();
        boolean emptyBaseFieldsSection = baseFields.isEmpty();
        boolean emptyForeignFieldsSection = foreignFieldIndexes.isEmpty();
        if (emptyForeignFieldsSection && emptyBaseFieldsSection) {
            return where;
        }
        if (emptyForeignFieldsSection) {
            buildBaseFieldsWhere(where, data);
            return where;
        }
        if (emptyBaseFieldsSection) {
            buildForeignFieldsWhere(where, foreignFieldIndexes);
            return where;
        }
        buildBaseFieldsWhere(where, data);
        where.and();
        buildForeignFieldsWhere(where, foreignFieldIndexes);
        return where;
    }

    private void buildForeignFieldsWhere(Where<T, ID> where, Map<String, Object> foreignFieldIndexes) throws SQLException {
        int lastElementIndex = foreignFieldIndexes.size() - 1;
        int count = 0;
        for (Map.Entry<String, Object> entry : foreignFieldIndexes.entrySet())  {
            where.eq(entry.getKey(), entry.getValue());
            if (count++ != lastElementIndex) {
                where.and();
            }
        }
    }

    private void buildBaseFieldsWhere(Where<T, ID> where, T data) throws SQLException {
        int lastElementIndex = baseFields.size() - 1;
        int count = 0;
        for (FieldInfo fieldInfo : baseFields) {
            where.eq(fieldInfo.getColumnName(), fieldInfo.getValue(data));
            if (count++ != lastElementIndex) {
                where.and();
            }
        }
    }

}
