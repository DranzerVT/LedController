package com.sample.ledcontroller.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.ledcontroller.CommunicationActivity;
import com.sample.ledcontroller.Database.AppDatabase;
import com.sample.ledcontroller.Database.Device;
import com.sample.ledcontroller.Database.DeviceDao;
import com.sample.ledcontroller.Interfaces.DeviceDeleter;
import com.sample.ledcontroller.MainActivity;
import com.sample.ledcontroller.R;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> implements DeviceDeleter {

    private static final String TAG = "DeviceListAdapter";
    private List<Device> deviceList;
    DeviceDao deviceDao;
    Context context;

    public DeviceListAdapter(List<Device> deviceList, DeviceDao deviceDao) {
        this.deviceList = deviceList;
        this.deviceDao = deviceDao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.device_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        holder.txtDevice.setText(String.format("%s:%s", deviceList.get(pos).ipAdress, deviceList.get(pos).port));
        holder.txtDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommunicationActivity.class);
                intent.putExtra("IPADRESS",deviceList.get(pos).ipAdress);
                intent.putExtra("PORT",deviceList.get(pos).port);
                context.startActivity(intent);
            }
        });
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    deviceDao.delete(deviceList.get(pos));
                    deviceList.remove(pos);
                    onDeviceDeleted(pos);
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    @Override
    public void onDeviceDeleted(int pos) {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(pos);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDevice;
        private final ImageView imgRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDevice = itemView.findViewById(R.id.device_item);
            imgRemove = itemView.findViewById(R.id.img_remove_device);
        }
    }
}
