package main;

public class Config {
    //    public float backgroundColor;
    //    public float
    public int backgroundColor = 0xFFFFFF, steeringColor = 0xD25943, gasColor = 0x55BC4D, brakeColor = 0xE73236;
    public float steeringDeadzone = 0f, gasDeadzone = 0f, brakeDeadzone = 0f;
    public boolean isDigitalThrottle = true;
    public boolean isAnalogueSteering = true;
    public byte gas_axis = 0, brake_axis = 0, steering_axis = 0, steering_left = 0, steering_right = 0;

    public Window window = new Window();

    public class Window {
        public int height = 700, width = 400;
    }

}
