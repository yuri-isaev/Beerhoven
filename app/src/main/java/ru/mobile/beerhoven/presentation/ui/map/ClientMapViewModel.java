package ru.mobile.beerhoven.presentation.ui.map;

import android.app.Activity;
import android.content.Context;
import android.location.Geocoder;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;

import ru.mobile.beerhoven.domain.usecases.GetDriverLocationUseCase;
import ru.mobile.beerhoven.domain.usecases.GetOrderLocationUseCase;

public class ClientMapViewModel extends ViewModel {
   private final GetDriverLocationUseCase mGetDriverLocationUseCase;
   private final GetOrderLocationUseCase mGetOrderLocationUseCase;

   public ClientMapViewModel(Activity activity, Context context, GoogleMap map) {
      this.mGetDriverLocationUseCase = new GetDriverLocationUseCase(activity, context, map);
      this.mGetOrderLocationUseCase = new GetOrderLocationUseCase(context, map);
   }

   public void onGetDriverLocationToUseCase() {
      mGetDriverLocationUseCase.invoke();
   }

   public void onGetOrderLocationToUseCase(FusedLocationProviderClient providerClient, Geocoder geocoder) {
      mGetOrderLocationUseCase.invoke(providerClient, geocoder);
   }
}
