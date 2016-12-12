package com.github.mikeherasimov.sample.transfer;

import com.github.mikeherasimov.duplicatelessormlite.DuplicatelessDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = FoodType.TABLE_NAME, daoClass = DuplicatelessDaoImpl.class)
public class FoodType {

    public static final String TABLE_NAME = "food_types";

    @DatabaseField(columnName = CommonColumnNames.COLUMN_ID, generatedId = true)
    private int id;
    @DatabaseField(columnName = CommonColumnNames.COLUMN_NAME)
    private String name;

    FoodType() {
    }

    public FoodType(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
