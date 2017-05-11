package com.adamlastowski.rib.gallery.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.view.Surface;

import com.adamlastowski.rib.graphics.GLContextContainer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.GalleryExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class PlayerWrapper {
	@Inject
	Context appContext;

	private GalleryExoPlayer player;
	private Handler handler;

	private DefaultDataSourceFactory dataSourceFactory;
	private DefaultExtractorsFactory defaultExtractorsFactory;
	private ExtractorMediaSource.EventListener sourceEventListener;
	private MediaSource.Listener mediaSourcePrepareListener;
	private final GLContextContainer glContextContainer;

	private PlayerCallbacks playerCallbacks;
	private SurfaceTexture surfaceTexture;
	private Surface surface;

	private boolean isStopped;

	private SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {
		@Override
		public void onFrameAvailable(SurfaceTexture surfaceTexture) {
			if (surface.isValid() && !isStopped) {
				glContextContainer.makeCurrent();

				try {
					surfaceTexture.updateTexImage();
					playerCallbacks.onFrameReady(surfaceTexture);
				} catch (RuntimeException ex) {
					registerVideoSurface(textureHandle);
				}
			}
		}
	};

	private ExtractorMediaSource videoSource;

	private DecoderRenderer renderer;
	private long pendingSeekPosition;

	private int textureHandle;

	public PlayerWrapper(final GLContextContainer glContextContainer, final PlayerCallbacks playerCallbacks, final DecoderRenderer renderer) {
		this.playerCallbacks = playerCallbacks;
		this.glContextContainer = glContextContainer;
		this.handler = new Handler();
		this.renderer = renderer;
		this.textureHandle = renderer.getNewTexture();
		createPlayer(textureHandle);
	}

	public void prepare(Uri mediaUri) {
		videoSource = new ExtractorMediaSource(mediaUri,
				dataSourceFactory,
				defaultExtractorsFactory,
				handler,
				sourceEventListener
		);

		videoSource.prepareSource(player, true, mediaSourcePrepareListener);
	}

	private void createPlayer(int textureHandle) {
		player = new GalleryExoPlayer(
				appContext,
				new DefaultTrackSelector(),
				new DefaultLoadControl(),
				null,
				GalleryExoPlayer.EXTENSION_RENDERER_MODE_OFF,
				ExoPlayerFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS
		);

		dataSourceFactory = new DefaultDataSourceFactory(
				appContext, "Gallery");
		defaultExtractorsFactory = new DefaultExtractorsFactory();

		sourceEventListener = this::postPlaybackError;

		mediaSourcePrepareListener = (timeline, manifest) -> {/* no-op */};

		this.textureHandle = textureHandle;

		registerVideoSizeChangedListener();
		registerVideoEndListener();
		registerVideoSurface(textureHandle);
	}

	private void registerVideoSizeChangedListener() {
		player.setVideoListener(new GalleryExoPlayer.VideoListener() {
			@Override
			public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
				playerCallbacks.onVideoSizeChanged(width, height);
			}

			@Override
			public void onRenderedFirstFrame() {
				//no-op
			}
		});
	}

	private void registerVideoEndListener() {
		player.addListener(new ExoListenerAdapter() {
			@Override
			public void onPlayerError(final ExoPlaybackException error) {
				postPlaybackError(error);
				release();
				createPlayer(textureHandle);
			}

			@Override
			public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
				PlayerWrapper.this.onPlayerStateChanged(playbackState, playWhenReady);
			}
		});
	}

	private void postPlaybackError(Exception error) {
//		eventBus.post(new VideoPlaybackErrored(error));
	}

	private void onPlayerStateChanged(int playbackState, boolean isPlayWhenReady) {
		switch (playbackState) {
			case ExoPlayer.STATE_ENDED:
				if (isPlayWhenReady) {
					playerCallbacks.onVideoEnded();
				}
				break;
			case ExoPlayer.STATE_READY:
				if (pendingSeekPosition != 0) {
					player.seekTo(pendingSeekPosition);
					pendingSeekPosition = 0;
				}
				if (!isPlayWhenReady) {
					player.setPlayWhenReady(true);
				}
				break;
			default:
				break;
		}
	}

	long getSurfaceTimestampNanos() {
		return TimeUnit.MILLISECONDS.toNanos(player.getCurrentPosition());
	}

	void registerVideoSurface(int textureHandle) {
		if (surface != null) {
			surfaceTexture.release();
			surface.release();
		}

		surfaceTexture = new SurfaceTexture(textureHandle);
		surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);

		surface = new Surface(surfaceTexture);
		player.setVideoSurface(surface);
	}

	public void start() {
		player.prepare(videoSource);
		player.setPlayWhenReady(true);
		isStopped = false;
	}

	public void stop() {
		player.setPlayWhenReady(false);
		isStopped = true;
	}

	public void release() {
		surfaceTexture.release();
		surface.release();
		player.release();
		player = null;
	}

	public void seekTo(long nanoTime) {
		player.seekTo(TimeUnit.NANOSECONDS.toMillis(nanoTime));
	}

	public long getDurationNanos() {
		return TimeUnit.MILLISECONDS.toNanos(player.getDuration());
	}

	public void seekToFraction(float positionFraction) {
		pendingSeekPosition = (long) (positionFraction * (float) player.getDuration());
		start();
	}

	public void pause() {
		player.setPlayWhenReady(false);
		isStopped = true;
	}

}

