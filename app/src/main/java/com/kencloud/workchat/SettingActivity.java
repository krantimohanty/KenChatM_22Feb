package com.kencloud.workchat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kencloud.workchat.app_widget.CircularImageView;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        CircularImageView img = (CircularImageView) findViewById(R.id.userPic);
        LinearLayout layout = (LinearLayout) findViewById(R.id.profileLayout);
        TextView userName = (TextView) findViewById(R.id.user_name);
        userName.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/roboto-slab.light.ttf"));
        TextView userStatus = (TextView) findViewById(R.id.user_status);
//        userStatus.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab100.ttf"));
        TextView account = (TextView) findViewById(R.id.account);
        account.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/roboto-slab.light.ttf"));
        TextView chat = (TextView) findViewById(R.id.gruopMessage);
        chat.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/roboto-slab.light.ttf"));
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, ProfileEditActivity.class));
            }
        });
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if ((keyCode == KeyEvent.KEYCODE_BACK))
//        {
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if ((keyCode == KeyEvent.KEYCODE_BACK))
//        {
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //
        //
        //
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


