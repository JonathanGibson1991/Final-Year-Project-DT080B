package com.example.jonat.samra.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.jonat.samra.database.pojo.UserInfo;

import java.util.List;
/**
 * DAO stands for Data Access Object.
 * This class contains methods to access, insert and delete records held locally on the room/SQL-lite database
 * Look @ApplicationDataBase for @Room Implementation
 */
@Dao
public interface UserInfoDao {

    @Query("SELECT * FROM userinfo")
    List<UserInfo> getAll();

    @Query("SELECT * FROM userinfo WHERE uuid = :uuid LIMIT 1")
    UserInfo getByCardId(String uuid);

    @Insert
    void insertAll(UserInfo... userInfo);

    @Delete
    void delete(UserInfo... userInfo);
}
