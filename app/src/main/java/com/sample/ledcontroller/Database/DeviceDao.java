package com.sample.ledcontroller.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM Device")
    List<Device> getAll();

    @Insert
    void insertAll(Device... users);

    @Delete
    void delete(Device user);
}
