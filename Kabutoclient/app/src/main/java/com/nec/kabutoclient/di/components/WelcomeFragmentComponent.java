package com.nec.kabutoclient.di.components;

import com.nec.kabutoclient.di.PerFragment;
import com.nec.kabutoclient.di.modules.WelcomeFragmentModule;
import com.nec.kabutoclient.presenter.WelcomeFragmentPresenter;
import com.nec.kabutoclient.view.fragment.WelcomeFragment;

import dagger.Component;

/**
 * Created by liuzhonghu on 2017/3/23.
 *
 * @Description
 */
@PerFragment
@Component(modules = WelcomeFragmentModule.class)
public interface WelcomeFragmentComponent {
    WelcomeFragment inject(WelcomeFragment fragment);

    WelcomeFragmentPresenter presenter();
}
