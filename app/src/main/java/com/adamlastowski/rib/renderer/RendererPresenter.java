package com.adamlastowski.rib.renderer;

import javax.inject.Inject;

class RendererPresenter {

	RendererView view;
	RendererInteractor interactor;

	@Inject
	public RendererPresenter(RendererView rendererView) {
		view = rendererView;
	}

	public void attach(RendererInteractor interactor) {
		this.interactor = interactor;

		view.attach(this);
	}

	public void onSurfaceCreated() {
		interactor.onReady();
	}

	public void onSurfaceChanged(final int width, final int height) {
		interactor.onCanvasSizeChanged(width, height);
	}

	public void onDrawFrame() {
		interactor.onDraw();
	}
}
