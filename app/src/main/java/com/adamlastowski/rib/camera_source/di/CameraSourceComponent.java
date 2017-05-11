package com.adamlastowski.rib.camera_source.di;

import com.adamlastowski.rib.camera_source.CameraSourceBuilder;
import com.adamlastowski.rib.renderer.di.RendererComponent;

import dagger.Component;

@CameraSourceScope
@Component(dependencies = {RendererComponent.class}, modules = {CameraSourceModule.class})
public interface CameraSourceComponent {
	void inject(CameraSourceBuilder builder);
}
