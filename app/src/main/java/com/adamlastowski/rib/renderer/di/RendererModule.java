package com.adamlastowski.rib.renderer.di;

import android.content.Context;
import android.content.res.Resources;

import com.adamlastowski.rib.commons.widgets.SquareGLSurfaceView;
import com.adamlastowski.rib.renderer.Renderer;
import com.adamlastowski.rib.renderer.RendererInteractor;

import dagger.Module;
import dagger.Provides;

@Module
public class RendererModule {
	@RendererScope
	@Provides
	public SquareGLSurfaceView provideSurfaceView(Context context) {
		return new SquareGLSurfaceView(context);
	}

	@Provides
	public Renderer provideRenderer(RendererInteractor interactor) {
		return interactor;
	}

	@Provides
	public Resources provideResources(Context context) {
		return context.getResources();
	}

}
