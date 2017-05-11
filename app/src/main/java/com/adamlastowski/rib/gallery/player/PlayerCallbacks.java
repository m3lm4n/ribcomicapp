package com.adamlastowski.rib.gallery.player;

import android.graphics.SurfaceTexture;

public interface PlayerCallbacks {
	void onVideoSizeChanged(int width, int height);

	void onVideoEnded();

	void onFrameReady(SurfaceTexture surfaceTexture);
}
