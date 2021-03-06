/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.classes.TankDrive;
//import frc.robot.classes.PixyLineFollow;
import frc.robot.classes.SubSystems;
import frc.robot.classes.BlinkIn;
import frc.robot.classes.Camera;

public class Robot extends TimedRobot {

  //Create Joysticks
  Joystick m_joystickLeft;
  Joystick m_joystickRight;
  Joystick m_gamepad;

  // Create Drive Motors
  TalonSRX m_leftFront;
  TalonSRX m_rightFront;
  VictorSPX m_leftBack;
  VictorSPX m_rightBack;

  // Create Climb Motors
  Talon m_climbFront;
  Talon m_climbBack;
  Talon m_climbDrive;

  // Create Lift Motor
  Spark m_lift;

  // Create Intake Motor
  Talon m_intake;

  // Create Compressor and Solenoids
  Compressor m_compressor;
  Solenoid m_hatcherGround;
  Solenoid m_hatcherExtend;
  Solenoid m_hatcherExpand;

  // Create BlinkIn Spark
  Spark m_blinkInController;

  // Creat Limit Switches
  DigitalInput m_liftLimitBottom;
  DigitalInput m_liftLimitTop;
  DigitalInput m_climbLeftBottom;
  DigitalInput m_climbLeftTop;
  DigitalInput m_climbRightBottom;
  DigitalInput m_climbRightTop;

  //Create Custom Classes
  TankDrive m_drive;
  SubSystems m_subSystems;
  //PixyLineFollow m_pixy;
  Camera m_camera;
  BlinkIn m_blinkIn;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    // Initialize Joysticks

    m_joystickLeft = new Joystick(0);
    m_joystickRight = new Joystick(1);
    m_gamepad = new Joystick(2);

    // Initialize Drive Motors
    m_leftFront = new TalonSRX(7);
    m_rightFront = new TalonSRX(9);
    m_leftBack = new VictorSPX(6);
    m_rightBack = new VictorSPX(8);

    // Initialize Climb Motors
    m_climbFront = new Talon(2);
    m_climbBack = new Talon(0);
    m_climbDrive = new Talon(1);

    // Initialize Lift Motor
    m_lift = new Spark(4);

    // Initialize Intake Motor
    m_intake = new Talon(3);

    // Initialize Compressor and Solenoids
    m_compressor = new Compressor();
    m_hatcherExpand = new Solenoid(1);
    m_hatcherExtend = new Solenoid(2);
    m_hatcherGround = new Solenoid(3);

    // Initialize BlinkIn Spark
    m_blinkInController = new Spark(5);

    // Initialize Limit Switches
    m_liftLimitBottom = new DigitalInput(4);
    m_liftLimitTop = new DigitalInput(5);
    m_climbLeftBottom = new DigitalInput(1);
    m_climbLeftTop = new DigitalInput(0);
    m_climbRightBottom = new DigitalInput(2);
    m_climbRightTop = new DigitalInput(3);

    // Configure Drive
    m_leftFront.setInverted(true);
    m_leftBack.setInverted(true);
    m_leftBack.follow(m_leftFront);
    m_rightBack.follow(m_rightFront);

    // Initialize Custom Classes
    m_drive = new TankDrive(m_leftFront, m_rightFront, .02);
    m_subSystems = new SubSystems(m_climbFront, m_climbBack, m_climbDrive, m_lift, m_intake, m_hatcherGround, m_hatcherExtend, m_hatcherExpand, .02, 0, -613);
    //m_pixy = new PixyLineFollow();
    m_camera = new Camera(false);
    m_blinkIn = new BlinkIn(m_blinkInController, -.99);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   */
  @Override
  public void robotPeriodic() {
    //m_pixy.arduinoRead();
  }

  /**
   * This function is called when autonomous is first started.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    // Drive with joysticks or pixy
    if(!m_joystickLeft.getRawButton(1)){
      m_drive.drive(m_joystickLeft.getRawAxis(1), m_joystickRight.getRawAxis(1), 1);
    }
    else {
      //m_pixy.lineFollowTalonSRX(m_leftFront, m_rightFront, .2);
    }
    // Run Climb or Lift and Hatcher Subsystems
    if(m_gamepad.getRawButton(5))
    {
      // Run Climber Subsystem
      m_subSystems.climber(m_gamepad.getRawAxis(1), m_gamepad.getRawAxis(5), m_gamepad.getRawAxis(3), m_gamepad.getRawButton(1), m_climbLeftBottom.get(), m_climbLeftTop.get(), m_climbRightBottom.get(), m_climbRightTop.get());

      // Zero Other Motors
      m_subSystems.liftZero();
      m_subSystems.intakeZero();
      m_subSystems.hatcherZero();
    }
    else
    {
      // Run Lift Subsystem
      m_subSystems.lift(m_gamepad.getRawAxis(1), m_gamepad.getPOV(), m_leftFront.getSelectedSensorPosition(), m_liftLimitBottom.get(), m_liftLimitTop.get());
      // Run Intake Subsystem
      m_subSystems.intake(m_gamepad.getRawAxis(2), m_gamepad.getRawAxis(3));
      // Run Hatcher Subsystem
      //.hatcher(Ground, Extend, Expand)
      m_subSystems.hatcher(m_gamepad.getRawButton(1), m_gamepad.getRawButton(6), m_gamepad.getRawButton(3));

      // Zero Other Motors
      m_subSystems.climbZero();
    }

    //Run BlinkIn
    m_blinkIn.update();

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

