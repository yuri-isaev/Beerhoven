package ru.mobile.beerhoven.data.network;

import static android.graphics.BitmapFactory.decodeResource;
import static androidx.core.app.NotificationCompat.Builder;
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
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.domain.repository.IPushMessagingService;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.Randomizer;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class PushMessagingService extends FirebaseMessagingService implements IPushMessagingService {
   private static final String TAG = "PushMessagingService";

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

   @SuppressLint("NewApi")
   @Override
   public void onMessageReceived(@NonNull RemoteMessage message) {
      super.onMessageReceived(message);
      Intent intent = new Intent(this, MainActivity.class);
      NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      int notificationID = Randomizer.getRandomInt();
      onSetupChannels(manager);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      @SuppressLint("UnspecifiedImmutableFlag")
      PendingIntent pdi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
      Bitmap bitmap = decodeResource(getResources(), R.color.colorMainYellow);
      String title = requireNonNull(message.getNotification()).getTitle();
      String body = message.getNotification().getBody();

      Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      Builder notificationBuilder = new Builder(this, Constants.ADMIN_CHANNEL_ID)
          .setContentTitle(title)
          .setContentText(body)
          .setSmallIcon(R.drawable.ic_logo)
          .setLargeIcon(bitmap)
          .setAutoCancel(true)
          .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
          .setOnlyAlertOnce(true)
          .setSound(notificationSound)
          .setContentIntent(pdi);
      manager.notify(notificationID, notificationBuilder.build());
      onGetToken();
   }

   @RequiresApi(api = Build.VERSION_CODES.O)
   public void onSetupChannels(@NonNull NotificationManager notificationManager) {
      CharSequence adminChannelName = "New notification";
      String adminChannelDescription = "Device to device notification";
      NotificationChannel channel = new NotificationChannel(Constants.ADMIN_CHANNEL_ID,
          adminChannelName, NotificationManager.IMPORTANCE_HIGH);

      channel.setDescription(adminChannelDescription);
      channel.enableVibration(true);
      channel.enableLights(true);
      channel.setLightColor(Color.BLACK);
      notificationManager.createNotificationChannel(channel);
   }

   @Override
   public void onSendPushNotification(FragmentActivity activity) {
      RequestQueue requestQue = Volley.newRequestQueue(activity);
      FirebaseMessaging.getInstance().subscribeToTopic("news");
      JSONObject json = new JSONObject();

      try {
         json.put("to", "/topics/" + "news");

         JSONObject obj = new JSONObject();
         obj.put("title", "Получен новый заказ");
         obj.put("body", "от Имени");

         JSONObject extra = new JSONObject();
         extra.put("brandId", "brand");
         extra.put("category", "logo");
         json.put("notification", obj);
         json.put("data", extra);

         JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
             Constants.GOOGLE_API,
             json,
             response -> Log.d("MUR", "onResponse: "),
             error -> Log.d("MUR", "onError: " + error.networkResponse)) {

            @NonNull
            public Map<String, String> getHeaders() {
               Map<String, String> header = new HashMap<>();
               header.put("content-type", "application/json");
               header.put("authorization", getString(R.string.server_key));
               return header;
            }
         };

         requestQue.add(request);

      } catch (JSONException e) {
         e.printStackTrace();
      }
   }
}
