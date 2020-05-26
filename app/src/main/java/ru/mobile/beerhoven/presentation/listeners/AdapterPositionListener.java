package ru.mobile.beerhoven.presentation.listeners;

import ru.mobile.beerhoven.domain.model.Product;

public interface AdapterPositionListener {
   void onInteractionAdd(Product product);
   void onInteractionDelete(Product product);
}
