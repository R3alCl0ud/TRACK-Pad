package xyz.neptunetm.trackpad;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class DrawModeA extends AbstractDrawMode {
    public static float left_control = -0.1f; // padding from the horizontal center
    public static float right_control = 0.1f; // padding from the horizontal center
    public static float display_padding = 0.1f; // padding for edge of screen

    public DrawModeA(Config cfg) {
        super(cfg);
    }

    private void setDrawColor(int color) {
        setDrawColor(color, 1f);
    }

    private void setDrawColor(int color, float alpha) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;

        GL11.glColor4f(r / 255f, g / 255f, b / 255f, alpha);
    }

    @Override
    public void drawSteering(float steering) {
        // Begin draw left arrow
        GL11.glBegin(GL11.GL_TRIANGLES);
        setDrawColor(config.steeringColor);
        glVertex2f(left_control, -1f + display_padding * 4);
        float leftWidth = (-1f + display_padding - left_control);
        glVertex2f(Math.min(-leftWidth * steering + left_control, left_control), 0);
        glVertex2f(left_control, 1f - display_padding * 4);
        GL11.glEnd();

        GL11.glBegin(GL_LINES);
        setDrawColor(config.steeringColor);
        glVertex2f(-1f + display_padding, 0);
        glVertex2f(left_control, -1f + display_padding * 4);

        glVertex2f(-1f + display_padding, 0);
        glVertex2f(left_control, 1f - display_padding * 4);

        glVertex2f(left_control, -1f + display_padding * 4);
        glVertex2f(left_control, 1f - display_padding * 4);

        // setDrawColor(0x00000, .5f);
        if (config.controls.isAnalogueSteering) {
            //50%
            glColor4f(60 / 255f, 60 / 255f, 60 / 255f, 0.75f); // black outline at %50
            glVertex2f(leftWidth / 2f + left_control, 0);
            glVertex2f(left_control, -1f + display_padding * 4);

            glVertex2f(leftWidth / 2f + left_control, 0);
            glVertex2f(left_control, 1f - display_padding * 4);

            //25%
            glVertex2f(leftWidth / 4f + left_control, 0);
            glVertex2f(left_control, -1f + display_padding * 4);

            glVertex2f(leftWidth / 4f + left_control, 0);
            glVertex2f(left_control, 1f - display_padding * 4);

            //75%
            glVertex2f(leftWidth * 3 / 4f + left_control, 0);
            glVertex2f(left_control, -1f + display_padding * 4);

            glVertex2f(leftWidth * 3 / 4f + left_control, 0);
            glVertex2f(left_control, 1f - display_padding * 4);
        }

        GL11.glEnd();

        // begin right triangle
        GL11.glBegin(GL11.GL_TRIANGLES);
        setDrawColor(config.steeringColor);
        glVertex2f(0 + right_control, -1f + display_padding * 4);
        float rightWidth = 1f - display_padding - right_control;
        glVertex2f(Math.max(right_control, rightWidth * steering + right_control), 0);
        glVertex2f(0 + right_control, 1f - display_padding * 4);
        GL11.glEnd();


        GL11.glBegin(GL11.GL_LINES);
        setDrawColor(config.steeringColor);
        glVertex2f(1f - display_padding, 0);
        glVertex2f(0 + right_control, -1f + display_padding * 4);

        glVertex2f(1f - display_padding, 0);
        glVertex2f(0 + right_control, 1f - display_padding * 4);

        glVertex2f(0 + right_control, -1f + display_padding * 4);
        glVertex2f(0 + right_control, 1f - display_padding * 4);

        if (config.controls.isAnalogueSteering) {
            //50%
            glColor4f(60 / 255f, 60 / 255f, 60 / 255f, 0.75f); // black outline at %50
            glVertex2f(rightWidth / 2f + right_control, 0);
            glVertex2f(right_control, -1f + display_padding * 4);

            glVertex2f(rightWidth / 2f + right_control, 0);
            glVertex2f(right_control, 1f - display_padding * 4);

            //25%
            glVertex2f(rightWidth / 4f + right_control, 0);
            glVertex2f(right_control, -1f + display_padding * 4);

            glVertex2f(rightWidth / 4f + right_control, 0);
            glVertex2f(right_control, 1f - display_padding * 4);

            //75%
            glVertex2f(rightWidth * 3 / 4f + right_control, 0);
            glVertex2f(right_control, -1f + display_padding * 4);

            glVertex2f(rightWidth * 3 / 4f + right_control, 0);
            glVertex2f(right_control, 1f - display_padding * 4);
        }

        GL11.glEnd();
    }

    @Override
    public void drawGas(float gas) {
        float height = 1 - display_padding * 5 / 4f;
        float sqHeight = 1 - display_padding * 17 / 4f;
        float normalized = Math.max(0, height * gas);
        if (normalized > sqHeight) {
            GL11.glBegin(GL11.GL_TRIANGLES);
            setDrawColor(config.gasColor);
            glVertex2f(left_control, 1f - display_padding * 4f);
            glVertex2f(right_control, 1f - display_padding * 4f);
            glVertex2f(0, normalized + display_padding / 4f);
            GL11.glEnd();

            GL11.glBegin(GL_QUADS);
            setDrawColor(config.gasColor);
            glVertex2f(left_control, display_padding / 4f);
            glVertex2f(right_control, display_padding / 4f);
            glVertex2f(right_control, 1f - display_padding * 4f);
            glVertex2f(left_control, 1f - display_padding * 4f);
            GL11.glEnd();
        } else {
            GL11.glBegin(GL_QUADS);
            setDrawColor(config.gasColor);
            glVertex2f(left_control, display_padding / 4f);
            glVertex2f(right_control, display_padding / 4f);
            glVertex2f(right_control, display_padding / 4f + normalized);
            glVertex2f(left_control, display_padding / 4f + normalized);
            GL11.glEnd();
        }

        //BOX
        GL11.glBegin(GL_LINES);
        setDrawColor(config.gasColor);
        glVertex2f(left_control, display_padding / 4f);
        glVertex2f(right_control, display_padding / 4f);
        GL11.glEnd();

        GL11.glBegin(GL_LINES);
        setDrawColor(config.gasColor);
        glVertex2f(0, 1f - display_padding);
        glVertex2f(left_control, 1f - display_padding * 4);
        glVertex2f(right_control, 1f - display_padding * 4);
        glVertex2f(0, 1f - display_padding);
        GL11.glEnd();
    }

    @Override
    public void drawBrake(float brake) {
        float height = 1 - display_padding * 5 / 4f;
        float sqHeight = 1 - display_padding * 17 / 4f;
        float normalized = Math.max(0, height * brake);
        if (normalized > sqHeight) {
            GL11.glBegin(GL11.GL_TRIANGLES);
            setDrawColor(config.brakeColor);
            glVertex2f(left_control, -1f + display_padding * 4f);
            glVertex2f(right_control, -1f + display_padding * 4f);
            glVertex2f(0, -display_padding / 4f - normalized);
            GL11.glEnd();

            GL11.glBegin(GL_QUADS);
            setDrawColor(config.brakeColor);
            glVertex2f(left_control, -display_padding / 4f);
            glVertex2f(right_control, -display_padding / 4f);
            glVertex2f(right_control, -1f + display_padding * 4f);
            glVertex2f(left_control, -1f + display_padding * 4f);
            GL11.glEnd();
        } else {
            GL11.glBegin(GL_QUADS);
            setDrawColor(config.brakeColor);
            glVertex2f(left_control, -display_padding / 4f);
            glVertex2f(right_control, -display_padding / 4f);
            glVertex2f(right_control, -display_padding / 4f - normalized);
            glVertex2f(left_control, -display_padding / 4f - normalized);
            GL11.glEnd();
        }

        //BOX
        GL11.glBegin(GL_LINES);
        setDrawColor(config.brakeColor);
        glVertex2f(left_control, -display_padding / 4f);
        glVertex2f(right_control, -display_padding / 4f);
        GL11.glEnd();

        GL11.glBegin(GL_LINES);
        setDrawColor(config.brakeColor);
        glVertex2f(0, -1f + display_padding);
        glVertex2f(left_control, -1f + display_padding * 4);
        glVertex2f(right_control, -1f + display_padding * 4);
        glVertex2f(0, -1f + display_padding);
        GL11.glEnd();
    }

}
