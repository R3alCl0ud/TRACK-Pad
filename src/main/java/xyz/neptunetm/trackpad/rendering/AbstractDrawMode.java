package xyz.neptunetm.trackpad.rendering;

import xyz.neptunetm.trackpad.config.Config;

public abstract class AbstractDrawMode {
    protected Config config;

    public AbstractDrawMode(Config config) {
        this.config = config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public abstract void drawSteering(float steering);

    public abstract void drawGas(float gas);

    public abstract void drawBrake(float brake);
}
