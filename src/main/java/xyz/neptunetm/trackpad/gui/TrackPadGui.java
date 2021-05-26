package xyz.neptunetm.trackpad.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import xyz.neptunetm.trackpad.GLBuilder;
import xyz.neptunetm.trackpad.config.Config;
import xyz.neptunetm.trackpad.rendering.AbstractDrawMode;
import xyz.neptunetm.trackpad.rendering.styles.DrawModeB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class TrackPadGui extends JFrame {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Config config;
    private AbstractDrawMode drawMode;

    public static void main(String... args) {
        JFrame frame = new TrackPadGui();
    }

    public TrackPadGui() {
        super();
        loadConfig();
        drawMode = new DrawModeB(config);
        setLayout(new GridLayout(1, 2));
        setSize(config.window.width, config.window.height);
        add(new JButton("TEST"));
        GLComponent glComponent = new GLComponent(config);
        add(glComponent, BorderLayout.CENTER);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        glComponent.startRenderLoop();
    }

    public void loadConfig() {
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
        if (config != null && drawMode != null) {
            drawMode.setConfig(config);
        }
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
}
