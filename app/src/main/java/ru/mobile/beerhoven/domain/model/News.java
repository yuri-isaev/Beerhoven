package ru.mobile.beerhoven.domain.model;

public class News {
   private String description;
   private String time;
   private String title;
   private String uri;

   public News(String description, String time, String title, String uri) {
      this.description = description;
      this.time = time;
      this.title = title;
      this.uri = uri;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getUri() {
      return uri;
   }

   public void setUri(String uri) {
      this.uri = uri;
   }
}
