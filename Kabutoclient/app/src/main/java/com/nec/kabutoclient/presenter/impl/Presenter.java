package com.nec.kabutoclient.presenter.impl;

import com.nec.kabutoclient.view.impl.MvpView;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface Presenter<V extends MvpView> {

  void attachView(V mvpView);

  void detachView();
}
