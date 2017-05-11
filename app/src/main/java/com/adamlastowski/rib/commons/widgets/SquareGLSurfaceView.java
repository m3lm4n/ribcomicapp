package com.adamlastowski.rib.commons.widgets;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class SquareGLSurfaceView extends GLSurfaceView {
	public SquareGLSurfaceView(final Context context) {
		super(context);
	}

	public SquareGLSurfaceView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressWarnings("SuspiciousNameCombination")
	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}
