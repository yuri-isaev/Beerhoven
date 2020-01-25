package ru.mobile.beerhoven.interfaces;

import ru.mobile.beerhoven.domain.model.Product;

public interface InteractionListener {
   void onInteractionAdd(Product model);
   void onInteractionDelete(Product model);
}
