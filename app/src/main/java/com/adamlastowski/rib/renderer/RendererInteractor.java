package com.adamlastowski.rib.renderer;

import com.adamlastowski.rib.commons.Renderable;
import com.adamlastowski.rib.renderer.di.RendererScope;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

@RendererScope
public class RendererInteractor implements Renderer {

	private final RendererPresenter presenter;
	private RendererRouter router;

	private CopyOnWriteArrayList<Renderable> renderables;

	@Inject
	public RendererInteractor(RendererPresenter presenter) {
		this.presenter = presenter;

		renderables = new CopyOnWriteArrayList<>();
	}

	public void onReady() {
		router.attachCameraSource();
	}

	public void onCanvasSizeChanged(final int width, final int height) {

	}

	public void onDraw() {
		for (Renderable renderable : renderables) {
			renderable.onDraw();
		}
	}

	@Override
	public void attachRenderable(Renderable renderable) {
		;
		if (!renderables.contains(renderable)) {
			renderables.add(renderable);
		}
	}

	@Override
	public void removeRendereable(Renderable renderable) {
		if (renderables.contains(renderable)) {
			renderables.remove(renderable);
		}
	}

	public void start() {
		presenter.attach(this);
	}

	public void attachRouter(RendererRouter router) {
		this.router = router;
	}
}
