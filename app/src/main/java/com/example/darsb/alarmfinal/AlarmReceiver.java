package com.example.darsb.alarmfinal;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.Utils;


public class AlarmReceiver extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(final Context context, final Intent intent) {
        //this will update the UI with message
        MainActivity inst = MainActivity.instance();
        inst.setAlarmText("Alarm! Wake up! Wake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

//        final Window win = getWindow();
//        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
//                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);


        String state = intent.getExtras().getString("extra");
        Log.e("MyActivity", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,AlarmService.class);
        serviceIntent.putExtra("extra", state);

        context.startService(serviceIntent);

//        Intent intent12 = new Intent(context,CameraActivity.class);
//         No = (intent12.getStringExtra("No"));

//        final String No = intent.getStringExtra("No");


//        int secs = 4; // Delay in seconds

//        Log.e("Nooo", No);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                  int No = Integer.parseInt(intent.getStringExtra("No"));
                Log.e("Noo", "no"+ No);
                    if (No == 1){
                        ringtone.stop();
                        Toast.makeText(context,"Alarm Stopped",Toast.LENGTH_LONG).show();

                    }else {
                        if (ringtone.isPlaying()==false){
                            ringtone.play();
                        }
                    }
            }
        }, 3000);
    }
}