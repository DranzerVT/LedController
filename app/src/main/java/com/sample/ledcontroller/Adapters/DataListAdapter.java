package com.sample.ledcontroller.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.sample.ledcontroller.MainActivity;
import com.sample.ledcontroller.R;

import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.ViewHolder> {
    private static final String TAG = "DeviceListAdapter";
    private List<String>textList;

    public DataListAdapter(List<String>textList) {
        this.textList = textList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from( parent.getContext())
                .inflate(R.layout.data_list_item_row, parent, false);
        return new DataListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtData.setText(textList.get(position));
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.data_text);
        }
    }
}
