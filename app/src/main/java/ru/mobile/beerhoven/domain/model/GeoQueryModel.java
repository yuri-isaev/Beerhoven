package ru.mobile.beerhoven.domain.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class GeoQueryModel {
   private String g;
   private ArrayList<Double> l;
}
