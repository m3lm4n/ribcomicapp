package com.adamlastowski.rib.renderer;

import com.adamlastowski.rib.commons.Renderable;

public interface Renderer {
	public void attachRenderable(Renderable renderable);
	public void removeRendereable(Renderable renderable);
}
