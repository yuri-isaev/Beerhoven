package ru.mobile.beerhoven.domain.usecases;

import android.content.Context;
import android.location.Geocoder;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;

import ru.mobile.beerhoven.data.remote.MapRepository;
import ru.mobile.beerhoven.domain.repository.IMapRepository;

public class GetOrderLocationUseCase {
   private final IMapRepository mRepository;
   private final Context mContext;

   public GetOrderLocationUseCase(Context context, GoogleMap map) {
      this.mContext = context;
      this.mRepository = new MapRepository(map);
   }

   public void invoke(FusedLocationProviderClient providerClient, Geocoder geocoder) {
      mRepository.onGetOrderLocation(providerClient, mContext, geocoder);
   }
}
