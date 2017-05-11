package com.adamlastowski.rib.gallery.di;

import com.adamlastowski.rib.gallery.GalleryBuilder;
import com.adamlastowski.rib.renderer.di.RendererComponent;

import dagger.Component;

/**
 * Created by adamlastowski on 19/04/2017.
 */

@GalleryScope
@Component(dependencies = {RendererComponent.class}, modules = {GalleryModule.class})
public interface GalleryComponent {
	void inject(GalleryBuilder builder);
}
