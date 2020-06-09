package ru.mobile.beerhoven.presentation.ui.user.map;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IMapRepository;
import ru.mobile.beerhoven.domain.usecases.GetDriverLocationUseCase;

public class CustomerMapViewModel extends ViewModel {
   private final IMapRepository iRepository;
   private final GetDriverLocationUseCase mGetDriverLocationUseCase;

   public CustomerMapViewModel(Activity activity, Context ctx, GoogleMap map, IMapRepository repo) {
      this.mGetDriverLocationUseCase = new GetDriverLocationUseCase(activity, ctx, map, repo);
      this.iRepository = repo;
   }

   public LiveData<List<Order>> getOrderListFromRepository() {
      return iRepository.getOrderLocationListFromDatabase();
   }

   public void onGetDriverLocationToUseCase() {
      mGetDriverLocationUseCase.execute();
   }
}
