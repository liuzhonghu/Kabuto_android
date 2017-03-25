package com.nec.kabutoclient.di.components;

import com.nec.kabutoclient.di.PerFragment;
import com.nec.kabutoclient.di.modules.SplashFragmentModule;
import com.nec.kabutoclient.presenter.SplashFragmentPresenter;
import com.nec.kabutoclient.view.fragment.SplashFragment;

import dagger.Component;

/**
 * Created by liuzhonghu on 2017/3/25.
 *
 * @Description
 */
@PerFragment
@Component(modules = SplashFragmentModule.class)
public interface SplashFragmentComponent {
    SplashFragment inject(SplashFragment fragment);

    SplashFragmentPresenter presenter();
}
