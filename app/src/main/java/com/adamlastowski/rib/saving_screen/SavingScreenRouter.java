package com.adamlastowski.rib.saving_screen;

import com.adamlastowski.rib.saving_screen.di.SavingScreenComponent;

import javax.inject.Inject;

class SavingScreenRouter {
	private final SavingScreenInteractor interactor;

	private SavingScreenComponent component;

	@Inject
	public SavingScreenRouter(SavingScreenInteractor interactor) {
		this.interactor = interactor;
	}

	public void setComponent(SavingScreenComponent component) {
		this.component = component;
	}
}
