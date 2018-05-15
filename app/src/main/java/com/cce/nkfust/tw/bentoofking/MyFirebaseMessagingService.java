package com.cce.nkfust.tw.bentoofking;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by John on 2018/5/15.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        remoteMessage.getData();
        Intent intent = new Intent();   //點擊啟動到MainActivity頁面
        intent.setClass(this, MainActivity.class);
        showNotification(this, remoteMessage, intent);
    }

    // 顯示通知
     //remoteMessage.getData()         //-  無論app在什麼狀態下皆會執行 MyFirebaseMessagingService(需對應Service設定的字串文字)
    // remoteMessage.getNotification() -  只會在app顯示時,執行app的接收訊息 MyFirebaseMessagingService
    private void showNotification(Context context, RemoteMessage remoteMessage, Intent intent) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        PendingIntent iPending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(remoteMessage.getData().get("title")) //需對應Service設定的字串文字
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentText(remoteMessage.getData().get("body")) //需對應Service設定的字串文字
                        .setContentIntent(iPending)
                        .setAutoCancel(true);

        Notification notification = mBuilder.build();
        manager.notify(1, notification);

    }
}
