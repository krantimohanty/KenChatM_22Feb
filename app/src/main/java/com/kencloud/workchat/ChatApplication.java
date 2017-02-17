package com.kencloud.workchat;

import com.firebase.client.Firebase;

/**
 * Created by suchismita.p on 8/9/2016.
 *
 * Initialize Firebase with the application context. This must happen before the client is used.
 */
public class ChatApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
