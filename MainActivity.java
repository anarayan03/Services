package com.example.hp.rvalue;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    String message;
    MyService myService;
    boolean isBound=false;
    private static final String TAG="val";
    Button start,stop;
    TextView textView;
    BroadcastReceiver mReceiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            message = intent.getStringExtra("adi");
            textView.setText(message);
        }
    };
    public void onStartButton(View view)
    {
        myService.getRandom();
        // Intent intent = new Intent(this, MyService.class);
        //startService(intent);
    }
    public void onStopButton(View view)
    {
        //Intent i = new Intent(this, MyService.class);
        //stopService(i);
        myService.onDestroy();
        //isBound=false;
       // Log.i(TAG, "stop service");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this,MyService.class);
        bindService(i,serviceConnection, Context.BIND_AUTO_CREATE );
        start=(Button)findViewById(R.id.button);
        stop=(Button)findViewById(R.id.button2);
        textView=(TextView)findViewById(R.id.textView);
    }
    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service)
        {
            MyService.MyLocalBinder binder=(MyService.MyLocalBinder) service;
            myService=binder.getService();
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            isBound=false;
        }
    };

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(mReceiver,new IntentFilter("com.example.hp.rvalue"));
    }
}
