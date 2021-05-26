package xyz.neptunetm.trackpad.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import xyz.neptunetm.trackpad.GLBuilder;
import xyz.neptunetm.trackpad.config.Config;
import xyz.neptunetm.trackpad.rendering.AbstractDrawMode;
import xyz.neptunetm.trackpad.rendering.styles.DrawModeB;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class GLComponent extends AWTGLCanvas {
    private Config config;
    private AbstractDrawMode drawMode;

    public GLComponent(Config config) {
        this.config = config;
        drawMode = new DrawModeB(config);
    }

    public void setConfig(Config config) {
        this.config = config;
        drawMode.setConfig(config);
    }

    @Override
    public void initGL() {
        if (!glfwInit())
            throw new IllegalStateException("Failed to init GLFW");
        GL.createCapabilities();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        System.out.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
    }

    @Override
    public void paintGL() {
        int w = getWidth();
        int h = getHeight();
        float aspect = (float) w / h;
        GL11.glViewport(0, 0, w, h);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        doDraw();
        swapBuffers();
        GLFW.glfwPollEvents();
    }

    private void doDraw() {
        GL11.glDisable(GL11.GL_BLEND);

        GLBuilder.getBuilder()
            .mode(GL11.GL_QUADS)
            .color(config.window.backgroundColor, 1f)
            .vertex(-1f, 1f)
            .vertex(1f, 1f)
            .vertex(1f, -1f)
            .vertex(-1f, -1f)
            .end();

        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glLineWidth(2.5f);

        float steering = 0, gas = 0, brake = 0;
        if (GLFW.glfwJoystickPresent(0)) {
            FloatBuffer axesBuffer = GLFW.glfwGetJoystickAxes(0);
            ByteBuffer buttonsBuffer = GLFW.glfwGetJoystickButtons(0);
            assert axesBuffer != null;
            assert buttonsBuffer != null;

            if (config.controls.isAnalogueSteering) {
                steering = axesBuffer.get(config.controls.stick);
            } else {
                steering = buttonsBuffer.get(config.controls.right) - buttonsBuffer.get(config.controls.left);
            }

            if (config.controls.isAnalogueThrottle) {
                brake = (axesBuffer.get(config.controls.brake) + 1) / 2f;
                gas = (axesBuffer.get(config.controls.gas) + 1) / 2f;
            } else {
                brake = buttonsBuffer.get(config.controls.brake);
                gas = buttonsBuffer.get(config.controls.gas);
            }

            if (Math.abs(steering) < config.steeringDeadzone)
                steering = 0;
            if (Math.abs(brake) < 0.1)
                brake = 0;
            if (Math.abs(gas) < 0.1)
                gas = 0;
        }
        //TODO: DO KEYBOARD HERE


        //begin draw
        drawMode.drawSteering(steering);
        drawMode.drawGas(gas);
        drawMode.drawBrake(brake);

    }

    public void startRenderLoop() {
        Runnable renderLoop = new Runnable() {
            public void run() {
                if (!isValid())
                    return;
                render();
                SwingUtilities.invokeLater(this);
            }
        };
        SwingUtilities.invokeLater(renderLoop);
    }
}
