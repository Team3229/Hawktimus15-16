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

/*
 * Holly Springs High School Robotics Team
 * Hawktimus Prime, Team 3229
 * https://github.com/Team3229/Hawktimus15-16
 */

public class Robot extends SampleRobot {
    RobotDrive myRobot;  // class that handles basic drive operations
    Joystick leftStick;  // set to ID 1 in DriverStation
    Joystick rightStick; // set to ID 2 in DriverStation
    
    //Declare Talons
    byte talonFL = 1; //Talon, Front Left
    byte talonBL = 0;
    byte talonFR = 3;
    byte talonBR = 2;
    
    //Sensitivity value for drive system
    double sensitivity = .25;
    
    //Create a compressor object named compressor.
    Compressor compressor = new Compressor(0);
     
    //Create two double solenoid objects for the lift system and the ramp extender.
    DoubleSolenoid actuator = new DoubleSolenoid(0, 1);
    DoubleSolenoid rampExtension = new DoubleSolenoid(2, 3);
    
    //Start camera server
    CameraServer server = CameraServer.getInstance();
    
    //Sets camera at a default position (forward)
    double servoYaw = 0.0;
    double servoPitch = 0.5;
    
    //Create 2 servo motors for 2 directions
    Servo yaw = new Servo(5);  //Rotation
    Servo pitch = new Servo(4);  //Up and down
    
    //Speeds for both servo motors when turning
    double yawSpeed = .005;
    double pitchSpeed = .005;
    
    //Variables that can be used to get information about the compressor at runtime.
    boolean enabled = compressor.enabled();
    boolean pressureswitch = compressor.getPressureSwitchValue();
    float current = compressor.getCompressorCurrent();
    
    public Robot() {
    	//Create a Drive System with four motors
        myRobot = new RobotDrive(talonBR, talonFR, talonBL, talonFL); //weird order due to treads spinning backwards
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
        
        //Set sensitivity (default of .5)
        myRobot.setSensitivity(sensitivity);
    }
    
    //Start of autonomous
    public void autonomousPeriodic(){
    	System.out.println("Running Autonomous");
    	for(int x = 0; x < 750; x++){
    		myRobot.drive(.4, 0);  //drive robot with a .4 ratio
    		Timer.delay(0.005);
    	}
    	myRobot.drive(0, 0);  //Stop robot
    }
  
    //Run motors with tank steering
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	//Create tank drive controlled by two sticks previously declared
        	myRobot.tankDrive(rightStick, leftStick);
        	 Timer.delay(0.005);		// wait for a motor update time
            
            
          //Buttons for left stick
            boolean LTriggerPressed = leftStick.getRawButton(1);  //Actuator down
            boolean LFaceButtonUpPressed = leftStick.getRawButton(3);  // Solenoid in
            boolean LFaceButtonDownPressed = leftStick.getRawButton(2);  //Solenoid out
            boolean LFaceButtonLeftPressed = leftStick.getRawButton(4);  // Camera Reset
            boolean LBaseButton10Pressed = leftStick.getRawButton(10); //Turbo button
            
            //Buttons for right stick
            boolean RTriggerPressed = rightStick.getRawButton(1);  //Actuator up
            boolean RFaceButtonUpPressed = rightStick.getRawButton(3);  //Adjust camera up
            boolean RFaceButtonDownPressed = rightStick.getRawButton(2);  //Adjust camera down
            boolean RFaceButtonLeftPressed = rightStick.getRawButton(4);  //Rotate camera counterclockwise
            boolean RFaceButtonRightPressed = rightStick.getRawButton(5);  //Rotate camera clockwise
            
            
            //*******Start of solenoid code*********
            //Used to make ramp extension extend and retract
            if(LFaceButtonUpPressed ){rampExtension.set(DoubleSolenoid.Value.kForward);}
            else if(LFaceButtonDownPressed){rampExtension.set(DoubleSolenoid.Value.kReverse);}
          
            //Used to make actuator go up and down
            if(RTriggerPressed){actuator.set(DoubleSolenoid.Value.kForward);}
            else if(LTriggerPressed){actuator.set(DoubleSolenoid.Value.kReverse);}
            //**************************************
            
            
            /*******Start of servo code*******
            ***NOTE***
           * Additional checks are put in place in order to avoid sending the servo an improper value (<0 or >1)
            */
            //Adjust pitch up
            if(RFaceButtonUpPressed && servoPitch < 1){ servoPitch+=pitchSpeed; }
            //Adjust pitch down
            else if(RFaceButtonDownPressed && servoPitch > 0){servoPitch-=pitchSpeed;}
            
            //Rotate clockwise
            if(RFaceButtonRightPressed && servoYaw >0){servoYaw-=yawSpeed;}
            //Rotate counterclockwise
            else if(RFaceButtonLeftPressed && servoYaw <1){ servoYaw+=yawSpeed; }
            
            //Reset Camera to forward
            if(LFaceButtonLeftPressed){servoYaw=0; servoPitch=.5;}
            
            //Turbo Boost when button is pressed
            if(LBaseButton10Pressed){sensitivity=.5;}
            //Reset sensitivity to default when button is not pressed
            else{sensitivity=.25;}
            
            //Set the servo motor to newly calculated location
            yaw.set(servoYaw);
            pitch.set(servoPitch);
            myRobot.setSensitivity(sensitivity);
            //*********************************
        }
        
    }

}
