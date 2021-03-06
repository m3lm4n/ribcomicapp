package com.adamlastowski.rib.gallery;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.opengl.EGL14;
import android.util.Log;

import com.adamlastowski.rib.commons.Renderable;
import com.adamlastowski.rib.gallery.player.DecoderRenderer;
import com.adamlastowski.rib.gallery.player.PlayerCallbacks;
import com.adamlastowski.rib.gallery.player.PlayerWrapper;
import com.adamlastowski.rib.graphics.GLContextContainer;
import com.adamlastowski.rib.renderer.Renderer;

import java.io.File;

import javax.inject.Inject;

public class GalleryInteractor extends Renderable implements PlayerCallbacks {

	private static final String TAG = "GALLERY";
	private final GalleryPresenter presenter;
	private final Context context;
	private final Renderer renderer;
	private GalleryRouter router;
	private PlayerWrapper player;
	private DecoderRenderer decoderRenderer;

	@Inject
	public GalleryInteractor(GalleryPresenter presenter, Context context, Renderer renderer) {
		this.presenter = presenter;
		this.context = context;
		this.renderer = renderer;
	}

	public void onReady() {
		Log.d("TAG", "onReady: ");
	}

	public void attachRouter(GalleryRouter galleryRouter) {
		this.router = galleryRouter;
	}

	public void start() {
		presenter.attach(this);

		GLContextContainer contextContainer = new GLContextContainer(
				EGL14.eglGetCurrentDisplay(),
				EGL14.eglGetCurrentContext(),
				EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW)
		);

		decoderRenderer = new DecoderRenderer();
		decoderRenderer.surfaceCreated();
		decoderRenderer.setFlip(false);

		executeOnRenderThread(() -> {
//			player = new PlayerWrapper(contextContainer, this, decoderRenderer);
//
//			File file = new File("/sdcard/yakuza.mp4");
//			player.prepare(Uri.fromFile(file));
//
//			player.start();
		});
	}

	@Override
	public void draw() {

	}

	@Override
	public void onVideoSizeChanged(int width, int height) {
		Log.d(TAG, "onVideoSizeChanged: ");
	}

	@Override
	public void onVideoEnded() {
		Log.d(TAG, "onVideoEnded: ");
	}

	@Override
	public void onFrameReady(SurfaceTexture surfaceTexture) {
		Log.d(TAG, "onFrameReady: ");
	}
}
