package com.kencloud.workchat;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        //getting unique id for device
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        //displaying id in textview
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText(id);

    }
}
