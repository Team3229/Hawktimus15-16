package org.usfirst.frc.team3229.robot;


import edu.wpi.first.wpilibj.SampleRobot;
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
 */
public class Robot extends SampleRobot {
    RobotDrive myRobot;  // class that handles basic drive operations
    //Use two joysticks for two sticks on controller (one for left tread, one for right)
    Joystick ps3;

    double leftX = ps3.getRawAxis(0);
    double leftY = ps3.getRawAxis(1);
    double rightX = ps3.getRawAxis(4);
    double rightY = ps3.getRawAxis(5);
    
    public Robot() {
        myRobot = new RobotDrive(0, 1, 2, 3); //Four arguments are present because 2 motors per tread (2*2=4)
        myRobot.setExpiration(0.1);
        //Map sticks
        ps3 = new Joystick(0);
    }
    //Runs the motors with tank steering.
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) { //Start loop that will be run while game is active
        	myRobot.tankDrive(ps3,ps3);
            Timer.delay(0.005);		// wait for a motor update time
        }
    }

}
