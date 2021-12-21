package com.sample.ledcontroller.Interfaces;

import com.sample.ledcontroller.Database.Device;

import java.util.List;

public interface DeviceLoader {

    public void onDeviceLoaded(List<Device> devices);
}
