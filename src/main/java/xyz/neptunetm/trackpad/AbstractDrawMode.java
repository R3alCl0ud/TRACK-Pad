package xyz.neptunetm.trackpad;

public abstract class AbstractDrawMode implements DrawFunction {
    protected Config config;

    public AbstractDrawMode(Config config) {
        this.config = config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
