package com.nec.kabutoclient.view.activity.builder;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nec.kabutoclient.KabutoApplication;
import com.nec.kabutoclient.R;
import com.nec.kabutoclient.data.OverridePendingType;
import com.nec.kabutoclient.data.db.RealmManager;
import com.nec.kabutoclient.di.components.ApplicationComponent;
import com.nec.kabutoclient.di.components.DaggerActivityComponent;
import com.nec.kabutoclient.di.modules.ActivityModule;
import com.nec.kabutoclient.event.ShareEvent;
import com.nec.kabutoclient.listener.OnBackPressedListener;
import com.nec.kabutoclient.navigation.Navigator;
import com.nec.kabutoclient.util.StatusBarUtil;
import com.nec.kabutoclient.util.SystemUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liuzhonghu on 2017/3/21.
 *
 * @Description
 */

public abstract class BaseActivity extends FragmentActivity {

    @Inject
    Navigator navigator;
    private int statusBarTintColor;
    private StatusBarType statusBarType;
    private SystemBarTintManager tintManager;
    private List<OnBackPressedListener> onBackPressedListeners = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setSystemBar();
        super.onCreate(savedInstanceState);
        ApplicationComponent applicationComponent = getApplicationComponent();
        applicationComponent.inject(this);
        DaggerActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(this))
                .build();
        EventBus.getDefault().register(this);
        initRealm();
    }

    private void initRealm() {
        if (SystemUtils.isMainProcess((Application) getApplicationContext())) {
            RealmManager.getInstance().initRealm(getApplicationContext());
        }
    }

    @Override
    public void onBackPressed() {
        for (OnBackPressedListener onBackPressedListener : onBackPressedListeners) {
            if (onBackPressedListener.onBack()) {
                return;
            }
        }
        super.onBackPressed();
    }

    public void addOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        onBackPressedListeners.add(0, onBackPressedListener);
    }

    public void removeOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        onBackPressedListeners.remove(onBackPressedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public enum StatusBarType {
        FILL_MAIN, MAIN, LIVE_HOT, SCREEN, NORMAL, GAME_RULE, HOME_MAIN, NONE
    }

    public void setStatusBar(StatusBarType statusBarType) {
        setStatusBar(statusBarType, 0, 1);
    }

    private void setSystemBar() {
//        if (!(this instanceof LiveActivity) && !(this instanceof BigImageActivity)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                setTranslucentStatus(true);
//            }
//            if (this instanceof MainActivity) {
//                setTheme(R.style.MainTheme);
//            } else {
//                setTheme(R.style.AppTheme);
//            }
//        }
    }

    public void setStatusBar(StatusBarType statusBarType, int statusBarTintColor, float alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.statusBarTintColor = statusBarTintColor;
            setTranslucentStatus(true);
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            SystemBarTintManager.SystemBarConfig systemBarConfig = tintManager.getConfig();
            switch (statusBarType) {
                case FILL_MAIN:
                    this.statusBarType = statusBarType;
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintResource(R.color.translucent);
                    tintManager.setStatusBarAlpha(1);
                    findViewById(android.R.id.content).setPadding(0, 0, 0, 0);
                    break;
                case GAME_RULE:
                    this.statusBarType = statusBarType;
                    if (statusBarTintColor == 0) {
                        tintManager.setStatusBarTintEnabled(false);
                    } else {
                        tintManager.setStatusBarTintEnabled(true);
                        tintManager.setStatusBarTintColor(statusBarTintColor);
                        tintManager.setStatusBarAlpha(alpha);
                        findViewById(android.R.id.content).setPadding(0, 0,
                                0, 0);
                    }
                    break;
                case MAIN:
                    boolean result = StatusBarUtil.setStatusBarLightMode(this);
                    if (result) {
                        this.statusBarType = statusBarType;
                        tintManager.setStatusBarTintEnabled(true);
                        tintManager.setStatusBarTintResource(R.color.white);
                        // tintManager.setStatusBarAlpha(1);
                        findViewById(android.R.id.content).setPadding(0, 0, 0,
                                0);
                    } else {
                        this.statusBarType = statusBarType;
                        tintManager.setStatusBarTintEnabled(true);
                        tintManager.setStatusBarTintResource(R.color.white);
                        // tintManager.setStatusBarAlpha(1);
                        findViewById(android.R.id.content).setPadding(0, 0, 0, 0);
                    }
                    break;
                case LIVE_HOT:
                    boolean hotResult = StatusBarUtil.setStatusBarLightModeSysFontColor(this);
                    getWindow()
                            .getDecorView()
                            .setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    this.statusBarType = statusBarType;
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintResource(R.color.black);
                    findViewById(android.R.id.content).setPadding(0, 0, 0,
                            0);
                    break;
                case SCREEN:
                    this.statusBarType = statusBarType;
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintResource(R.color.translucent);
                    findViewById(android.R.id.content).setPadding(0, 0, 0, 0);
                    break;
                // case NORMAL:
                // this.statusBarType = statusBarType;
                // tintManager.setStatusBarTintEnabled(true);
                // tintManager.setStatusBarTintResource(R.color.white);
                // findViewById(android.R.id.content).setPadding(0, 0, 0,
                // 0);
                // break;
                case HOME_MAIN:
                    this.statusBarType = statusBarType;
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintResource(R.color.home_titlebar_color);
                    tintManager.setStatusBarAlpha(1);
                    findViewById(android.R.id.content).setPadding(0, 0, 0, 0);
                    break;
                case NONE:
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintResource(R.color.black);
                    findViewById(android.R.id.content).setPadding(0, systemBarConfig.getStatusBarHeight(), 0, 0);
                    break;
                default:
                    break;
            }
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ShareEvent event) {
        // TODO: 2017/3/28
    }

    /**
     * Get the Main Application component for dependency injection.
     */
    public ApplicationComponent getApplicationComponent() {
        return ((KabutoApplication) getApplicationContext()).getApplicationComponent();
    }
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void finish() {
        super.finish();
        switch (currentOverridePendingType()) {
            case SLIDE:
                super.overridePendingTransition(R.anim.slide_in_exit_left, R.anim.slide_out_right);
                break;
            case SLIDE_UP_DOWN:
                super.overridePendingTransition(R.anim.zoom_exit_null, R.anim.slide_out_to_bottom_no_alpha);
                break;
            case NULL:
                super.overridePendingTransition(R.anim.zoom_exit_null, R.anim.zoom_exit_null);
                break;
            default:
                break;
        }
    }

    public OverridePendingType currentOverridePendingType() {
        return OverridePendingType.SLIDE;
    }

}
