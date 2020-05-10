package ru.mobile.beerhoven.utils;

public final class Constants {
   // Shared preference keys
   public static final String SHARED_PREF = "shared_pref";
   public static final String KEY_CART_COUNT = "cart_counter_value";
   public static final String KEY_NEWS_COUNT = "news_counter_value";
   public static final String KEY_USER_NAME = "current_user_name";
   public static final String KEY_NOTIFICATION_COUNT = "notification_counter_value";

   // Database state node
   public static final String ADMIN_CHANNEL_ID = "admin_channel";
   public static final String NODE_ADMIN = "ADMIN";
   public static final String NODE_CART = "CART";
   public static final String NODE_CONFIRMS = "CONFIRMS";
   public static final String NODE_NEWS = "NEWS";
   public static final String NODE_ORDERS = "ORDERS";
   public static final String NODE_PRODUCTS = "PRODUCTS";
   public static final String NODE_USERS = "USERS";

   // Database images storage
   public static final String FOLDER_NEWS_IMG = "NEWS_IMG";
   public static final String FOLDER_PRODUCT_IMG = "ITEMS_IMG";

   // Data time
   public static final String CURRENT_DATA = "MMM dd, yyyy";
   public static final String CURRENT_TIME = "HH:mm:ss a";

   // Change state objects
   public static final String OBJECT_VISIBLE = "Visible";
   public static final String OBJECT_INVISIBLE = "Invisible";
   public static final String OBJECT_RENAME = "Rename";

   // URI address
   public static final String GOOGLE_API = "https://fcm.googleapis.com/fcm/send";
   public static final String APP_LOGO = "gs://beerhoven-app.appspot.com/NEWS_IMG/logo.png";
   public static final String URI_LOGO = "https://firebasestorage.googleapis.com/v0/b/beerhoven-app.appspot.com/o/NEWS_IMG%2FSat%20Jan%2028%2022%3A36%3A21%20GMT%2B02%3A00%202023?alt=media&token=394b75f7-7dac-4b80-95d5-ab2a53516987";

   // Geo
   public static final String DRIVER_INFO_REFERENCES = "DriverInfo";
   public static final String DRIVER_LOCATION_REFERENCES = "DriverLocation";

   // Permissions constants
   public static final int CAMERA_REQUEST_CODE = 100;
   public static final int STORAGE_REQUEST_CODE = 200;

   // Image pick constants
   public static final int IMAGE_PICK_CAMERA_CODE = 300;
   public static final int IMAGE_PICK_GALLERY_CODE = 400;
}
