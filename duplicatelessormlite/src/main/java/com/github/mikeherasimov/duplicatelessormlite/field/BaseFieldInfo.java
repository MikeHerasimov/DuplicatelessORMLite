package com.github.mikeherasimov.duplicatelessormlite.field;


import com.j256.ormlite.field.FieldType;

import java.lang.reflect.Field;

public class BaseFieldInfo implements FieldInfo{

    private FieldType fieldType;

    public BaseFieldInfo(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public String getColumnName() {
        return fieldType.getColumnName();
    }

    @Override
    public Object getValue(Object dto) {
        Field field = fieldType.getField();
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(dto);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }
}
