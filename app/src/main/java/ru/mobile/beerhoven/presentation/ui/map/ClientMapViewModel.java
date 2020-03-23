package ru.mobile.beerhoven.presentation.ui.map;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;

import ru.mobile.beerhoven.domain.usecases.GetDriverLocationUseCase;

public class ClientMapViewModel extends ViewModel {
   private final GetDriverLocationUseCase mUseCase;

   public ClientMapViewModel(Activity activity, Context context, GoogleMap map) {
      this.mUseCase = new GetDriverLocationUseCase(activity, context, map);
   }

   public void getDriverLocationToUseCase() {
      mUseCase.execute();
   }
}
