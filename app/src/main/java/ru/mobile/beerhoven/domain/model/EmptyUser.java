package ru.mobile.beerhoven.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyUser extends User {
   private String id = "null";
   private String email = "null";
   private String name = "null";
   private String phone = "null";
}
