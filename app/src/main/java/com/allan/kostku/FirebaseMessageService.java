package com.allan.kostku;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.allan.kostku.ActivityUser.MainActivity;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessageService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageService";
    public FirebaseMessageService(){

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.d(TAG, "onMessageReceived: Message Received: \n" +
                "Title: "+title+"\n"+
                "Message: "+message);
        sendNotification(title, message);
    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendNotification(String title, String messageBody){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_account)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }

}
