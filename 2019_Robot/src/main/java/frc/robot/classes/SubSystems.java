/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.classes;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

//import com.revrobotics.CANSparkMax;

/**
 * This class runs the subsystems for the 2019 game robot.
 */
public class SubSystems {

    // Create Talon Speed Controllers
    private Talon m_climbFront;
    private Talon m_climbBack;
    private Talon m_climbDrive;

    // Create Spark Max Speed Controller
    //private CANSparkMax m_lift;

    // Create Solenoid
    private Solenoid m_hatcher;

    // Create Configurable Values
    public NetworkTableEntry m_deadzone;

    /**
     * This initializes all of the motors and base settings for the robot subsystems
     * @param ClimbFront The motor for the Talon front climber
     * @param ClimbBack The motor for the Talon back climber
     * @param ClimbDrive The Talon drive motor on the climber
     * @param Lift The CANSparkMax Lift Motor
     * @param Hatcher The solenoid for the Hatcher system
     * @param defaultDeadzone The default for Switchboard deadzone value
     */
    public SubSystems(Talon ClimbFront, Talon ClimbBack, Talon ClimbDrive, /*CANSparkMax Lift,*/ Solenoid Hatcher, double defaultDeadzone)
    {
        m_climbFront = ClimbFront;
        m_climbBack = ClimbBack;
        m_climbDrive = ClimbDrive;
        //m_lift = Lift;
        m_hatcher = Hatcher;

        m_deadzone = Shuffleboard.getTab("SubSystems").add("Joystick Deadzone", defaultDeadzone).withWidget("Number Slider").withPosition(2, 2).withSize(2, 1).getEntry();
    }

    /**
     * This runs the climb motors
     * @param joystickLeftY The left joystick value
     * @param joystickRightY The right joystick value
     * @param joystickDriveY The drive joystick value
     */
    public void climber(double joystickLeftY, double joystickRightY, double joystickDriveY)
    {
        // Impliment Deadzone
        if(joystickLeftY < m_deadzone.getDouble(.02) && joystickLeftY > -m_deadzone.getDouble(.02))
        {
            joystickLeftY = 0;
        }
        if(joystickRightY < m_deadzone.getDouble(.02) && joystickRightY > -m_deadzone.getDouble(.02))
        {
            joystickRightY = 0;
        }
        if(joystickDriveY < m_deadzone.getDouble(.02) && joystickDriveY > -m_deadzone.getDouble(.02))
        {
            joystickDriveY = 0;
        }

        // Square joystick values
        double updatedLeft = joystickLeftY * Math.abs(joystickLeftY);
        double updatedRight = joystickRightY * Math.abs(joystickRightY);
        double updatedDrive = joystickDriveY * Math.abs(joystickDriveY);

        // Set front values
        m_climbFront.set(updatedLeft);
        // Set back values
        m_climbBack.set(updatedRight);
        // Set drive values
        m_climbDrive.set(updatedDrive);
    }

    /**
     * Runs the lift motors
     * @param joystickY The lift joystick value
     */
    public void lift(double joystickY)
    {
        // Impliment Deadzone
        if(joystickY < m_deadzone.getDouble(.02) && joystickY > -m_deadzone.getDouble(.02))
        {
            joystickY = 0;
        }

        // Square joystick values
        double updatedY = joystickY * Math.abs(joystickY);

        // Set motor value
        //m_lift.set(updatedY);
    }

    /**
     * Runs the hatcher solenoid
     * @param button The button to activate the solenoid
     */
    public void hatcher(Boolean button)
    {
      m_hatcher.set(button);
    }
}