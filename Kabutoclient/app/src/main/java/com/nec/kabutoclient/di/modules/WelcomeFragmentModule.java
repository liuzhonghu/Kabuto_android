package com.nec.kabutoclient.di.modules;

import com.nec.kabutoclient.di.PerFragment;
import com.nec.kabutoclient.presenter.WelcomeFragmentPresenter;
import com.nec.kabutoclient.view.fragment.WelcomeFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liuzhonghu on 2017/3/23.
 *
 * @Description
 */
@Module
public class WelcomeFragmentModule {
    private WelcomeFragment fragment;

    public WelcomeFragmentModule(WelcomeFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @PerFragment
    WelcomeFragment provideWelcomeFragment() {
        return fragment;
    }

    @Provides
    @PerFragment
    WelcomeFragmentPresenter provideWelcomeFragmentPresenter() {
        WelcomeFragmentPresenter presenter = new WelcomeFragmentPresenter();
        presenter.attachView(fragment);
        return presenter;
    }
}
