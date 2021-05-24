package main;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class GLBuilder {
    public static GLBuilder builder = new GLBuilder();

    public GLBuilder mode(int mode) {
        GL11.glBegin(mode);
        return this;
    }

    public GLBuilder vertex(float x, float y) {
        GL11.glVertex2f(x, y);
        return this;
    }

    public GLBuilder color(int r, int g, int b, int a) {
        GL11.glColor4f(r/255f, g/255f, b/255f, a/255f);
        return this;
    }

    public GLBuilder color(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
        return this;
    }

    public GLBuilder color(int rgb, float a) {
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;

        GL11.glColor4f(r / 255f, g / 255f, b / 255f, a);
        return this;
    }

    public void end() {
        GL11.glEnd();
    }

    public static GLBuilder getBuilder() {
        return builder;
    }
}
