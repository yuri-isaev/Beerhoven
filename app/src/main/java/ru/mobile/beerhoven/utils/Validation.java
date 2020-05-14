package ru.mobile.beerhoven.utils;

import static android.util.Patterns.PHONE;
import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public final class Validation {
   private static final String inValidField = "Поле не должно быть пустым";

   public static boolean isValidName(@NonNull TextInputLayout field) {
      String name = requireNonNull(field.getEditText()).getText().toString();

      if (!name.isEmpty()) {
         field.setError(null);
         field.setErrorEnabled(false);
         return true;
      } else {
         field.setError(inValidField);
         return false;
      }
   }

   public static boolean isValidEmail(@NonNull TextInputLayout field) {
      String email = requireNonNull(field.getEditText()).getText().toString();
      String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

      if (email.isEmpty()) {
         field.setError(inValidField);
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

   public static boolean isValidPhoneNumber(@NonNull TextInputLayout field) {
      String number = requireNonNull(field.getEditText()).getText().toString();

      if (!number.isEmpty()) {
         field.setError(null);
         field.setErrorEnabled(false);
         return PHONE.matcher(number).matches();
      } else {
         field.setError(inValidField);
         return false;
      }
   }

   public static boolean isValidAddress(@NonNull TextInputLayout field) {
      String address = Objects.requireNonNull(field.getEditText()).getText().toString();

      if (!address.isEmpty()) {
         field.setError(null);
         field.setErrorEnabled(false);
         return true;
      } else {
         field.setError(inValidField);
         return false;
      }
   }
}
