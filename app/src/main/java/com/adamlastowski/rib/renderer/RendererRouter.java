package com.adamlastowski.rib.renderer;

import com.adamlastowski.rib.camera_source.CameraSourceBuilder;
import com.adamlastowski.rib.camera_source.CameraSourceRouter;
import com.adamlastowski.rib.gallery.GalleryBuilder;
import com.adamlastowski.rib.gallery.GalleryRouter;
import com.adamlastowski.rib.renderer.di.RendererComponent;

import javax.inject.Inject;

public class RendererRouter {
	private RendererComponent rendererComponent;

	private final RendererInteractor interactor;

	private CameraSourceRouter childCameraSourceRouter;
	private GalleryRouter childGalleryRouter;

	@Inject
	public RendererRouter(RendererInteractor interactor) {
		this.interactor = interactor;
	}

	public void start() {
		interactor.attachRouter(this);
		interactor.start();
	}

	public void attachCameraSource() {
//		CameraSourceBuilder builder = new CameraSourceBuilder();
//		childCameraSourceRouter = builder.build(rendererComponent);
//
//		childCameraSourceRouter.start();

		GalleryBuilder galleryBuilder = new GalleryBuilder();
		childGalleryRouter = galleryBuilder.build(rendererComponent);

		childGalleryRouter.start();
	}

	public void setComponent(RendererComponent rendererComponent) {
		this.rendererComponent = rendererComponent;
	}
}
