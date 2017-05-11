package com.adamlastowski.rib.graphics;

import android.support.annotation.NonNull;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1fv;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform2f;
import static android.opengl.GLES20.glUniform2fv;
import static android.opengl.GLES20.glUniform3fv;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

public class Program {
	private int handle;
	private Map<String, Integer> attributeLocations;
	private Map<String, Integer> uniformLocations;

	public Program() {
		attributeLocations = new HashMap<>();
		uniformLocations = new HashMap<>();
	}

	public int getHandle() {
		return handle;
	}

	private void setUniformLocationForName(String uniform) {
		int location = glGetUniformLocation(handle, uniform);
		if (location == -1) {
			throw new IllegalArgumentException("Uniform " + uniform + " not present in shader");
		}
		uniformLocations.put(uniform, location);
	}

	private void setAttributeLocationForName(String attribute) {
		int location = glGetAttribLocation(handle, attribute);
		if (location == -1) {
			throw new IllegalArgumentException("Attribute " + attribute + " not present in shader");
		}
		attributeLocations.put(
				attribute,
				glGetAttribLocation(handle, attribute)
		);
	}

	public void use() {
		glUseProgram(handle);
	}

	public void release() {
		glDeleteProgram(handle);
		handle = 0;
	}

	public void enableVertexAttribArrays(String... attribs) {
		for (String attrib : attribs) {
			glEnableVertexAttribArray(attributeLocations.get(attrib));
		}
	}

	public void setUniform(String uniformName, int value) {
		glUniform1i(uniformLocations.get(uniformName), value);
	}

	public void setUniform(String uniformName, float value) {
		glUniform1f(uniformLocations.get(uniformName), value);
	}

	public void setUniform(String uniformName, float value1, float value2) {
		glUniform2f(uniformLocations.get(uniformName), value1, value2);
	}

	public void setUniform(@NonNull String uniformName, @NonNull float[] values, int length) {
		int uniformLoc = uniformLocations.get(uniformName);
		switch (length) {
			case 1:
				glUniform1fv(uniformLoc, 1, values, 0);
				break;
			case 2:
				glUniform2fv(uniformLoc, 1, values, 0);
				break;
			case 3:
				glUniform3fv(uniformLoc, 1, values, 0);
				break;
			case 4:
				glUniform4fv(uniformLoc, 1, values, 0);
				break;
		}
	}

	public void setUniformMatrix(String uniformName, float[] value) {
		glUniformMatrix4fv(uniformLocations.get(uniformName), 1, false, value, 0);
	}

	public void setVertexAttribPointer(String attribName, int size, int type, boolean normalized, int stride, FloatBuffer buffer) {
		glVertexAttribPointer(attributeLocations.get(attribName), size, type, normalized, stride, buffer);
	}

	public void setHandle(int handle) {
		this.handle = handle;
	}

	public static class Initializer {
		private final Program mProgram;
		private String[] mUniforms;
		private String mVertexShaderCode;
		private String mFragmentShaderCode;
		private String[] mAttributes;

		public Initializer(@NonNull final Program program) {
			mProgram = program;
		}

		public Initializer uniforms(String... uniforms) {
			mUniforms = uniforms;
			return this;
		}

		public Initializer attributes(String... attributes) {
			mAttributes = attributes;
			return this;
		}

		public Initializer vertexShader(String vertexShaderCode) {
			mVertexShaderCode = vertexShaderCode;
			return this;
		}

		public Initializer fragmentShader(String fragmentShaderCode) {
			mFragmentShaderCode = fragmentShaderCode;
			return this;
		}

		public void initialize() {
			if (mVertexShaderCode == null)
				throw new IllegalArgumentException("Trying to create Program without vertex shader.");
			if (mFragmentShaderCode == null)
				throw new IllegalArgumentException("Trying to create Program without fragment shader.");

			int handle = createProgram(mVertexShaderCode, mFragmentShaderCode);
			mProgram.setHandle(handle);

			for (String uniform : mUniforms) {
				mProgram.setUniformLocationForName(uniform);
			}

			for (String attribute : mAttributes) {
				mProgram.setAttributeLocationForName(attribute);
			}
			mProgram.use();
		}
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
}
