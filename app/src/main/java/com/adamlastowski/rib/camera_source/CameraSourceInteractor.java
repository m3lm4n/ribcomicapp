package com.adamlastowski.rib.camera_source;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import com.adamlastowski.rib.R;
import com.adamlastowski.rib.commons.Renderable;
import com.adamlastowski.rib.renderer.Renderer;

import javax.inject.Inject;

public class CameraSourceInteractor extends Renderable {

	private final CameraSourcePresenter presenter;
	private final Resources resources;
	private CameraRenderer cameraRenderer;
	private Renderer renderer;
	private CameraSourceRouter router;

	@Inject
	public CameraSourceInteractor(Renderer renderer, CameraRenderer cameraRenderer, CameraSourcePresenter presenter, Resources resources) {
		this.renderer = renderer;
		this.cameraRenderer = cameraRenderer;
		this.presenter = presenter;
		this.resources = resources;
	}

	public void attachRouter(CameraSourceRouter router) {
		this.router = router;
	}

	public void start() {
		presenter.attach(this);
		changeImage(R.drawable.drops);
		renderer.attachRenderable(this);
	}

	@Override
	public void draw() {
		if (!cameraRenderer.isInitialized) {
			cameraRenderer.initialize();
		}

		cameraRenderer.onDraw();
	}

	public void showDropsImage() {
		changeImage(R.drawable.drops);
	}

	public void showRootsImage() {
		changeImage(R.drawable.roots);
	}

	public void changeImage(@DrawableRes final int image) {

		executeOnRenderThread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = BitmapFactory.decodeResource(resources, image);
				cameraRenderer.setBitmap(bitmap);
				bitmap.recycle();
			}
		});
	}
}
