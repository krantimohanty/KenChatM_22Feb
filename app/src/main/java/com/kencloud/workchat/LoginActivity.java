package com.kencloud.workchat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kencloud.workchat.util.Utils;

public class LoginActivity extends AppCompatActivity {

    private EditText mEtMobNumber;
    private Button btnSubmit;
    private static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 12;
    private String mUserMobileNumber = "";
    private String mUserDeviceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtMobNumber=(EditText)findViewById(R.id.editPhNo);
        mEtMobNumber.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab300.ttf"));
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/RobotoSlab300.ttf"));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserMobileNumber = mEtMobNumber.getText().toString().trim();
                if(mUserMobileNumber == null || mUserMobileNumber.trim().length() <= 0){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setTitle("Mobile Number");
                    alertDialog.setMessage("Must provide your mobile number.");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.create().show();
                    return;
                }

                int permissionCheckForReadPhoneState = ContextCompat.checkSelfPermission(LoginActivity.this,
                        android.Manifest.permission.READ_PHONE_STATE);

                // READ_PHONE_STATE
                if(permissionCheckForReadPhoneState !=  PackageManager.PERMISSION_GRANTED
                        || permissionCheckForReadPhoneState !=  PackageManager.PERMISSION_GRANTED){
                    // Ask Permission for READ_PHONE_STATE
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{android.Manifest.permission.READ_PHONE_STATE},
                            REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
                } else {
                    // Read Contacts Granted
                    mUserDeviceId = getDeviceIMEINumberAndSaveAsUserName();
                }
            }
        });
    }

    private String getDeviceIMEINumberAndSaveAsUserName(){
        return Utils.getDeviceIMEINumber(LoginActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    mUserDeviceId = getDeviceIMEINumberAndSaveAsUserName();
                } else {
                    // permission denied
                }
                return;
            }
        }
    }
}
