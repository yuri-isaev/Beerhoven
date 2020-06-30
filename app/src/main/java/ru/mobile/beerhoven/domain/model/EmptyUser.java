package ru.mobile.beerhoven.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmptyUser extends User {
   private String id    = "null";
   private String email = "null";
   private String name  = "null";
   private String phone = "null";
}
