package com.nec.kabutoclient.di.components;


import android.content.Context;

import com.nec.kabutoclient.di.modules.ApplicationModule;
import com.nec.kabutoclient.view.activity.builder.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(BaseActivity baseActivity);

  //Exposed to sub-graphs.
  Context context();
}
