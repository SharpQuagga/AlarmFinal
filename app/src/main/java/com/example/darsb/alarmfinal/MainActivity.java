package com.example.darsb.alarmfinal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AlarmManager alarmManager;
    private TimePicker alarmTimePicker;
    private static MainActivity inst;
    private TextView alarmTextView;
    Button btnON , btnOff;
    Context context;
    private PendingIntent pending_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        alarmTimePicker =  findViewById(R.id.alarmTimePicker);
        alarmTextView =  findViewById(R.id.alarmText);
        btnOff = findViewById(R.id.turnoff);
        btnON = findViewById(R.id.turnon);
        btnON.setOnClickListener(this);
        btnOff.setOnClickListener(this);
        this.context=this;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    @Override
    public void onClick(View v) {
        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        int id = v.getId();
        Log.e("Maaa", "OnCLoC CJakad;; ");

        if (id == R.id.turnon){
            Calendar calendar = Calendar.getInstance();
            //setAlarmText("You clicked a button");

            final int hour = alarmTimePicker.getCurrentHour();
            final int minute = alarmTimePicker.getCurrentMinute();

            Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
            setAlarmText("You clicked a " + hour + " and " + minute);


            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            myIntent.putExtra("extra", "yes");
            pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

            // now you should change the set Alarm text so it says something nice
            setAlarmText("Alarm set to " + hour + ":" + minute);
        }


        if (id == R.id.turnoff){
            myIntent.putExtra("extra", "no");
            sendBroadcast(myIntent);

            alarmManager.cancel(pending_intent);
            setAlarmText("Alarm canceled");
            //setAlarmText("You clicked a " + " canceled");

        }

    }

    @Override
    protected void onDestroy() {
        Log.e("MainActivity","Activity Destroyed");
        super.onDestroy();
    }
}
