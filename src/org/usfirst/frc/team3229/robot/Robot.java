package org.usfirst.frc.team3229.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically it 
 * contains the code necessary to operate a robot with tank drive.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    RobotDrive myRobot;  // class that handles basic drive operations
    Joystick leftStick;  // set to ID 1 in DriverStation
    Joystick rightStick; // set to ID 2 in DriverStation
    
    Compressor compressor = new Compressor(0);
     
    DoubleSolenoid actuator = new DoubleSolenoid(0, 1);
    DoubleSolenoid rampExtension = new DoubleSolenoid(2, 3);
    
    boolean enabled = compressor.enabled();
    boolean pressureswitch = compressor.getPressureSwitchValue();
    float current = compressor.getCompressorCurrent();
    
    
    public Robot() {
        myRobot = new RobotDrive(0, 1, 2, 3);
        myRobot.setExpiration(0.1);
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        compressor.setClosedLoopControl(true);
    }

    
    /**
     * Runs the motors with tank steering.
     */
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	myRobot.tankDrive(leftStick, rightStick);
            Timer.delay(0.005);		// wait for a motor update time
            
            //Code for Ramp Extension
            if(leftStick.getRawButton(2)){rampExtension.set(DoubleSolenoid.Value.kForward);}
            else if(leftStick.getRawButton(1)){rampExtension.set(DoubleSolenoid.Value.kReverse);}
          
            //Code for actuator
            if(rightStick.getRawButton(0)){actuator.set(DoubleSolenoid.Value.kForward);}
            else if(leftStick.getRawButton(0)){actuator.set(DoubleSolenoid.Value.kReverse);}
        }
        
    }

}
