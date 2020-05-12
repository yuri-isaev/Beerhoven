package ru.mobile.beerhoven.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import ru.mobile.beerhoven.R;

public final class Toasty {

   public static void success(Context ctx, int resId) {
      Toast toast = Toast.makeText(ctx, resId, Toast.LENGTH_SHORT);
      View view = toast.getView();
      view.getBackground().setColorFilter(
          ContextCompat.getColor(ctx, R.color.colorGreen),
          PorterDuff.Mode.SRC_OVER);
      toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
      toast.show();
   }

   public static void warning(Context ctx, int resId) {
      Toast toast = Toast.makeText(ctx, resId, Toast.LENGTH_SHORT);
      View view = toast.getView();
      view.getBackground().setColorFilter(
          ContextCompat.getColor(ctx, R.color.colorOrange),
          PorterDuff.Mode.SRC_OVER);
      toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
      toast.show();
   }

   public static void error(Context ctx, int resId) {
      Toast toast = Toast.makeText(ctx, resId, Toast.LENGTH_SHORT);
      View view = toast.getView();
      view.getBackground().setColorFilter(
          ContextCompat.getColor(ctx, R.color.colorRed),
          PorterDuff.Mode.SRC_OVER);
      toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
      toast.show();
   }
}
