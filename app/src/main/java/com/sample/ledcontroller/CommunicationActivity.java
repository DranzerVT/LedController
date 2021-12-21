package com.sample.ledcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.sample.ledcontroller.Adapters.DataListAdapter;
import com.sample.ledcontroller.Utils.TcpClient;

import java.util.ArrayList;
import java.util.List;

public class CommunicationActivity extends AppCompatActivity {

    RecyclerView dataList;
    List<String> textList = new ArrayList<>();
    DataListAdapter dataListAdapter;
    String ipAdress;
    int port;
    TcpClient mTcpClient;
    boolean connectionStatus = false;
    ImageButton btnSend;
    EditText edtData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        dataList = findViewById(R.id.data_list);
        edtData = findViewById(R.id.edt_data);
        btnSend = findViewById(R.id.img_send);
        ipAdress = getIntent().getExtras().getString("IPADRESS");
        port = getIntent().getExtras().getInt("PORT");
        setupDataTextList();
    }

    public void setupDataTextList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        dataList.setLayoutManager(linearLayoutManager);
        dataListAdapter = new DataListAdapter(textList);
        dataList.setAdapter(dataListAdapter);

        textList.add("Connecting to " + ipAdress + ":" + port +" ..." );
        dataListAdapter.notifyItemInserted(textList.size()-1);
        new ConnectTask().execute();
    }

    public void updateList(String data){
        textList.add(data);
        dataListAdapter.notifyItemInserted(textList.size()-1);
    }

    public void sendData(View view) {
        if(!connectionStatus) {
            Toast.makeText(CommunicationActivity.this, "Device not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        String data = edtData.getText().toString().trim();
        if(data.equals("")){
            Toast.makeText(CommunicationActivity.this, "Enter Valid data", Toast.LENGTH_SHORT).show();
            return;
        }

        mTcpClient.sendMessage(data);

    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message,boolean state) {
                    //this method calls the onProgressUpdate
                    connectionStatus = state;
                    publishProgress(message);
                }
            }, ipAdress, port);
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
            updateList(values[0]);

        }
    }
}