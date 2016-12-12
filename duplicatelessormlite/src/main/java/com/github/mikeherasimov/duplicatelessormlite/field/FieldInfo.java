package com.github.mikeherasimov.duplicatelessormlite.field;


public interface FieldInfo {

    String getColumnName();
    Object getValue(Object dto);

}
