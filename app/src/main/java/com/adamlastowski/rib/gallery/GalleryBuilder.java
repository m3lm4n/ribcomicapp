package com.adamlastowski.rib.gallery;

import com.adamlastowski.rib.gallery.di.DaggerGalleryComponent;
import com.adamlastowski.rib.gallery.di.GalleryComponent;
import com.adamlastowski.rib.renderer.di.RendererComponent;

import javax.inject.Inject;

/**
 * Created by adamlastowski on 19/04/2017.
 */

public class GalleryBuilder {
	@Inject
	GalleryRouter router;

	public GalleryRouter build(RendererComponent rendererComponent) {
		GalleryComponent component = DaggerGalleryComponent.builder()
				.rendererComponent(rendererComponent)
				.build();

		component.inject(this);

		router.setGalleryComponent(component);
		return router;
	}

}
