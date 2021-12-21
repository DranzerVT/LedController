package com.sample.ledcontroller.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Device {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "ip_address")
    public String ipAdress;

    @ColumnInfo(name = "port")
    public int port;

    public Device(String ipAdress, int port) {
        this.ipAdress = ipAdress;
        this.port = port;
    }
}
