package com.nec.kabutoclient.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nec.kabutoclient.R;
import com.nec.kabutoclient.di.components.DaggerWelcomeFragmentComponent;
import com.nec.kabutoclient.di.modules.WelcomeFragmentModule;
import com.nec.kabutoclient.presenter.WelcomeFragmentPresenter;
import com.nec.kabutoclient.view.impl.IWelcomeView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuzhonghu on 2017/3/22.
 *
 * @Description
 */

public class WelcomeFragment extends BaseFragment implements IWelcomeView {

    @Inject
    WelcomeFragmentPresenter presenter;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        rootView.setOnTouchListener((v, event) -> true);
        unbinder = ButterKnife.bind(this, rootView);
        initViewData();
        addListener();
        return rootView;
    }

    private void initViewData() {
        DaggerWelcomeFragmentComponent.builder().welcomeFragmentModule(new WelcomeFragmentModule(this))
                .build().inject(this);

    }

    private void addListener() {

    }
}
