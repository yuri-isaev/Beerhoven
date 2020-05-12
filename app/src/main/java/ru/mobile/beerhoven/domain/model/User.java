package ru.mobile.beerhoven.domain.model;

public abstract class User {
   public abstract String getId();
   public abstract void setId(String Id);
   public abstract String getEmail();
   public abstract void setEmail(String email);
   public abstract String getName();
   public abstract void setName(String name);
   public abstract String getPhone();
   public abstract void setPhone(String phone);
}
