package ru.mobile.beerhoven.presentation.interfaces;

import ru.mobile.beerhoven.domain.model.Product;

public interface IAdapterPositionListener {
   void onInteractionAdd(Product product);
   void onInteractionDelete(Product product);
}
