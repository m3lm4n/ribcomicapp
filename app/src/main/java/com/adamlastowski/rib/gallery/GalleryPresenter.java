package com.adamlastowski.rib.gallery;

import javax.inject.Inject;

/**
 * Created by adamlastowski on 19/04/2017.
 */

class GalleryPresenter {

	private final GalleryView view;
	private GalleryInteractor interactor;

	@Inject
	public GalleryPresenter(GalleryView galleryView) {
		view = galleryView;
	}

	public void attach(GalleryInteractor galleryInteractor) {
		interactor = galleryInteractor;

		view.attach(this);
	}

//	@Inject
//	public RendererPresenter(RendererView rendererView) {
//		view = rendererView;
//	}
//
//	public void attach(RendererInteractor interactor) {
//		this.interactor = interactor;
//
//		view.attach(this);
//	}
//
//	public void onSurfaceCreated() {
//		interactor.onReady();
//	}
//
//	public void onSurfaceChanged(final int width, final int height) {
//		interactor.onCanvasSizeChanged(width, height);
//	}
//
//	public void onDrawFrame() {
//		interactor.onDraw();
//	}
}
