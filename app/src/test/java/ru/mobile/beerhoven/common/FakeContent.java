package ru.mobile.beerhoven.common;

import java.util.UUID;

import ru.mobile.beerhoven.models.Item;

public final class FakeContent {

   public static final Item fakePost = new Item(UUID.randomUUID().toString(),
       "test", "country", "manufacture", "style", "fortress",
       "density", "description", "data", "time", "url",
       "quantity", "price", "total", 50.0, 50.0, 50.0, 50.0f);
}