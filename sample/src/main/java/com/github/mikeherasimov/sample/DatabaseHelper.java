package com.github.mikeherasimov.sample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikeherasimov.duplicatelessormlite.DuplicatelessDao;
import com.github.mikeherasimov.sample.transfer.Food;
import com.github.mikeherasimov.sample.transfer.FoodType;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    public static final String DATABASE_NAME = "testDB";

    private DuplicatelessDao<Food, Integer> foodDao;
    private DuplicatelessDao<FoodType, Integer> foodTypeDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, FoodType.class);
            TableUtils.createTable(connectionSource, Food.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public DuplicatelessDao<Food, Integer> getFoodDao() throws SQLException {
        if (foodDao == null) {
            foodDao = getDao(Food.class);
        }
        return foodDao;
    }

    public DuplicatelessDao<FoodType, Integer> getFoodTypeDao() throws SQLException {
        if (foodTypeDao == null) {
            foodTypeDao = getDao(FoodType.class);
        }
        return foodTypeDao;
    }


}
