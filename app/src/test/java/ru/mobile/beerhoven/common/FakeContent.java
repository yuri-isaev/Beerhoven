package ru.mobile.beerhoven.common;

import java.util.UUID;

import ru.mobile.beerhoven.domain.model.Product;

public final class FakeContent {

   public static final Product product = new Product(UUID.randomUUID().toString(),
       "", "", "", "", "", "", "", "", "", "", 50.0, 50.0
   );
}
