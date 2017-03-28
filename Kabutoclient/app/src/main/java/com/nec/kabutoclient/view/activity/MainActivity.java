package com.nec.kabutoclient.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.nec.kabutoclient.KabutoApplication;
import com.nec.kabutoclient.R;
import com.nec.kabutoclient.data.OverridePendingType;
import com.nec.kabutoclient.data.sp.AppConfig;
import com.nec.kabutoclient.util.AppManager;
import com.nec.kabutoclient.view.activity.builder.BaseActivity;
import com.nec.kabutoclient.view.fragment.SplashFragment;
import com.nec.kabutoclient.view.fragment.WelcomeFragment;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

public class MainActivity extends BaseActivity {

    public boolean noAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TextUtils.isEmpty(AppConfig.getUserToken())) {
            setStatusBar(StatusBarType.FILL_MAIN);
            setContentView(R.layout.activity_main);
            if (savedInstanceState == null) {
                if (KabutoApplication.isColdStart) {
                    enterToFragment(false);
                } else {
                    // new Handler().postDelayed(() -> enterToFragment(false), 500);
                    splashAnim(false);
                }
            }
        }else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_main);
            if (savedInstanceState == null) {
                if (KabutoApplication.isColdStart) {
                    enterToFragment(true);
                } else {
                    // new Handler().postDelayed(() -> enterToFragment(true), 500);
                    splashAnim(true);
                }
            }
        }

        KabutoApplication.isColdStart = false;
        AppManager.getAppManager().addActivity(this);
    }

    public void enterToFragment(boolean isSplash) {
        try {
            if (isSplash) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, new SplashFragment())
                            .commitAllowingStateLoss();
                }
            } else {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, new WelcomeFragment())
                            .commitAllowingStateLoss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void splashAnim(boolean isSplash) {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("aplha", 0.3f, 1.0f);
        ObjectAnimator animator = ObjectAnimator.
                ofPropertyValuesHolder(findViewById(R.id.container), valuesHolder).setDuration(500);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                enterToFragment(isSplash);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public OverridePendingType currentOverridePendingType() {
        if (noAnim) {
            return OverridePendingType.NORMAL;
        }
        return super.currentOverridePendingType();
    }
}
