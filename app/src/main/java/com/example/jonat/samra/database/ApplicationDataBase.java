package com.example.jonat.samra.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.jonat.samra.database.dao.UserInfoDao;
import com.example.jonat.samra.database.pojo.UserInfo;

/**
 * This class implements the RoomDatabase
 * Room library acts as a client/wrapper on top of the SQLLITE database
 */

@Database(entities = {UserInfo.class}, version = 1)
public abstract class ApplicationDataBase extends RoomDatabase {

    private static ApplicationDataBase INSTANCE;

    public abstract UserInfoDao userInfoDao();

    public static ApplicationDataBase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), ApplicationDataBase.class, "userinfo-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
