package com.sample.ledcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sample.ledcontroller.Adapters.DeviceListAdapter;
import com.sample.ledcontroller.Database.AppDatabase;
import com.sample.ledcontroller.Database.Device;
import com.sample.ledcontroller.Database.DeviceDao;
import com.sample.ledcontroller.Interfaces.DeviceLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DeviceLoader {

    private static final String TAG = "MainActivity";
    Dialog deviceDialog;
    AppDatabase deviceDB;
    DeviceDao deviceDao;
    RecyclerView deviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceList = findViewById(R.id.device_list);
        deviceDB = AppDatabase.getDatabase(getApplicationContext());
        deviceDao = deviceDB.deviceDao();
        setupDeviceList();
    }

    public void setupDeviceList(){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Device> devices = deviceDao.getAll();
            onDeviceLoaded(devices);
        });
    }

    public void showNewDeviceDialog(View view){
        deviceDialog = new Dialog(this);
        deviceDialog.setContentView(R.layout.new_device_dialog);
        deviceDialog.show();
    }

    public void addDeviceToDB(View view){
        if(deviceDialog == null){
            return;
        }
        EditText edtIpAdress = deviceDialog.findViewById(R.id.edt_ipaddress);
        EditText edtPort = deviceDialog.findViewById(R.id.edt_port);

        String ipAdress = edtIpAdress.getText().toString().trim();
        String port = edtPort.getText().toString().trim();
        boolean isValid = true;
        if(ipAdress.equals("")){
            edtIpAdress.setError("Enter Valid IP");
            isValid = false;
        }
        if(port.equals("")){
            edtPort.setError("Enter Valid Port");
            isValid = false;
        }
        if(!isValid)
            return;

        int portnum = Integer.parseInt(port);
        Device device = new Device(ipAdress,portnum);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            deviceDao.insertAll(device);
        });
        deviceDialog.dismiss();
        setupDeviceList();
        Toast.makeText(MainActivity.this, "Device Details added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceLoaded(List<Device> devices) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DeviceListAdapter deviceListAdapter = new DeviceListAdapter(devices,deviceDao);
                deviceList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                deviceList.setAdapter(deviceListAdapter);
            }
        });
    }
}