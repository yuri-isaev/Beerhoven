package ru.mobile.beerhoven.utils;

import static android.util.Patterns.*;
import static java.util.Objects.*;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public final class Validation {

   public static boolean isValidNameField(TextInputLayout field) {
      String name = requireNonNull(field.getEditText()).getText().toString();

      if (name.isEmpty()) {
         field.setError("Поле не должно быть пустым");
         return false;
      } else {
         field.setError(null);
         field.setErrorEnabled(false);
         return true;
      }
   }

   public static boolean isValidateEmail(TextInputLayout field) {
      String email = requireNonNull(field.getEditText()).getText().toString();
      String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

      if (email.isEmpty()) {
         field.setError("Поле не должно быть пустым");
         return false;
      } else if (!email.matches(emailPattern)) {
         field.setError("Неверный адрес электронной почты");
         return false;
      } else {
         field.setError(null);
         field.setErrorEnabled(false);
         return true;
      }
   }

   public static boolean isValidPhoneNumber(TextInputLayout field) {
      String number = requireNonNull(field.getEditText()).getText().toString();

      if (number.isEmpty()) {
         field.setError("Поле не должно быть пустым");
         return false;
      } else if (number.length() == 11) {
         return true;
      } else {
         field.setError(null);
         field.setErrorEnabled(false);
         return PHONE.matcher(number).matches();
      }
   }

   public static Boolean setValidateAddress(TextInputLayout field) {
      String address = Objects.requireNonNull(field.getEditText()).getText().toString();

      if (address.isEmpty()) {
         field.setError("Поле не должно быть пустым");
         return false;
      } else {
         field.setError(null);
         field.setErrorEnabled(false);
         return true;
      }
   }
}
