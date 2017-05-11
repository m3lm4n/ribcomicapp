package com.adamlastowski.rib.renderer;

import com.adamlastowski.rib.activity.di.ActivityComponent;
import com.adamlastowski.rib.renderer.di.DaggerRendererComponent;
import com.adamlastowski.rib.renderer.di.RendererComponent;

import javax.inject.Inject;

public class RendererBuilder {
	@Inject
	RendererRouter router;

	public RendererRouter build(ActivityComponent activityComponent) {
		RendererComponent component = DaggerRendererComponent.builder()
				.activityComponent(activityComponent)
				.build();

		component.inject(this);

		router.setComponent(component);
		return router;
	}
}
