package com.adamlastowski.rib.camera_source;

import com.adamlastowski.rib.camera_source.di.CameraSourceComponent;

import javax.inject.Inject;

public class CameraSourceRouter {
	private CameraSourceComponent cameraSourceComponent;

	CameraSourceInteractor interactor;

	@Inject
	public CameraSourceRouter(CameraSourceInteractor interactor) {
		this.interactor = interactor;
	}

	public void start() {
		interactor.attachRouter(this);
		interactor.start();
	}

	public void attachComponent(CameraSourceComponent component) {
		cameraSourceComponent = component;
	}
}
