package com.adamlastowski.rib.gallery;

import com.adamlastowski.rib.gallery.di.GalleryComponent;

import javax.inject.Inject;

/**
 * Created by adamlastowski on 19/04/2017.
 */

public class GalleryRouter {
	private final GalleryInteractor interactor;
	private GalleryComponent galleryComponent;

	@Inject
	public GalleryRouter(GalleryInteractor interactor) {
		this.interactor = interactor;
	}

	public void start() {
		interactor.attachRouter(this);
		interactor.start();
	}

	public void setGalleryComponent(GalleryComponent galleryComponent) {
		this.galleryComponent = galleryComponent;
	}
}
