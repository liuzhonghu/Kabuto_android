package com.nec.kabutoclient.di.modules;

import com.nec.kabutoclient.di.PerFragment;
import com.nec.kabutoclient.presenter.SplashFragmentPresenter;
import com.nec.kabutoclient.view.fragment.SplashFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liuzhonghu on 2017/3/25.
 *
 * @Description
 */
@Module
public class SplashFragmentModule {
    private SplashFragment fragment;

    public SplashFragmentModule(SplashFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @PerFragment
    SplashFragment provideSplashFragment() {
        return fragment;
    }

    @Provides
    @PerFragment
    SplashFragmentPresenter provideSplashFragmentPresenter() {
        SplashFragmentPresenter presenter = new SplashFragmentPresenter();
        presenter.attachView(fragment);
        return presenter;
    }
}
