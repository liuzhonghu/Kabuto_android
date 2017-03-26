package com.nec.kabutoclient.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nec.kabutoclient.R;
import com.nec.kabutoclient.presenter.SplashFragmentPresenter;
import com.nec.kabutoclient.view.fragment.builder.BaseFragment;
import com.nec.kabutoclient.view.impl.ISplashView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuzhonghu on 2017/3/22.
 *
 * @Description
 */

public class SplashFragment extends BaseFragment implements ISplashView {

    @Inject
    SplashFragmentPresenter presenter;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        rootView.setOnTouchListener((v, event) -> true);
        unbinder = ButterKnife.bind(this, rootView);
        initViewData();
        addListener();
        return rootView;
    }

    private void initViewData() {
//        DaggerSplashFragmentComponent.builder().splashFragmentModule(new SplashFragmentModule(this))
//                .build().inject(this);
    }

    private void addListener() {

    }
}
