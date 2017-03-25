package com.nec.kabutoclient.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Date: 2016-07-01
 * Time: 17:45
 */
public class GsonUtils {
  private static Gson gsonInstance;

  public synchronized static Gson getInstance() {
    if (gsonInstance == null) {
      gsonInstance = new Gson();
    }
    return gsonInstance;
  }


  public static <T> T fromJson(String gson, Class<T> t) {
    return getInstance().fromJson(gson, t);
  }

  public static <T> List<T> fromJson(String gson, Type typeOfT) {
    return getInstance().fromJson(gson, typeOfT);
  }

  public static <T> String toJson(T t) {
    return getInstance().toJson(t);
  }
}
