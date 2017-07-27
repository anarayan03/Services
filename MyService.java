package com.example.hp.rvalue;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class MyService extends Service
{
    Thread thread;
    boolean ra=false;
    private int ran;
    private final IBinder binder = new MyLocalBinder();
    public MyService()
    {

    }
    public void getRandom()
    {
        ra=true;
        Runnable runnable =new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent("com.example.hp.rvalue");
                while (ra ) {
                    Random random = new Random();
                    ran = random.nextInt(100) + 1;
                    synchronized (this) {
                        try {
                            wait(5000);
                            in.putExtra("adi",String.valueOf(ran));
                            sendBroadcast(in);
                        } catch (InterruptedException e)
                        {
                            return ;
                        }
                    }
                }
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    public class MyLocalBinder extends Binder
    {
        MyService getService()
        {
            return MyService.this;
        }
    }

    @Override
    public void onDestroy()
    {
        ra=false;
        thread.interrupt();
    }
}
