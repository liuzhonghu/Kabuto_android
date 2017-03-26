package com.nec.kabutoclient.data.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConfigItem implements Serializable {


  private static final long serialVersionUID = 856589664475313473L;
  @SerializedName("id")
  private int id;
  @SerializedName("type")
  private int type;
  @SerializedName("value")
  private String value;
  @SerializedName("description")
  private String description;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
