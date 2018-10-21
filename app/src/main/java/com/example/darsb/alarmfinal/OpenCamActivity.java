package com.example.darsb.alarmfinal;

import android.content.Intent;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.IccOpenLogicalChannelResponse;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OpenCamActivity extends AppCompatActivity {

    Button btnWake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cam);
        btnWake = findViewById(R.id.buttonCam);
        initViews();
        getSupportActionBar().hide();
    }

    public void initViews() {
        Toast.makeText(this,"This alarm will only turns of when you are awake",Toast.LENGTH_LONG).show();
        btnWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenCamActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });
    }


}
