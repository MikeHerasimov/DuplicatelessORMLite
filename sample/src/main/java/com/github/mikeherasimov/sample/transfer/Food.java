package com.github.mikeherasimov.sample.transfer;

import com.github.mikeherasimov.duplicatelessormlite.DuplicatelessDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = Food.TABLE_NAME, daoClass = DuplicatelessDaoImpl.class)
public class Food {

    public static final String TABLE_NAME = "foods";
    public static final String COLUMN_FOOD_TYPE = "food_type";

    @DatabaseField(columnName = CommonColumnNames.COLUMN_ID, generatedId = true)
    private int id;
    @DatabaseField(columnName = CommonColumnNames.COLUMN_NAME)
    private String name;
    @DatabaseField(columnName = COLUMN_FOOD_TYPE, foreign = true, foreignAutoCreate = true)
    private FoodType foodType;

    Food() {
    }

    public Food(String name, FoodType foodType) {
        this.name = name;
        this.foodType = foodType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }
}
