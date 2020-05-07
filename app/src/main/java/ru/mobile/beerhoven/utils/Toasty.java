package ru.mobile.beerhoven.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import ru.mobile.beerhoven.R;

public final class Toasty {

   @SuppressLint("ResourceType")
   public static void success(Activity ctx, String message) {
      Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
      View view = toast.getView();
      view.getBackground()
          .setColorFilter(ContextCompat.getColor(ctx, R.color.colorGreen), PorterDuff.Mode.SRC_OVER);
      toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 90);
      toast.show();
   }

   @SuppressLint("ResourceType")
   public static void error(Activity ctx, String message) {
      Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
      View view = toast.getView();
      view.getBackground()
          .setColorFilter(ContextCompat.getColor(ctx, R.color.colorRed), PorterDuff.Mode.SRC_OVER);
      toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 90);
      toast.show();
   }
}
