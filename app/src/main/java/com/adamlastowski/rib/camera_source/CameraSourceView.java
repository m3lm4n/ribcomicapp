package com.adamlastowski.rib.camera_source;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.adamlastowski.rib.R;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraSourceView {

	private final RelativeLayout rootView;
	private CameraSourcePresenter presenter;

	private View controlsView;

	@Inject
	public CameraSourceView(@Named("rootView") RelativeLayout rootView) {
		this.rootView = rootView;
	}

	public void attach(CameraSourcePresenter presenter) {
		this.presenter = presenter;
	}

	public void layOutViews() {
		controlsView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.camera_source_view, rootView, false);
		ButterKnife.bind(this, controlsView);

		rootView.addView(controlsView);
	}

	@OnClick(R.id.drops_button)
	public void onDropsButtonClicked() {
		presenter.dropsClicked();
	}

	@OnClick(R.id.roots_button)
	public void onRootsButtonClicked() {
		presenter.rootsClicked();
	}
}
