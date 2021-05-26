package xyz.neptunetm.trackpad.config;

public class Config {
    public int steeringColor = 0xD25943;
    public int gasColor = 0x55BC4D;
    public int brakeColor = 0xE73236;


    public float steeringDeadzone = 0.05f; // deadzone for joysticks
    public float gasDeadzone = 0.05f; // deadzone for triggers/joysticks
    public float brakeDeadzone = 0.05f; // deadzone for triggers/joysticks

    public Window window = new Window(); // settings for the windows
    public Controls controls = new Controls();


    public static class Window {
        public int height = 400, width = 700, backgroundColor = 0x222222;
    }

    public static class Controls {
        public int stick = 0;
        public int left = 14;
        public int right = 12;
        public boolean isAnalogueThrottle = false;
        public boolean isAnalogueSteering = true;
        public int gas = 0, brake = 1;
        public int backgroundColor = 0x9a9a9a;

    }

}
