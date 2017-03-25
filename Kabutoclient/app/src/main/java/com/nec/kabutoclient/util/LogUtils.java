package com.nec.kabutoclient.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * 好用的Log工具
 * 分两种log<br/>
 * 1 普通log TAG自动产生，格式为：类名.方法名(行号)；建议普通log使用  log.i/d/....<br/>
 * 2 超级log TAG默认为LogUtils 格式为：类名.方法名.行号，并且双击可跳转，但耗能是普通log的20倍，故默认关闭打印，建议发布前关闭  log.printI/D....<br/>
 * Created by Cinema chengWangYong on 2015/4/17 /0017.
 */
public class LogUtils {
    public static String TAG = "LogUtils";
    private static final int JSON_INDENT = 4;

    private LogUtils() {
    }

    private static String getPrefix() {
        String prefix = "%s.%s(L:%d)";

        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        prefix = String.format(Locale.CHINA,prefix, callerClazzName, caller.getMethodName(),
                caller.getLineNumber());
        return prefix;
    }

    /**
     * Send a DEBUG log message,蓝色
     */
    public static void d(String content) {
        Log.d(getPrefix(), content);
    }

    /**
     * Send an ERROR log message,红色
     */
    public static void e(String content) {
        Log.e(getPrefix(), content+"");
    }

    /**
     * Send an INFO log message,绿色
     */
    public static void i(String content) {
        Log.i(getPrefix(), content);

    }

    /**
     * Send a VERBOSE log message,黑色
     */
    public static void v(String content) {
        Log.v(getPrefix(), content);
    }

    /**
     * Send a WARN log message,黄色
     */
    public static void w(String content) {
        Log.w(getPrefix(), content);
    }

    public static void e(String content, Throwable tr) {
        Log.e(getPrefix(), content, tr);
    }

    public static void e(Throwable tr) {
        Log.e(getPrefix(), tr.getMessage());
    }


    /*******************super log 带行号并且点击可跳转的log**************************/
    /**
     * Send an INFO log message,绿色
     */
    public static void printI(String log) {
        Log.i(TAG, log + "-->" + callMethodAndLine());
    }

    /**
     * Send a DEBUG log message,蓝色
     */
    public static void printD(String log) {
        Log.d(TAG, log + "-->" + callMethodAndLine());
    }

    /**
     * Send an ERROR log message,红色
     */
    public static void printE(String log) {
        Log.e(TAG, log + "-->" + callMethodAndLine());
    }

    /**
     * Send a VERBOSE log message,黑色/白色
     */
    public static void printV(String log) {
        Log.v("TAG",log + "-->" + callMethodAndLine());
    }

    /**
     * Send a WARN log message,黄色
     */
    public static void printW(String log) {
        Log.w(TAG, log + "-->" + callMethodAndLine());
    }


    private static String callMethodAndLine() {
        StringBuilder result = new StringBuilder("at ");
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result.append(thisMethodStack.getClassName() + ".").append(thisMethodStack.getMethodName()).append("(" + thisMethodStack.getFileName()).append(":" + thisMethodStack.getLineNumber() + ")  ");
        return result.toString();
    }

    /**
     * 答应
     * @param xml
     */
    public static void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            Log.d(TAG, "Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            Log.d(TAG, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            Log.d(TAG, e.getCause().getMessage() + "\n" + xml);
        }
    }


    /**
     * Formats the json content and print it
     * 格式JSON内容和打印
     *
     * @param json the json content
     */
    public static void json(String json) {

        if (TextUtils.isEmpty(json)) {
            Log.d(TAG, "Empty/Null json content");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                Log.d(TAG, message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                Log.d(TAG, message);
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getCause().getMessage() + "\n" + json);
        }
    }


//    private static String getClassName() {
//        String result = "";
//        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
//        result = thisMethodStack.getClassName();
//        return result;
//    }
}
