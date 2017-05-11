package com.adamlastowski.rib.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.adamlastowski.rib.R;
import com.adamlastowski.rib.activity.di.ActivityComponent;
import com.adamlastowski.rib.activity.di.ActivityModule;
import com.adamlastowski.rib.activity.di.DaggerActivityComponent;
import com.adamlastowski.rib.renderer.RendererBuilder;
import com.adamlastowski.rib.renderer.RendererRouter;

public class MainActivity extends AppCompatActivity {

	ActivityComponent component;

	private RendererRouter rootRouter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		component = DaggerActivityComponent.builder()
				.activityModule(new ActivityModule(this))
				.build();

		RendererBuilder builder = new RendererBuilder();
		rootRouter = builder.build(component);
		rootRouter.start();
	}
}
