package ru.mobile.beerhoven.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import ru.mobile.beerhoven.R;

public class RegistrationFragment extends Fragment {

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_registration, container, false);
      TextInputLayout mRegName = view.findViewById(R.id.reg_name);
      TextInputLayout mRegEmail = view.findViewById(R.id.req_email);
      TextInputLayout mRegPhoneNo = view.findViewById(R.id.reg_phone_number);
      Button btnRegister = view.findViewById(R.id.reg_btn);
      return view;
   }
}
