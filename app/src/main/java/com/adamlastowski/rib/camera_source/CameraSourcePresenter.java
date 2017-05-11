package com.adamlastowski.rib.camera_source;

import javax.inject.Inject;

public class CameraSourcePresenter {

	CameraSourceInteractor interactor;
	CameraSourceView view;

	@Inject
	public CameraSourcePresenter(CameraSourceView view) {
		this.view = view;
	}

	public void attach(CameraSourceInteractor interactor) {
		this.interactor = interactor;

		view.attach(this);
		view.layOutViews();
	}

	public void dropsClicked() {
		interactor.showDropsImage();
	}

	public void rootsClicked() {
		interactor.showRootsImage();
	}
}
