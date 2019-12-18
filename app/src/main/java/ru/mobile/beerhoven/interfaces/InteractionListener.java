package ru.mobile.beerhoven.interfaces;

import ru.mobile.beerhoven.models.Item;

public interface InteractionListener {
   void onInteractionAdd(Item model);
   void onInteractionDelete(Item model);
}
