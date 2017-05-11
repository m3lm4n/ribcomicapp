package com.adamlastowski.rib.graphics;

import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.support.annotation.NonNull;

/**
 * Created by adamlastowski on 19/04/2017.
 */

public class GLContextContainer {
	@NonNull
	private final EGLDisplay display;
	private final EGLContext context;
	private final EGLSurface surface;

	public GLContextContainer(@NonNull final EGLDisplay display, final EGLContext context, final EGLSurface surface) {
		this.display = display;
		this.context = context;
		this.surface = surface;
	}

	public void makeCurrent() {
		EGL14.eglMakeCurrent(display, surface, surface, context);
	}

	void destroy() {
		EGL14.eglDestroyContext(display, context);
		EGL14.eglDestroySurface(display, surface);
	}

	boolean hasCurrentDisplay() {
		return display.equals(EGL14.eglGetCurrentDisplay());
	}
}
