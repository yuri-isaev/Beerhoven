package ru.mobile.beerhoven.configs;

import com.google.android.material.textfield.TextInputLayout;

public class Utils {

   private Utils() {}

   private static Utils sInstance = null;

   public static Utils getInstance() {
      if (sInstance == null) {
         sInstance = new Utils();
      }
      return sInstance;
   }

   public Boolean isValidNameField(TextInputLayout name) {
      String val = name.getEditText().getText().toString();

      if (val.isEmpty()) {
         name.setError("Поле не должно быть пустым");
         return false;
      } else {
         name.setError(null);
         name.setErrorEnabled(false);
         return true;
      }
   }

   public Boolean isValidateEmail(TextInputLayout email) {
      String val = email.getEditText().getText().toString();
      String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

      if (val.isEmpty()) {
         email.setError("Поле не должно быть пустым");
         return false;
      } else if (!val.matches(emailPattern)) {
         email.setError("Неверный адрес электронной почты");
         return false;
      } else {
         email.setError(null);
         email.setErrorEnabled(false);
         return true;
      }
   }

   public final boolean isValidPhoneNumber(TextInputLayout phone) {
      String val = phone.getEditText().getText().toString();

      if (val.isEmpty()) {
         phone.setError("Поле не должно быть пустым");
         return false;
      } else {
         if (val.length() < 16 || val.length() > 17) {
            return false;
         } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return android.util.Patterns.PHONE.matcher(val).matches();
         }
      }
   }
}
