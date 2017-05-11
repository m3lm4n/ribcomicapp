package com.adamlastowski.rib.activity.di;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import com.adamlastowski.rib.R;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

	private final Activity activity;

	public ActivityModule(Activity activity) {
		this.activity = activity;
	}

	@ActivityScope
	@Provides
	public Context provideContext() {
		return activity;
	}

	@ActivityScope
	@Provides
	@Named("rootView")
	public RelativeLayout provideRootView() {
		return (RelativeLayout) activity.findViewById(R.id.content_view);
	}
}
