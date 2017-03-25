package com.nec.kabutoclient.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IntDef;

import com.nec.kabutoclient.KabutoApplication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Toast extends android.widget.Toast {
    /**
     * @hide
     */
    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    public Toast(Context context) {
        super(context);
    }

    private static android.widget.Toast toast;

    public static void showToast(Context context, CharSequence text,
                                 @Duration int duration) {
        // android.widget.Toast result = new Toast(context);
        // LayoutInflater inflate = (LayoutInflater)
        // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View layout = inflate.inflate(R.layout.toast, null);
        // TextView tv = (TextView) layout.findViewById(R.id.toast_tv);
        // tv.setTypeface(FontsUtils.getRobotoSlabRegular());
        // tv.setText(text);
        // result.setView(layout);
        // result.setText(text);
        // result.setDuration(duration);
        // result.show();
        Toast.makeText(context, text, duration).show();
    }

    public static void showLongToast(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(KabutoApplication.getAppContext(), text,
                    android.widget.Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showLongToast(int textId) {
        if (toast == null) {
            toast = Toast.makeText(KabutoApplication.getAppContext(), textId,
                    android.widget.Toast.LENGTH_LONG);
        } else {
            toast.setText(textId);
        }
        toast.show();
    }

    public static void showShortToast(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(KabutoApplication.getAppContext(), text,
                    android.widget.Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showShortToast(int textId) {
        if (toast == null) {
            toast = Toast.makeText(KabutoApplication.getAppContext(), textId,
                    android.widget.Toast.LENGTH_SHORT);
        } else {
            toast.setText(textId);
        }
        toast.show();
    }

    public static void showToast(Context context, int textResId,
                                 @Duration int duration)
            throws Resources.NotFoundException {
        showToast(context, context.getResources().getText(textResId), duration);
    }
}
