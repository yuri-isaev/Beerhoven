package ru.mobile.beerhoven.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class News {
   private String description;
   private String id;
   private String image;
   private String time;
   private String title;
}
