package com.adamlastowski.rib.renderer.di;

import android.content.Context;
import android.content.res.Resources;
import android.widget.RelativeLayout;

import com.adamlastowski.rib.activity.di.ActivityComponent;
import com.adamlastowski.rib.renderer.Renderer;
import com.adamlastowski.rib.renderer.RendererBuilder;

import javax.inject.Named;

import dagger.Component;

@RendererScope
@Component(dependencies = {ActivityComponent.class}, modules = {RendererModule.class})
public interface RendererComponent {
	public void inject(RendererBuilder builder);

	public Renderer getRenderer();
	public Resources getResources();

	@Named("rootView")
	public RelativeLayout getRootView();

	public Context getContext();
}
