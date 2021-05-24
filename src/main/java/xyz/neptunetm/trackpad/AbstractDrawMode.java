package xyz.neptunetm.trackpad;

public abstract class AbstractDrawMode implements DrawFunction {
    protected final Config config;

    public AbstractDrawMode(Config config) {
        this.config = config;
    }
}
