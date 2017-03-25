package com.nec.kabutoclient.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.nec.kabutoclient.KabutoApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * System and device related utils.
 */
public class SystemUtils {
  private static String versionName = null;
  private static int versionCode = 0;
  private static String deviceId = null;
  private static String deviceMAC = null;

  public static Paint drawPaint = new Paint();

  private SystemUtils() {}

  public static boolean aboveApiLevel(int sdkInt) {
    return getApiLevel() >= sdkInt;
  }

  public static int getApiLevel() {
    return Build.VERSION.SDK_INT;
  }

  public static boolean isSDCardMounted() {
    return Environment.getExternalStorageState().equals(
        Environment.MEDIA_MOUNTED);
  }

  public static String getWifiIPAddress(Context context) {
    try {
      WifiManager mgr = (WifiManager) context
          .getSystemService(Context.WIFI_SERVICE);
      if (mgr == null) {
        return null;
      }

      WifiInfo info = mgr.getConnectionInfo();
      if (info == null) {
        return null;
      }

      int ipAddress = info.getIpAddress();
      if (ipAddress == 0) {
        return null;
      }

      String ip = String.format(Locale.US, "%d.%d.%d.%d", (ipAddress & 0xff),
          (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
          (ipAddress >> 24 & 0xff));

      return ip;
    } catch (Exception e) {
      return null;
    }
  }

  public static String getPhoneIp() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
          .hasMoreElements();) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
            .hasMoreElements();) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
            return inetAddress.getHostAddress().toString();
          }
        }
      }
    } catch (Exception e) {}
    return "";
  }

  public static String getGPRSLocalIpAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface
          .getNetworkInterfaces(); en.hasMoreElements();) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf
            .getInetAddresses(); enumIpAddr.hasMoreElements();) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            return inetAddress.getHostAddress();
          }
        }
      }
    } catch (SocketException ex) {}
    return null;
  }

  public static int getVersionCode(Context context) {
    if (versionCode != 0) {
      return versionCode;
    }
    PackageInfo packageInfo;
    try {
      packageInfo = context.getPackageManager().getPackageInfo(
          context.getPackageName(), 0);
      versionCode = packageInfo.versionCode;
      return versionCode;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static String getImei(Context context) {
    try {
      TelephonyManager telephonyManager = (TelephonyManager) context
          .getSystemService(Context.TELEPHONY_SERVICE);
      return telephonyManager.getDeviceId();
    } catch (Exception e) {
      // In some devices, we are not able to get device id, and may cause some exception,
      // so catch it.
      return "";
    }
  }

  public static String getImsi(Context context) {
    try {
      TelephonyManager telephonyManager = (TelephonyManager) context
          .getSystemService(Context.TELEPHONY_SERVICE);
      return telephonyManager.getSubscriberId();
    } catch (Exception e) {
      // In some devices, we are not able to get device id, and may cause some exception,
      // so catch it.
      return "";
    }
  }

  public static String getVersionName(Context context) {
    if (versionName == null) {
      PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(), 0);
      if (packageInfo != null) {
        versionName = packageInfo.versionName;
      } else {
        versionName = "";
      }

    }
    return versionName;
  }

  public static String getFullVersion(Context context) {
    return getVersionName(context) + "." + getVersionCode(context);
  }

  public static PackageInfo getPackageInfo(Context context, String packageName, int flag) {
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo = null;
    try {
      packageInfo = packageManager.getPackageInfo(packageName, flag);
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    } catch (RuntimeException e) {
      // In some ROM, there will be a PackageManager has died exception. So we catch it here.
      e.printStackTrace();
    }
    return packageInfo;
  }

  public static int getNavigationBarHeightPx() {
    Resources resources = KabutoApplication.getAppContext().getResources();
    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    if (resourceId > 0) {
      return resources.getDimensionPixelSize(resourceId);
    }
    return 0;
  }

  public static int getStatusBarHeightPx() {
    try {
      Class c = Class.forName("com.android.internal.R$dimen");
      Object obj = c.newInstance();
      Field field = c.getField("status_bar_height");
      int x = Integer.parseInt(field.get(obj).toString());
      return KabutoApplication.getAppContext().getResources().getDimensionPixelSize(x);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static int getScreenHeightPx() {
    WindowManager windowManager = (WindowManager) KabutoApplication.getAppContext()
        .getSystemService(Context.WINDOW_SERVICE);
    Display defaultDisplay = windowManager.getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    defaultDisplay.getMetrics(metrics);
    return metrics.heightPixels;
  }

  public static int getScreenWidthPx() {
    WindowManager windowManager = (WindowManager) KabutoApplication.getAppContext()
        .getSystemService(Context.WINDOW_SERVICE);
    Display defaultDisplay = windowManager.getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    defaultDisplay.getMetrics(metrics);
    return metrics.widthPixels;
  }

  public static String getBrand() {
    if (TextUtils.isEmpty(Build.BRAND)) {
      return "";
    } else {
      return Build.BRAND;
    }
  }

  public static String getDeviceVersion() {
    return Build.VERSION.RELEASE;
  }

  public static String getModelVersion() {
    return Build.MODEL;
  }

  public static String getSdkVersion() {
    return String.valueOf(Build.VERSION.SDK_INT);
  }

  public static void showInputMethod(View view) {
    try {
      InputMethodManager imm = (InputMethodManager) view.getContext()
          .getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void hideInputMethod(View view) {
    if (view != null && view.getContext() != null && view.getWindowToken() != null) {
      try {
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(view.getWindowToken(), 0);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


  public static boolean isPackageInstalled(Context context, String packageName) {
    PackageManager packageManager = context.getPackageManager();
    List<PackageInfo> packages = packageManager.getInstalledPackages(0);
    for (PackageInfo packageInfo : packages) {
      if (packageInfo.packageName.equals(packageName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断当前屏幕是否锁屏.
   *
   * @param context
   * @return boolean
   */
  public static boolean inKeyguardRestrictedInputMode(Context context) {
    KeyguardManager mKeyguardManager =
        (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    return mKeyguardManager.inKeyguardRestrictedInputMode();
  }

  /**
   * 屏幕是否是亮着的.
   *
   * @param context
   * @return boolean
   */
  public static boolean isScreenOn(Context context) {
    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    return pm.isScreenOn();
  }

  public static int dpToPx(float dp) {
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        KabutoApplication.getAppContext().getResources().getDisplayMetrics());
    return (int) px;
  }

  public static int pxTodp(float px) {
    float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px,
        KabutoApplication.getAppContext().getResources().getDisplayMetrics());
    return (int) dp;
  }

  public static int getRealScreenHeight() {
    if (Build.BRAND.toLowerCase().contains("meizu")) {
      return SystemUtils.getScreenHeightPx() - SystemUtils.getNavigationBarHeightPx();
    }
    return SystemUtils.getScreenHeightPx();
  }

  public static synchronized String getDeviceId() {
    if (TextUtils.isEmpty(deviceId)) {
      Context context = KabutoApplication.getAppContext();
      TelephonyManager telephonyManager =
          (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      deviceId = telephonyManager.getDeviceId();
    }
    return deviceId;
  }

  public static synchronized String getMACAddress() {
    if (TextUtils.isEmpty(deviceMAC)) {
      Context context = KabutoApplication.getAppContext();
      WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      if (wifiManager != null) {
        WifiInfo info = null;
        try {
          // here maybe throw exception in android framework
          info = wifiManager.getConnectionInfo();
        } catch (Exception e) {
          e.printStackTrace();
        }
        if (info != null) {
          deviceMAC = info.getMacAddress();
        }
      }
    }
    return deviceMAC;
  }

  /**
   * @param value
   * @return 将dip或者dp转为float
   */
  public static float dipOrDpToFloat(String value) {
    if (value.indexOf("dp") != -1) {
      value = value.replace("dp", "");
    } else {
      value = value.replace("dip", "");
    }
    return Float.parseFloat(value);
  }

  public static boolean isEn(Context context) {
    Locale locale = context.getResources().getConfiguration().locale;
    String language = locale.getLanguage();
    if (language.endsWith("en"))
      return true;
    else
      return false;
  }

  /**
   * 获取虚拟按键栏的高度
   * 
   * @param context
   * @return
   */
  public static int navigationBarHeight = 0;

  public static int getNavigationBarHeight(Context context) {
    if (navigationBarHeight == 0) {
      if (hasNavBar(context)) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
          navigationBarHeight = res.getDimensionPixelSize(resourceId);
        }
      } else if (isMeizu()) {
        navigationBarHeight = getSmartBarHeight(context);
      }
    }
    return navigationBarHeight;
  }

  /**
   * 检查是否存在虚拟按键栏
   * 
   * @param context
   * @return
   */
  @SuppressLint("NewApi")
  private static boolean hasNavBar(Context context) {
    Resources res = context.getResources();
    int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
    if (resourceId != 0) {
      boolean hasNav = res.getBoolean(resourceId);
      // check override flag
      String sNavBarOverride = getNavBarOverride();
      if ("1".equals(sNavBarOverride)) {
        hasNav = false;
      } else if ("0".equals(sNavBarOverride)) {
        hasNav = true;
      }
      return hasNav;
    } else { // fallback
      return !ViewConfiguration.get(context).hasPermanentMenuKey();
    }
  }

  /**
   * 判断虚拟按键栏是否重写
   * 
   * @return
   */
  private static String getNavBarOverride() {
    String sNavBarOverride = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      try {
        Class c = Class.forName("android.os.SystemProperties");
        Method m = c.getDeclaredMethod("get", String.class);
        m.setAccessible(true);
        sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
      } catch (Throwable e) {}
    }
    return sNavBarOverride;
  }

  /**
   * 判断是否meizu手机
   * 
   * @return
   */
  public static boolean isMeizu() {
    return Build.BRAND.equals("Meizu");
  }

  /**
   * 获取魅族手机底部虚拟键盘高度
   * 
   * @param context
   * @return
   */
  public static int getSmartBarHeight(Context context) {
    try {
      Class c = Class.forName("com.android.internal.R$dimen");
      Object obj = c.newInstance();
      Field field = c.getField("mz_action_button_min_height");
      int height = Integer.parseInt(field.get(obj).toString());
      return context.getResources().getDimensionPixelSize(height);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * 应用市场升级Intent
   * 
   * @return
   */
  private static Intent getMarketUpgradeIntent() {
    return new Intent("android.intent.action.VIEW",
        Uri.parse("market://details?id=com.qiliuwu.kratos"));
  }


  public static void updateVersionFromMaket(Context context, String maketPackageName) {
    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
    if (!TextUtils.isEmpty(maketPackageName)
        && SystemUtils.isPackageInstalled(context, maketPackageName)) {
      goToMarket.setPackage(maketPackageName);
    }
    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try {
      context.startActivity(goToMarket);
    } catch (ActivityNotFoundException e) {
      Toast.makeText(context, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * caolei， 变白图
   */
  public static Bitmap createWhiteBitmap(Bitmap bitmap) {
    if (bitmap == null)
      return null;
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();
    int[] data = new int[w * h];
    bitmap.getPixels(data, 0, w, 0, 0, w, h);
    for (int i = data.length - 1; i >= 0; i--) {
      if ((data[i] & 0xff000000) != 0x00) {
        data[i] = 0xffffffff;
      }
    }
    return Bitmap.createBitmap(data, 0, w, w, h, Bitmap.Config.ARGB_8888);
  }

  /**
   * 居中绘制描边字符串
   */
  public static void drawStrokedText(String content, Canvas canvas, Typeface typeface, float x,
                                     float y,
                                     int textSize, int textColor, int strokeColor, float alpha) {
    int fontSize = textSize;
    Paint paint = drawPaint;// new Paint();
    paint.reset();
    if (typeface != null) {
      paint.setTypeface(typeface);
    }
    if (alpha < 0) {
      alpha = 0;
    }
    String name = content;

    int bColor = strokeColor;
    int fColor = textColor;

    paint.setTextAlign(Paint.Align.CENTER);
    float fx = x;
    float fy = y;
    paint.setColor(bColor);
    paint.setTextSize(fontSize + 3);
    paint.setAntiAlias(true);

    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(8.0f);
    paint.setColor(bColor);
    paint.setAlpha((int) (alpha * 255));
    canvas.drawText(name, fx, fy, paint);

    paint.setStyle(Paint.Style.FILL);
    paint.setColor(fColor);
    paint.setAlpha((int) (alpha * 255));
    canvas.drawText(name, fx, fy, paint);
  }

  public static void drawTripleStrokedText(String content, Canvas canvas, Typeface typeface,
                                           float x, float y,
                                           int textSize, int textColor, int strokeColor, float alpha) {

    int fontSize = textSize;
    Paint paint = drawPaint;// new Paint();
    paint.reset();
    if (typeface != null) {
      paint.setTypeface(typeface);
    }
    if (alpha < 0) {
      alpha = 0;
    }
    String name = content;

    int bColor = strokeColor;
    int fColor = textColor;

    paint.setTextAlign(Paint.Align.CENTER);
    float fx = x;
    float fy = y;
    paint.setColor(bColor);
    paint.setTextSize(fontSize);
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);

    paint.setStrokeWidth(8.0f);
    paint.setColor(fColor);
    paint.setAlpha((int) (alpha * 255));
    canvas.drawText(name, fx, fy, paint);

    paint.setStrokeWidth(2.0f);
    paint.setColor(bColor);
    paint.setAlpha((int) (alpha * 255));
    canvas.drawText(name, fx, fy, paint);
  }

  public static void drawThreeColorText(String content, Canvas canvas, Typeface typeface, float x,
                                        float y,
                                        int textSize, int firstColor, int secondColor, LinearGradient thirdColor, float alpha) {
    // 三种不同颜色
    Paint paint = drawPaint;// new Paint();
    paint.reset();
    if (typeface != null) {
      paint.setTypeface(typeface);
    }
    if (alpha < 0) {
      alpha = 0;
    }
    paint.setTextAlign(Paint.Align.CENTER);
    paint.setTextSize(textSize);
    paint.setAntiAlias(true);
    float fx = x;
    float fy = y;
    int intAlpha = (int) (alpha * 255);

    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(18);
    paint.setColor(firstColor);
    paint.setAlpha(intAlpha);
    canvas.drawText(content, fx, fy, paint);

    paint.setStrokeWidth(12);
    paint.setColor(secondColor);
    paint.setAlpha(intAlpha);
    canvas.drawText(content, fx, fy, paint);

    paint.setTextSize(textSize);
    paint.setStyle(Paint.Style.FILL);
    paint.setShader(thirdColor);
    paint.setAlpha(intAlpha);
    canvas.drawText(content, fx, fy, paint);
  }

  public static void clearFrescoMemoryCache() {
    ImagePipeline imagePipeline = Fresco.getImagePipeline();
    imagePipeline.clearMemoryCaches();
  }

  public static String getChannelName() {
    //return BuildConfig.FLAVOR;
    return ChannelUtil.getChannel(KabutoApplication.getAppContext());
  }


  public static boolean isMainProcess(Application kratosApplication) {
    String packageName = getProcessName(kratosApplication);
    return !TextUtils.isEmpty(packageName)
        && packageName.equals(kratosApplication.getPackageName());
  }

  public static String getProcessName(Context context) {
    int myPid = android.os.Process.myPid();
    if (context != null) {
      ActivityManager manager =
          (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
      if (processes != null) {
        for (ActivityManager.RunningAppProcessInfo process : processes) {
          if (process.pid == myPid) {
            return process.processName;
          }
        }
      }
    }
    return "";
  }



  public static String getMD5(String val) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(val.getBytes());
      byte[] m = md5.digest();// 加密
      return getString(m);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String getString(byte[] b) {
    StringBuilder sb = new StringBuilder();
    for (byte aB : b) {
      sb.append(aB);
    }
    return sb.toString();
  }

  /**
   * 通过字符串名字获取资源id
   * 
   * @param variableName
   * @param c
   * @return
   */
  public static int getResId(String variableName, Class<?> c) {
    try {
      Field idField = c.getDeclaredField(variableName);
      return idField.getInt(idField);
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  public static float getResDimen(@DimenRes int resId) {
    return KabutoApplication.getAppContext().getResources().getDimension(resId);
  }

  public static int getResColor(@ColorRes int resId) {
    return KabutoApplication.getAppContext().getResources().getColor(resId);
  }

}
