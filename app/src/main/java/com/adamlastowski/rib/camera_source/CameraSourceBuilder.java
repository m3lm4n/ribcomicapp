package com.adamlastowski.rib.camera_source;

import com.adamlastowski.rib.camera_source.di.CameraSourceComponent;
import com.adamlastowski.rib.camera_source.di.DaggerCameraSourceComponent;
import com.adamlastowski.rib.renderer.di.RendererComponent;

import javax.inject.Inject;

public class CameraSourceBuilder {

	@Inject
	CameraSourceRouter router;

	public CameraSourceRouter build(RendererComponent rendererComponent) {
		CameraSourceComponent component = DaggerCameraSourceComponent.builder()
				.rendererComponent(rendererComponent)
				.build();

		component.inject(this);

		router.attachComponent(component);
		return router;
	}
}
