package com.kencloud.workchat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.microsoft.windowsazure.notifications.NotificationsHandler;

/**
 * Created by suchismita.p on 8/23/2016.
 */


    public class MyHandler extends NotificationsHandler {
        public static final int NOTIFICATION_ID = 1;
        private NotificationManager mNotificationManager;
        NotificationCompat.Builder builder;
        Context ctx;

        @Override
        public void onReceive(Context context, Bundle bundle) {
            ctx = context;
            String nhMessage = bundle.getString("message");
            sendNotification(nhMessage);
            if (MainActivity.isVisible) {
                MainActivity.mainActivity.ToastNotify(nhMessage);
            }
        }

        private void sendNotification(String msg) {

            Intent intent = new Intent(ctx, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            mNotificationManager = (NotificationManager)
                    ctx.getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(R.mipmap.kenchat_icon)
                            .setContentTitle("KenChat")
//                            .setDefaults(Notification.DEFAULT_ALL)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                            .setSound(defaultSoundUri)
                            .setContentText(msg);

//            // Big style
//            NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
//            inboxStyle.setBigContentTitle(extras.getString("title"));
//            inboxStyle.bigText(message);
//            mBuilder.setStyle(inboxStyle);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

