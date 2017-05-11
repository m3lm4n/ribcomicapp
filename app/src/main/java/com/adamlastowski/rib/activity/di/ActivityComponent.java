package com.adamlastowski.rib.activity.di;

import android.content.Context;
import android.widget.RelativeLayout;

import javax.inject.Named;

import dagger.Component;

@ActivityScope
@Component(modules = {ActivityModule.class})
public interface ActivityComponent {

	public Context getContext();

	@Named("rootView")
	public RelativeLayout getRootView();
}
