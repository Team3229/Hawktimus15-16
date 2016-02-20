package org.usfirst.frc.team3229.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;


public class Robot extends SampleRobot {
    RobotDrive myRobot;  // class that handles basic drive operations
    Joystick leftStick;  // set to ID 1 in DriverStation
    Joystick rightStick; // set to ID 2 in DriverStation
    
    //Create a compressor object named compressor.
    Compressor compressor = new Compressor(0);
     
    //Create two double solenoid objects for the lift system and the ramp extender.
    DoubleSolenoid actuator = new DoubleSolenoid(0, 1);
    DoubleSolenoid rampExtension = new DoubleSolenoid(2, 3);
    
    CameraServer server = CameraServer.getInstance();
    
    double servoYaw = 0.5;
    double servoPitch = 0.5;
    
    Servo yaw = new Servo(4);
    Servo pitch = new Servo(5);
    
    //Variables that can be used to get information about the compressor at runtime.
    boolean enabled = compressor.enabled();
    boolean pressureswitch = compressor.getPressureSwitchValue();
    float current = compressor.getCompressorCurrent();
    
    public Robot() {
    	//Create a Drive System with four motors
        myRobot = new RobotDrive(0, 1, 2, 3);
        myRobot.setExpiration(0.1);
        //Two Logitech sticks are used, so two joystick objects are created.
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        //Set the compressor system to "Closed." This will allow the compressor to turn on and off automatically.
        compressor.setClosedLoopControl(true);
        //Camera quality can be set here, higher number is higher quality
        server.setQuality(75);
        //Make sure name of camera found in setup is the name that appears here
        server.startAutomaticCapture("axis-camera");
    }
  
    //Run motors with tank steering
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	myRobot.tankDrive(leftStick, rightStick);
            Timer.delay(0.005);		// wait for a motor update time
            
          //Code for status of buttons used for solenoids
            boolean LTriggerPressed = leftStick.getRawButton(1);
            boolean RTriggerPressed = rightStick.getRawButton(1);
            boolean LFaceButtonUpPressed = leftStick.getRawButton(3);
            boolean LFaceButtonDownPressed = leftStick.getRawButton(2);
            
            //Code for Ramp Extension
            if(LFaceButtonUpPressed == true){rampExtension.set(DoubleSolenoid.Value.kForward);}
            else if(LFaceButtonDownPressed == true){rampExtension.set(DoubleSolenoid.Value.kReverse);}
          
            //Code for actuator
            if(RTriggerPressed == true){actuator.set(DoubleSolenoid.Value.kForward);}
            else if(LTriggerPressed == true){actuator.set(DoubleSolenoid.Value.kReverse);}
            
            //Start of servo code
            if(rightStick.getRawButton(3) && servoPitch < 1){ //Up
            		servoPitch+=.005;
            }
            else if(rightStick.getRawButton(2) && servoPitch > 0){ //Down
            		servoPitch-=.005;
            }
            
            if(rightStick.getRawButton(5) && servoYaw >0){ //Left
            	servoYaw-=.005;
            }
            else if(rightStick.getRawButton(4) && servoYaw <1){ //Right
            	servoYaw+=.005;
            }
            
            yaw.set(servoYaw);
            pitch.set(servoPitch);
        }
        
    }

}
