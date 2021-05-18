package com.cbm.cbmapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    private NotificationCompat.Builder builder;

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //추가한것
        //sendNotification(remoteMessage.getData().get("message"));
        sendNotification(
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("message")
        );

    }

    // 채널 만들기
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //SDK_INT 버전에서 조건에 의해 차단
            CharSequence name = "channel_name"; //채널이름
            String description = "channel_description"; //채널설명
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel_Id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String title, String messageBody) {

        // 다음 스니펫은 사용자가 알림을 탭하면 활동을 여는 기본 인텐트를 만드는 방법을 보여줍니다.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0); //PendingIntent->앱이 꺼져 있오도 원격으로 킬 수가 있는 거

        Log.d("fcm",title+"/"+messageBody);
        // 알림의 콘텐츠와 채널 설정
        builder = new NotificationCompat.Builder(this, "Channel_Id")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("저혈당 발생!")
                .setAutoCancel(true)
                //BigTextStyle
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(title)
                        .bigText(messageBody))
                .setContentIntent(pendingIntent);


        // 알림 진짜 띄우게 하는거 (알림 표시)
        createNotificationChannel();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(0, builder.build()); // 0 줌

    }
}
