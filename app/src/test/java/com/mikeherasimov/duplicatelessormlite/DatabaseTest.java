package com.mikeherasimov.duplicatelessormlite;

import android.content.Context;
import android.os.Build;

import com.mikeherasimov.duplicatelessormlite.transfer.Food;
import com.mikeherasimov.duplicatelessormlite.transfer.FoodType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.FileInputStream;
import java.io.FileOutputStream;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class DatabaseTest {

    @Test
    public void insertTest() throws Exception {
        Context context = RuntimeEnvironment.application;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        Food lemonade = new Food("lemonade", new FoodType("juices"));
        Food orangeJuice1 = new Food("orange juice", new FoodType("juices"));
        Food orangeJuice2 = new Food("orange juice", new FoodType("juices"));
        databaseHelper.getFoodDao().insertIfNotExists(lemonade);
        databaseHelper.getFoodDao().insertIfNotExists(orangeJuice1);
        databaseHelper.getFoodDao().insertIfNotExists(orangeJuice2);

        String dbPath = context.getDatabasePath(DatabaseHelper.DATABASE_NAME).toString();
        copyFile(dbPath, "testDatabase");
    }

    private void copyFile(String from, String to) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(from);
        FileOutputStream fileOutputStream = new FileOutputStream(to);
        byte[] buffer = new byte[1024];

        int length;
        while ((length = fileInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0 , length);
        }

        fileInputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
