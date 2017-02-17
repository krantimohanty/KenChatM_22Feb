package com.kencloud.workchat;

import android.Manifest;

/**
 * Created by madhur on 3/1/15.
 */
public class Constants {

    public static final String TAG="chatbubbles";
    public static final int CONTACT_PICKER_RESULT = 9010;

    //For file attachment file type<Audio, Video, Image, Doc, Map>
    public static final int MESSAGE_TYPE_AUDIO = 11;
    public static final int MESSAGE_TYPE_VIDEO = 12;
    public static final int MESSAGE_TYPE_IMAGE = 13;
    public static final int MESSAGE_TYPE_DOC = 14;
    public static final int MESSAGE_TYPE_MAP = 15;
    public static final int MESSAGE_TYPE_TEXT = 16;

    public static final String APP_IMAGE_STORAGE_DIRECTORY_NAME = "KenChat";

    public static final String CONTACT_USER_NAME = "USER_NAME";
    public static final String CONTACT_USER_PHONE_NUMBER = "USER_PHONE_NUMBER";

    public static final String PERMISSION_READ_CONTACTS = android.Manifest.permission.READ_CONTACTS;
    public static final String PERMISSION_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;

}
