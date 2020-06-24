package ru.mobile.beerhoven.domain.model;

import lombok.Data;

@Data
public class News {
   private String id;
   private String description;
   private String image;
   private String time;
   private String title;
}
