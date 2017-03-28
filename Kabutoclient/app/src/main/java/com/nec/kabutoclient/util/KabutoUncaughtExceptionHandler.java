package com.nec.kabutoclient.util;

import android.os.Environment;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

/**
 * Created by liuzhonghu on 2017/3/28.
 *
 * @Description
 */

public class KabutoUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    private static final String DIAGNOSIS_DIR =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/nec_kabuto/diagnosis/";
    Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler =
            Thread.getDefaultUncaughtExceptionHandler();

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        saveCrashLog(ex);
        defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

    private void saveCrashLog(Throwable ex) {
        File file = new File(DIAGNOSIS_DIR);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return;
            }
        }
        PrintStream stream = null;
        try {
            stream = new PrintStream(DIAGNOSIS_DIR + "last_crash_log.txt");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = dateFormat.format(new java.util.Date());
            stream.println(date);
            ex.printStackTrace(stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
