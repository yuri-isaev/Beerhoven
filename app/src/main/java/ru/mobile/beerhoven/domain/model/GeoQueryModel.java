package ru.mobile.beerhoven.domain.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeoQueryModel {
   private String g;
   private ArrayList<Double> l;
}
