package engine.rendering;

import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import api.vulkan.Vertex;
import math.Constants;
import util.Util;

public class Shape {
	//TODO: Figure out why the radius breaks it
	public static Vertex[] generateSphereVertices(float radius, int vdiv, int hdiv) {
		int total = vdiv * hdiv + 2;
		Vertex top = new Vertex(0, radius, 0, 0, 1, 0, 0, 0);
		Vertex bot = new Vertex(0, -radius, 0, 0, -1, 0, 1, 0);
		Vertex[] vertices = new Vertex[total];
		//Stepping around the y-axis
		float hstep = 360f / (vdiv) * Constants.RADIAN;
		//Stepping around the x-axis
		float vstep = (180f / (hdiv + 1)) * Constants.RADIAN; //Fix, should subtract after calc not before
		vertices[0] = top;
		vertices[total - 1] = bot;
		//Texture Coordinates
		float sStep = 1.0f / (hdiv + 2);
		float tStep = 1.0f / vdiv;
		
		for(int i = 0; i < vdiv; i++) {
			for(int j = 1; j < hdiv + 1; j++) {
				float x = (float) (Math.cos(i * hstep) * Math.sin(j * vstep));
				//Swapped Y and Z axes
				float z = (float) (Math.sin(i * hstep) * Math.sin(j * vstep));
				float y = (float) (Math.cos(j * vstep));
				//float length = 1.0f / (float) Math.sqrt(x * x + y * y + z * z);//Attempt to fix normals, I don't think it worked
				vertices[j + i * hdiv] = new Vertex(radius * x, radius * y, radius * z, x, y, z, sStep * j, tStep * i);
			}
		}
		return vertices;
	}
	public static IntBuffer generateSphereIndices(int vdiv, int hdiv) {
		int numIndices = 6 * vdiv * hdiv;
		int total = vdiv * hdiv + 2;
		IntBuffer indices = MemoryUtil.memAllocInt(numIndices);
		for(int i = 0; i < vdiv; i++) {
			int x1 = 1 + i * hdiv, y1 = i != vdiv - 1 ? 1 + i * hdiv + hdiv : 1;
			indices.put(0).put(y1).put(x1); //Top triangle
			for(int j = 0; j < hdiv - 1; j++) {
				indices.put(x1).put(y1 + 1).put(x1 + 1);
				indices.put(x1).put(y1).put(y1 + 1);
				x1++; y1++;
			}
			indices.put(x1).put(y1).put(total - 1); //Bot Triangle
		}
		indices.flip();
		
		//Util.printIntBuffer(indices);
		return indices;
	}
}
