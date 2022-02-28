package opengdl.core;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL15.*;

import opengdl.render.Camera;
import opengdl.shader.Shader;
import vecutils.Matrix;
import vecutils.Vector2;
import vecutils.Vector3;
import vecutils.Vector4;

public abstract class Mesh {
	public static final Quad quad = new Quad();
	public static final Cube cube = new Cube();
	public static final Sphere sphere = new Sphere();
	public static final Terrain terrain = new Terrain(null);
	public Vector3[] vertices;
	public Vector4[] colors;
	public Vector2[] uvs;
	private int mode;
	Random ran = new Random();
	public Mesh(int mode) {
		this.mode = mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public int getMode() {
		return this.mode;
	}
	public void renderVBO(Vector3 pos, Vector3 scale, Vector4 rotation, int vertices,
			int vbo_vertex_handle, int vbo_color_handle, int vbo_tex_handle, int vertex_size,
			int color_size, int tex_size, Texture texture, boolean renderInSpace) {
		initRender(pos, scale, rotation, renderInSpace);
		
		if (texture != null) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.textureHandle);
		}
		glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle);
		GL11.glVertexPointer(vertex_size, GL11.GL_FLOAT, 0, 0l);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_color_handle);
		GL11.glColorPointer(color_size, GL11.GL_FLOAT, 0, 0l);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo_tex_handle);
		GL11.glTexCoordPointer(tex_size, GL11.GL_FLOAT, 0, 0l);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		GL11.glDrawArrays(mode, 0, vertices);

		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		if (texture != null)
			GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	private void initRender(Vector3 pos, Vector3 scale, Vector4 rotation, boolean renderInSpace) {
		if (renderInSpace)
			Camera.instance.lookThrough();
		
		GL11.glTranslatef(pos.x, -pos.y, pos.z);
		GL11.glRotatef(rotation.t, rotation.x, rotation.y, rotation.z);
		GL11.glScalef(scale.x, scale.y, scale.z);
	}
	public void render(Vector3 pos, Vector3 scale, Vector4 rotation, Texture tex,
			Shader shader, boolean renderInSpace) {
		initRender(pos, scale, rotation, renderInSpace);
		
		if (tex != null) {
			GL11.glBindTexture(3553, tex.textureHandle);
			GL11.glEnable(3553);
		}
		if (shader.renderTransparent) {
			GL11.glEnable(shader.alphaRenderMode);
			if (shader.alphaRenderMode == GL11.GL_ALPHA_TEST)
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			if (shader.alphaRenderMode == GL11.GL_BLEND)
				GL11.glBlendFunc(770, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		GL11.glBegin(mode);
		if (colors.length == 1)
			GL11.glColor4f(colors[0].x, colors[0].y, colors[0].z, colors[0].t);
        for (int i = 0; i < vertices.length; i++) {
        	if (colors.length != 1 && colors[i] != null)
    			GL11.glColor4f(colors[i].x, colors[i].y, colors[i].z, colors[i].t);
        	if (uvs[i] != null)
        		GL11.glTexCoord2f(uvs[i].x, uvs[i].y);
        	if (vertices[i] != null)
        		GL11.glVertex3f(vertices[i].x, vertices[i].y, vertices[i].z);
        }
        GL11.glEnd();
        GL11.glDisable(shader.alphaRenderMode);
        if (tex != null)
        	GL11.glDisable(3553);
	}
}
