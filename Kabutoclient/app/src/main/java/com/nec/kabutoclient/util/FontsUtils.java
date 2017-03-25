package com.nec.kabutoclient.util;

import android.graphics.Typeface;

import com.nec.kabutoclient.KabutoApplication;


public class FontsUtils {
  private static Typeface normalTypeface;
  private static Typeface normalBoldTypeface;
  private static Typeface numTypeface;
  private static Typeface yuantiTypeface;
  private static Typeface baskervilleTypeface;
  private static Typeface superCarTitleTypeface;
  private static Typeface baskervilleBoldMiniTypeface;
  private static Typeface fireCatTypeface;

  private FontsUtils() {}

  public static void init() {
    normalTypeface = Typeface.DEFAULT;
    normalBoldTypeface = Typeface.DEFAULT_BOLD;
    numTypeface = Typeface.createFromAsset(KabutoApplication.getAppContext().getAssets(),
        "fonts/GeorgiaMini.otf");
    fireCatTypeface = Typeface.createFromAsset(KabutoApplication.getAppContext().getAssets(),
        "fonts/fire_car.ttf");
    yuantiTypeface = Typeface.createFromAsset(KabutoApplication.getAppContext().getAssets(),
        "fonts/yuanti001Status.ttf");
    baskervilleTypeface = Typeface.createFromAsset(KabutoApplication.getAppContext().getAssets(),
        "fonts/Baskerville.ttf");
    baskervilleTypeface = Typeface.create(baskervilleTypeface, Typeface.BOLD_ITALIC);
    superCarTitleTypeface = Typeface.createFromAsset(KabutoApplication.getAppContext().getAssets(),
        "fonts/SuperCarTitle.ttf");
    baskervilleBoldMiniTypeface =
        Typeface.createFromAsset(KabutoApplication.getAppContext().getAssets(),
            "fonts/BaskervilleBoldMini.ttf");
    superCarTitleTypeface = Typeface.create(superCarTitleTypeface, Typeface.ITALIC);
  }

  public static Typeface getNormalTypeface() {
    return normalTypeface;
  }

  public static Typeface getNormalBoldTypeface() {
    return normalBoldTypeface;
  }

  public static Typeface getNumTypeface() {
    return numTypeface;
  }

  public static Typeface getYuanTiTypeface() {
    return yuantiTypeface;
  }

  public static Typeface getBaskervilleTypeface() {
    return baskervilleTypeface;
  }

  public static Typeface getBaskervilleBoldMiniTypeface() {
    return baskervilleBoldMiniTypeface;
  }

  public static Typeface getSuperCarTitleTypeface() {
    return superCarTitleTypeface;
  }

  public static Typeface getFireCatTypeface() {
    return fireCatTypeface;
  }
}
