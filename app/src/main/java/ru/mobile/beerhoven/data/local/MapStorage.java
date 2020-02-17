package ru.mobile.beerhoven.data.local;

import android.net.Uri;

import java.util.HashMap;

public final class MapStorage {
   public static HashMap<String, String> productMap = new HashMap<>();
   public static HashMap<String, Double> priceMap = new HashMap<>();
   public static HashMap<String, Uri> uriMap = new HashMap<>();
}
