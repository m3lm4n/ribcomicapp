package com.adamlastowski.rib.saving_screen;

import com.adamlastowski.rib.renderer.di.RendererComponent;
import com.adamlastowski.rib.saving_screen.di.DaggerSavingScreenComponent;
import com.adamlastowski.rib.saving_screen.di.SavingScreenComponent;

import javax.inject.Inject;

public class SavingScreenBuilder {

	@Inject
	SavingScreenRouter router;

	public SavingScreenRouter build(RendererComponent rendererComponent) {
		SavingScreenComponent component = DaggerSavingScreenComponent.builder()
				.rendererComponent(rendererComponent)
				.build();

		component.inject(this);

		router.setComponent(component);
		return router;
	}

}
