package com.adamlastowski.rib.gallery.player;

import android.graphics.SurfaceTexture;
import android.opengl.Matrix;

import com.adamlastowski.rib.graphics.Program;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES10.GL_RGB;
import static android.opengl.GLES10.GL_TEXTURE0;
import static android.opengl.GLES10.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES10.glActiveTexture;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glDeleteTextures;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES10.glTexImage2D;
import static android.opengl.GLES11.glTexParameteri;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glTexParameterf;

/**
 * Created by adamlastowski on 19/04/2017.
 */

public class DecoderRenderer {
	private static final int FLOAT_SIZE_BYTES = 4;
	private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
	private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
	private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;

	private static final String A_TEXTURE_COORD = "aTextureCoord";
	private static final String A_POSITION = "aPosition";
	private static final String U_PROJECTION_MATRIX = "uProjection";
	private static final String U_MODEL_MATRIX = "uModel";
	private static final String U_ST_MATRIX = "uSTMatrix";
	private static final String U_TEXTURE = "uTexture";
	private static final int INVALID_TEXTURE_ID = -12345;

	private static final float[] TRIANGLE_VERTICES_DATA = {
			// X, Y, Z, U, V
			-1f, -1.0f, 0, 0.0f, 0.0f,
			1f, -1.0f, 0, 1.0f, 0.0f,
			-1f, 1.0f, 0, 0.0f, 1.0f,
			1f, 1.0f, 0, 1.0f, 1.0f,
	};
	private static final int OUTPUT_WIDTH = 640;
	private static final int OUTPUT_HEIGHT = 640;
	private static final int INVALID_GL_RESOURCE_ID = -1;

	private final FloatBuffer triangleVerticesBuffer;

	private static final String VERTEX_SHADER = "uniform mat4 uSTMatrix;\n"
			+ "uniform mat4 uModel;\n"
			+ "uniform mat4 uProjection;\n"
			+ "attribute vec4 aPosition;\n"
			+ "attribute vec4 aTextureCoord;\n"
			+ "varying vec2 vTextureCoord;\n"
			+ "void main() {\n"
			+ "    gl_Position = uProjection * uModel * aPosition;\n"
			+ "    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n"
			+ "}";

	private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\n"
			+ "precision mediump float;\n"
			+ "varying vec2 vTextureCoord;\n"
			+ "uniform sampler2D uTexture;\n"
			+ "void main() {\n"
			+ "    gl_FragColor = texture2D(uTexture, vTextureCoord);\n"
			+ "}";

	private final float[] surfaceTextureTransformMatrix = new float[16];
	private final float[] defaultModelMatrix = new float[16];
	private final float[] defaultProjectionMatrix = new float[16];

	private final Program program;

	int textureId;

	public DecoderRenderer() {
		triangleVerticesBuffer = ByteBuffer.allocateDirect(
				TRIANGLE_VERTICES_DATA.length * FLOAT_SIZE_BYTES)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		triangleVerticesBuffer.put(TRIANGLE_VERTICES_DATA).position(0);

		Matrix.setIdentityM(surfaceTextureTransformMatrix, 0);

		Matrix.setIdentityM(defaultProjectionMatrix, 0);
		Matrix.orthoM(defaultProjectionMatrix, 0, 0, OUTPUT_WIDTH, OUTPUT_HEIGHT, 0, -1, 1);

		Matrix.setIdentityM(defaultModelMatrix, 0);
		Matrix.translateM(defaultModelMatrix, 0, OUTPUT_WIDTH / 2, OUTPUT_HEIGHT / 2, 0);
		Matrix.scaleM(defaultModelMatrix, 0, OUTPUT_WIDTH / 2, -OUTPUT_HEIGHT / 2, 0);

		textureId = INVALID_TEXTURE_ID;

		program = new Program();
	}

	public void setFlip(boolean shouldFlip) {
		int flip = shouldFlip ? -1 : 1;

		Matrix.setIdentityM(defaultModelMatrix, 0);
		Matrix.translateM(defaultModelMatrix, 0, OUTPUT_WIDTH / 2, OUTPUT_HEIGHT / 2, 0);
		Matrix.scaleM(defaultModelMatrix, 0, OUTPUT_WIDTH / 2, flip * OUTPUT_HEIGHT / 2, 0);
	}

	public int getTextureId() {
		return textureId;
	}

	public void drawFrame(SurfaceTexture st, int width, int height) {
		drawFrame(st, width, height, defaultProjectionMatrix, defaultModelMatrix, null);
	}

	public void drawFrame(int textureId, int width, int height) {
		drawFrame(textureId, width, height, defaultProjectionMatrix, defaultModelMatrix, null);
	}

	/**
	 * Draws the external texture in SurfaceTexture onto the current EGL surface.
	 */
	public void drawFrame(SurfaceTexture st, int width, int height, float[] projectionMatrix, float[] modelMatrix, float[] textureTransformMatrix) {
		st.getTransformMatrix(surfaceTextureTransformMatrix);

		if (textureTransformMatrix != null) {
			Matrix.multiplyMM(surfaceTextureTransformMatrix, 0, surfaceTextureTransformMatrix, 0, textureTransformMatrix, 0);
		}

		program.use();

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(getTextureTarget(), textureId);

		triangleVerticesBuffer.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
		program.setVertexAttribPointer(A_POSITION, 3, GL_FLOAT, false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVerticesBuffer);
		triangleVerticesBuffer.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
		program.setVertexAttribPointer(A_TEXTURE_COORD, 2, GL_FLOAT, false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVerticesBuffer);

		program.enableVertexAttribArrays(A_POSITION, A_TEXTURE_COORD);

		program.setUniformMatrix(U_ST_MATRIX, surfaceTextureTransformMatrix);
		program.setUniformMatrix(U_MODEL_MATRIX, modelMatrix);
		program.setUniformMatrix(U_PROJECTION_MATRIX, projectionMatrix);

		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

		glBindTexture(getTextureTarget(), 0);
	}

	public void drawFrame(int texture, int width, int height, float[] projectionMatrix, float[] modelMatrix, float[] textureTransformMatrix) {
		program.use();

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(getTextureTarget(), texture);

		triangleVerticesBuffer.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
		program.setVertexAttribPointer(A_POSITION, 3, GL_FLOAT, false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVerticesBuffer);
		triangleVerticesBuffer.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
		program.setVertexAttribPointer(A_TEXTURE_COORD, 2, GL_FLOAT, false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVerticesBuffer);

		program.enableVertexAttribArrays(A_POSITION, A_TEXTURE_COORD);

		program.setUniformMatrix(U_ST_MATRIX, surfaceTextureTransformMatrix);
		program.setUniformMatrix(U_MODEL_MATRIX, modelMatrix);
		program.setUniformMatrix(U_PROJECTION_MATRIX, projectionMatrix);

		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

		glBindTexture(getTextureTarget(), 0);
	}

	int getTextureTarget() {
		return GL_TEXTURE_2D;
	}

	String getFragmentShader() {
		return FRAGMENT_SHADER;
	}

	/**
	 * Initializes GL state.  Call this after the EGL surface has been created and made current.
	 */
	public void surfaceCreated() {
		new Program.Initializer(program)
				.vertexShader(VERTEX_SHADER)
				.fragmentShader(getFragmentShader())
				.attributes(A_TEXTURE_COORD, A_POSITION)
				.uniforms(U_ST_MATRIX, U_TEXTURE, U_MODEL_MATRIX, U_PROJECTION_MATRIX)
				.initialize();

		deleteTexture();
		createNewTexture();
	}

	void createNewTexture() {
		int[] textures = new int[1];
		glGenTextures(1, textures, 0);

		textureId = textures[0];
		glBindTexture(getTextureTarget(), textureId);

		glTexParameterf(getTextureTarget(), GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(getTextureTarget(), GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(getTextureTarget(), GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(getTextureTarget(), GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(getTextureTarget(), 0, GL_RGB, OUTPUT_WIDTH, OUTPUT_HEIGHT, 0, GL_RGB, GL_UNSIGNED_BYTE, null);
	}

	public int getNewTexture() {
		if (textureId == INVALID_TEXTURE_ID) {
			createNewTexture();
		}
		return textureId;
	}

	public void release() {
		program.release();
		deleteTexture();
	}

	public void deleteTexture() {
		glDeleteTextures(1, new int[]{textureId}, 0);
		textureId = INVALID_GL_RESOURCE_ID;
	}


}
