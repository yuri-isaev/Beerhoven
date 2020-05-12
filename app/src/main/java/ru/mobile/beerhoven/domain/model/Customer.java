package ru.mobile.beerhoven.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer extends User {
   private String id;
   private String email;
   private String name;
   private String phone;
}
