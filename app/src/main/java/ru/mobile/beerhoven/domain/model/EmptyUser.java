package ru.mobile.beerhoven.domain.model;

import lombok.Data;

@Data
public class EmptyUser extends User {
   private String id    = "null";
   private String email = "null";
   private String name  = "null";
   private String phone = "null";
}
