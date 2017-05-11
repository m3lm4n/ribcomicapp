package com.adamlastowski.rib.saving_screen.di;

import com.adamlastowski.rib.renderer.di.RendererComponent;
import com.adamlastowski.rib.saving_screen.SavingScreenBuilder;

import dagger.Component;

@SavingScreenScope
@Component(dependencies = {RendererComponent.class}, modules = {SavingScreenModule.class})
public interface SavingScreenComponent {
	void inject(SavingScreenBuilder savingScreenBuilder);
}
