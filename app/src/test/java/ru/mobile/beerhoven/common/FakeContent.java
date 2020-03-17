package ru.mobile.beerhoven.common;

import java.util.UUID;

import ru.mobile.beerhoven.domain.model.Product;

public final class FakeContent {

   public static final Product fakePost = new Product(UUID.randomUUID().toString(),
       "test", "country", "manufacture", "style", "fortress",
       "density", "description", 50.0, 50.0, "url",
       "quantity", "price");
}
