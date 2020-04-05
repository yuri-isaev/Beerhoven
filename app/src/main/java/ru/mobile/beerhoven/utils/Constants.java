package ru.mobile.beerhoven.utils;

public final class Constants {
    // Shared preference
    public static final String SHARED_PREF = "SHARED_PREF";
    public static final String COUNTER_VALUE = "COUNTER_VALUE";
    public static final String CURRENT_USER_NAME = "CURRENT_USER_NAME";

    // Database state node
    public static final String ADMIN_CHANNEL_ID = "admin_channel";
    public static final String NODE_ADMIN = "ADMIN";
    public static final String NODE_CART = "CART LIST";
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

    // Display delay
    public static final long SPLASH_DISPLAY_LENGTH = 3000;

    // Geo
    public static final String DRIVER_INFO_REFERENCES = "DriverInfo";
    public static final String DRIVER_LOCATION_REFERENCES = "DriverLocation";
}
