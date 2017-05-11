package com.adamlastowski.rib.camera_source;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.inject.Inject;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

public class CameraRenderer {
	Resources resources;

	boolean isInitialized = false;

	private static final float[] TEXTURE_VERTICES = {
			0, 0,
			1, 0,
			0, 1,
			1, 1
	};
	static final FloatBuffer TEXTURE_BUFFER = createFloatBuffer(TEXTURE_VERTICES);
	private static final float[] VERTICES = {
			-1, -1,
			1, -1,
			-1, 1,
			1, 1,
	};
	static final FloatBuffer VERTEX_BUFFER = createFloatBuffer(VERTICES);
	private int program;
	private int positionHandle;
	private int coordHandle;
	private int drawTexture;

	public static FloatBuffer createFloatBuffer(final float[] coords) {
		final ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		final FloatBuffer fb = bb.asFloatBuffer();
		fb.put(coords);
		fb.position(0);
		return fb;
	}

	public static int createProgram(final String vertexSource, final String fragmentSource) {
		final int vertexShader = loadShader(GL_VERTEX_SHADER, vertexSource);
		final int pixelShader = loadShader(GL_FRAGMENT_SHADER, fragmentSource);
		final int program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, pixelShader);
		glLinkProgram(program);
		glDeleteShader(pixelShader);
		glDeleteShader(vertexShader);
		return program;
	}

	private static int loadShader(final int shaderType, final String source) {
		final int shader = glCreateShader(shaderType);
		glShaderSource(shader, source);
		glCompileShader(shader);
		return shader;
	}

	public static final String FRAGMENT = "precision highp float;\n"
			+ "varying vec2 vTextureCoord;\n"
			+ "uniform sampler2D uInputTexture;\n"
			+ "void main() {\n"
			+ "    gl_FragColor = texture2D(uInputTexture, vTextureCoord);\n"
			+ "}";

	public static final String VERTEX = "precision highp float;\n"
			+ "attribute vec4 aPosition;\n"
			+ "attribute vec4 aInputTextureCoordinate;\n"
			+ "varying vec2 vTextureCoord;\n"
			+ "\n"
			+ "void main() {\n"
			+ "    gl_Position = aPosition;\n"
			+ "    vTextureCoord = vec2(aInputTextureCoordinate.s, aInputTextureCoordinate.t);\n"
			+ "}";

	@Inject
	CameraRenderer(Resources resources) {
		this.resources = resources;
	}

	void initialize() {
		program = createProgram(VERTEX, FRAGMENT);

		glUseProgram(program);

		positionHandle = glGetAttribLocation(program, "aPosition");
		coordHandle = glGetAttribLocation(program, "aInputTextureCoordinate");

		glEnableVertexAttribArray(positionHandle);
		glEnableVertexAttribArray(coordHandle);

		int textureLocation = glGetUniformLocation(program, "uInputTexture");
		glUniform1i(textureLocation, 0);

		int[] textures = new int[1];
		glGenTextures(1, textures, 0);
		drawTexture = textures[0];

		glBindTexture(GL_TEXTURE_2D, drawTexture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		isInitialized = true;
	}

	void setBitmap(Bitmap bitmap) {
		glBindTexture(GL_TEXTURE_2D, drawTexture);
		GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
	}

	void onDraw() {
		glUseProgram(program);

		glVertexAttribPointer(positionHandle, 2, GL_FLOAT, false, 0, VERTEX_BUFFER);
		glVertexAttribPointer(coordHandle, 2, GL_FLOAT, false, 0, TEXTURE_BUFFER);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, drawTexture);

		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
