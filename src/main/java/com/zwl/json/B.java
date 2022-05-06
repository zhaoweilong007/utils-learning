package com.zwl.json;

import lombok.Data;

import java.util.List;

@Data
public class B {

  private String id;
  private String name;
  private String bm;
  private String fbm;
  private List<B> company;
}
