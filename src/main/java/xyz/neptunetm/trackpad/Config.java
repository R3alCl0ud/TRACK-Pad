package xyz.neptunetm.trackpad;

public class Config {
    //    public float backgroundColor;
    //    public float
    public int backgroundColor = 0xFFFFFF, steeringColor = 0xD25943, gasColor = 0x55BC4D, brakeColor = 0xE73236;
    public float steeringDeadzone = 0.05f, gasDeadzone = 0.05f, brakeDeadzone = 0.05f;

//    public byte gas_axis = 0, brake_axis = 1, steering_axis = 0, steering_left = 0, steering_right = 0;

    public Window window = new Window();
    public Controls controls = new Controls();



    public static class Window {
        public int height = 400, width = 700;
    }

    public static class Controls {
        public int stick = 0;
        public int left = 14;
        public int right = 12;
        public boolean isAnalogueThrottle = false;
        public boolean isAnalogueSteering = true;
        public int gas = 0, brake = 1;


    }

}
