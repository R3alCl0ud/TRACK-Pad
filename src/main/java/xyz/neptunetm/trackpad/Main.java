package xyz.neptunetm.trackpad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.system.MemoryStack;
import xyz.neptunetm.trackpad.config.Config;
import xyz.neptunetm.trackpad.rendering.AbstractDrawMode;
import xyz.neptunetm.trackpad.rendering.styles.DrawModeB;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private long window;

    private boolean isHeld = false;

    public static float steering;
    public static float brake;
    public static float gas;

    private Config config;

    private FloatBuffer steering_buffer;
    private ByteBuffer buttons_buffer;

    private AbstractDrawMode drawFunction;

    public static void main(String... args) {
        new Main().loadConfig().run();
    }

    public Main loadConfig() {

        File cfg = new File("./config.json");
        if (cfg.exists()) {
            try (FileReader reader = new FileReader(cfg)) {
                config = gson.fromJson(reader, Config.class);
            } catch (IOException ex) {
                ex.printStackTrace();
                config = new Config();
                writeConfig();
            }
        } else {
            config = new Config();
            writeConfig();
        }
        if (config != null && drawFunction != null) {
            drawFunction.setConfig(config);
        }
        return this;
    }

    public void writeConfig() {
        File cfg = new File("./config.json");
        if (cfg.exists()) {
            if (!cfg.renameTo(new File("./config.json.bak"))) return;
        }
        try (FileWriter writer = new FileWriter(cfg)) {
            writer.write(gson.toJson(config));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Failed to init GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are
        // already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden
        // after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be
        // resizable

        // Create the window
        window = glfwCreateWindow(config.window.width, config.window.height, "TRACK Pad", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        /*
         * Setup a key callback. It will be called every time a key is pressed, repeated
         * or released.
         */

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//            glfwGetPrimaryMonitor()
            // Center the window
//            vidmode.
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();

        // manager = new RenderManager(window);
        // renderer = new Renderer(window);
        System.out.println("Joystick 0 found: " + glfwJoystickPresent(0));
        steering_buffer = GLFW.glfwGetJoystickAxes(0);

    }

    private void doDraw() {
        glDisable(GL_BLEND);

//        GL11.glPushMatrix();
        GL11.glBegin(GL_QUADS);
        setDrawColor(config.window.backgroundColor);
        glVertex2f(-1f, 1);
        glVertex2f(1f, 1);
        glVertex2f(1f, -1);
        glVertex2f(-1f, -1);
        GL11.glEnd();

        glPolygonMode(GL_FRONT, GL_FILL);
        glEnable(GL_BLEND);
        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
        GL14.glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glLineWidth(2.5f);

        drawFunction.drawSteering(steering);
        drawFunction.drawGas(gas);
        drawFunction.drawBrake(brake);
    }


    private void handleJoyshits() {
        if (!isHeld && glfwGetKey(window, GLFW_KEY_TAB) == 1) {
            System.out.println("Reloading config master");
            loadConfig();
            isHeld = true;
        } else if (isHeld && glfwGetKey(window, GLFW_KEY_TAB) != 1) {
            isHeld = false;
        }

        steering_buffer = GLFW.glfwGetJoystickAxes(0);
        buttons_buffer = glfwGetJoystickButtons(GLFW_JOYSTICK_1);

        // if (steering_buffer != null) {
        if (config.controls.isAnalogueSteering) {
            if (glfwJoystickPresent(0)) {
                try {
                    steering = steering_buffer.get(config.controls.stick);
                } catch (Exception ignored) {
                }
            } else {
                if (glfwJoystickPresent(0)) {
                    try {
                        steering = buttons_buffer.get(config.controls.right) - buttons_buffer.get(config.controls.left);
                    } catch (Exception ignored) {
                    }
                } else {
                    // implements keyboard support in a kinda dumb way
                    steering = glfwGetKey(window, config.controls.right) - glfwGetKey(window, config.controls.left);
                }
            }
        }
        if (config.controls.isAnalogueThrottle) {
            if (glfwJoystickPresent(0)) {
                try {
                    brake = (steering_buffer.get(config.controls.brake) + 1) / 2f;
                    gas = (steering_buffer.get(config.controls.gas) + 1) / 2f;
                } catch (Exception ignored) {
                }
            }
        } else {
            if (glfwJoystickPresent(0)) {

                try {
                    brake = buttons_buffer.get(config.controls.brake);
                    gas = buttons_buffer.get(config.controls.gas);
                } catch (Exception ignored) {
                }
            } else {
                try {
                    gas = glfwGetKey(window, config.controls.gas);
                    brake = glfwGetKey(window, config.controls.brake);
                } catch (Exception ignored) {
                }
            }
        }

        if (Math.abs(steering) < config.steeringDeadzone)
            steering = 0;
        if (Math.abs(brake) < 0.1)
            brake = 0;
        if (Math.abs(gas) < 0.1)
            gas = 0;
        // }
    }


    private void loop() {
        /*
         * This line is critical for LWJGL's interoperation with GLFW's OpenGL context,
         * or any context that is managed externally. LWJGL detects the context that is
         * current in the current thread, creates the GLCapabilities instance and makes
         * the OpenGL bindings available for use.
         */

        // Set the clear color
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
//		glEnable(GL11.GL_DEPTH_TEST);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            // clear the framebuffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//             handle joyshits
            handleJoyshits();
            // Draw shit
            doDraw();
            // handle the players movement first, because it's more logical to
            // do it like this
            // this will be moved into a different thread later

            // swap the color buffers
            glfwSwapBuffers(window);
            // event shit
            GLFW.glfwPollEvents();

        }
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL_TEXTURE_2D);
    }

    public void run() {
        drawFunction = new DrawModeB(config);
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        System.setProperty("java.awt.headless", "true");
        init();
        loop();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        //
        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
