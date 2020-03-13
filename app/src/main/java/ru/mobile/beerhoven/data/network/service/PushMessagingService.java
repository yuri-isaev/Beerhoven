package ru.mobile.beerhoven.data.network.service;

import static android.graphics.BitmapFactory.*;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.Randomizer;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class PushMessagingService extends FirebaseMessagingService {
   private static final String TAG = "";

   public void onGetToken() {
      FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
         if (!task.isSuccessful()) {
            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
            return;
         }
         // Get new FCM registration token
         String token = task.getResult();
         // Log
         Log.w(TAG, token, task.getException());
      });
   }

   @Override
   public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
      super.onMessageReceived(remoteMessage);

      Intent intent = new Intent(this, MainActivity.class);
      NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      int notificationID = Randomizer.getRandomInt();

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         setupChannels(notificationManager);
      }

      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      @SuppressLint("UnspecifiedImmutableFlag")
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
      Bitmap bitmap = decodeResource(getResources(), R.color.colorMainYellow);
      String title = requireNonNull(remoteMessage.getNotification()).getTitle();
      String body = remoteMessage.getNotification().getBody();

      Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.ADMIN_CHANNEL_ID)
          .setContentTitle(title)
          .setContentText(body)
          .setSmallIcon(R.drawable.logo)
          .setLargeIcon(bitmap).setAutoCancel(true)
          .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
          .setOnlyAlertOnce(true)
          .setSound(notificationSoundUri)
          .setContentIntent(pendingIntent);
      notificationManager.notify(notificationID, notificationBuilder.build());

       onGetToken();
   }


   @RequiresApi(api = Build.VERSION_CODES.O)
   public void setupChannels(NotificationManager notificationManager) {
      CharSequence adminChannelName = "New notification";
      String adminChannelDescription = "Device to device notification";
      NotificationChannel notification = new NotificationChannel(Constants.ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);

      notification.setDescription(adminChannelDescription);
      notification.enableVibration(true);
      notification.enableLights(true);
      notification.setLightColor(Color.BLACK);
      notificationManager.createNotificationChannel(notification);
   }
}
