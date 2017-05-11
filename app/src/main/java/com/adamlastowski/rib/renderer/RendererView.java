package com.adamlastowski.rib.renderer;

import android.opengl.GLSurfaceView;
import android.widget.RelativeLayout;

import com.adamlastowski.rib.R;
import com.adamlastowski.rib.commons.widgets.SquareGLSurfaceView;

import javax.inject.Inject;
import javax.inject.Named;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class RendererView implements GLSurfaceView.Renderer {
	SquareGLSurfaceView surfaceView;

	RelativeLayout rootView;
	RendererPresenter presenter;

	@Inject
	RendererView(@Named("rootView") RelativeLayout rootView, SquareGLSurfaceView surfaceView) {
		this.surfaceView = surfaceView;
		this.rootView = rootView;
	}

	public void attach(RendererPresenter presenter) {
		this.presenter = presenter;
		surfaceView.setId(R.id.renderer_view);
		surfaceView.setEGLContextClientVersion(2);
		surfaceView.setRenderer(this);
		surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		rootView.addView(surfaceView);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		surfaceView.post(new Runnable() {
			@Override
			public void run() {
				presenter.onSurfaceCreated();
			}
		});
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		presenter.onSurfaceChanged(width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		presenter.onDrawFrame();
	}
}
