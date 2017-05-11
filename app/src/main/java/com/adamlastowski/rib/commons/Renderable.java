package com.adamlastowski.rib.commons;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Renderable {
	Queue<Runnable> runnableQueue = new LinkedList<>();

	public final void executeOnRenderThread(Runnable runnable) {
		runnableQueue.add(runnable);
	}

	public final void onDraw() {
		draw();

		if(runnableQueue.peek() != null) {
			runnableQueue.poll().run();
		}
	}

	public abstract void draw();
}
