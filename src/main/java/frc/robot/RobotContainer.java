// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.proto.Controller;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.ControllerButtons;
import frc.robot.commands.EmptyCommand;
import frc.robot.commands.noteintake.IntakeControl;
import frc.robot.commands.noteintake.NoteTransfer;
import frc.robot.commands.noteintake.OutakeControl;
import frc.robot.commands.swervedrive.drivebase.AbsoluteDriveAdv;
import frc.robot.commands.swervedrive.drivebase.AbsoluteFieldDrive;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.NoteSubsystem;
import frc.robot.subsystems.VisionSubsystem;

import java.io.File;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Driver;
import java.sql.Driver;

import com.fasterxml.jackson.core.sym.Name;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPlannerTrajectory;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and controller are defined here...
  private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
  private final NoteSubsystem noteControl = new NoteSubsystem(false, true, false, true);
  // CommandJoystick rotationController = new CommandJoystick(1);
  // Replace with CommandPS4Controller or CommandJoystick if needed
  CommandJoystick driverController = new CommandJoystick(1);
  // CommandJoystick driverController   = new CommandJoystick(3);//(OperatorConstants.DRIVER_CONTROLLER_PORT);
  CommandXboxController driverXbox = new CommandXboxController(0);
  CommandXboxController opXbox = new CommandXboxController(1);
  
  private SendableChooser<Command> autoChooser;
  private SendableChooser<Boolean> oneController;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    NamedCommands.registerCommand("IntakeDown", new NoteTransfer(noteControl, true).withTimeout(1.25));
    NamedCommands.registerCommand("IntakeUp", new NoteTransfer(noteControl, false).withTimeout(1.25));
    NamedCommands.registerCommand("RunIntake", new IntakeControl(noteControl));
    NamedCommands.registerCommand("LaunchNote", new OutakeControl(noteControl).withTimeout(1.25));

    drivebase.setupPathPlanner();
    autoChooser = AutoBuilder.buildAutoChooser();
    
    autoChooser.addOption("Middle Blue", AutoBuilder.buildAuto("Middle Blue"));
    autoChooser.addOption("Middle Red", AutoBuilder.buildAuto("Middle Red"));
    autoChooser.addOption("Bottom Blue", AutoBuilder.buildAuto("Bottom Blue"));
    autoChooser.addOption("Bottom Red", AutoBuilder.buildAuto("Bottom Red"));
    autoChooser.addOption("Auto Test", AutoBuilder.buildAuto("Auto Test"));
    autoChooser.setDefaultOption("None", new EmptyCommand());
    SmartDashboard.putData(autoChooser);

    // Configure the trigger bindingsP
    configureBindings();
/*
    AbsoluteDriveAdv closedAbsoluteDriveAdv = new AbsoluteDriveAdv(drivebase,
      () -> MathUtil.applyDeadband(driverXbox.getLeftY(),
        OperatorConstants.LEFT_Y_DEADBAND),
      () -> MathUtil.applyDeadband(driverXbox.getLeftX(),
        OperatorConstants.LEFT_X_DEADBAND),
      () -> MathUtil.applyDeadband(driverXbox.getRightX(),
        OperatorConstants.RIGHT_X_DEADBAND),
      driverXbox::getYButtonPressed,
      driverXbox::getAButtonPressed,
      driverXbox::getXButtonPressed,
      driverXbox::getBButtonPressed);*/

    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the desired angle NOT angular rotation
    Command driveFieldOrientedDirectAngle = drivebase.driveCommand(
        () -> MathUtil.applyDeadband(-driverXbox.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(-driverXbox.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
        () -> MathUtil.applyDeadband(-driverXbox.getRightX(), OperatorConstants.RIGHT_X_DEADBAND),
        () -> MathUtil.applyDeadband(-driverXbox.getRightY(), OperatorConstants.RIGHT_Y_DEADBAND)
    );

    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the angular velocity of the robot
    Command driveFieldOrientedAnglularVelocity = drivebase.driveCommand(
        () -> MathUtil.applyDeadband(driverXbox.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(driverXbox.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
        () -> driverXbox.getRawAxis(2));

    Command driveFieldOrientedDirectAngleSim = drivebase.simDriveCommand(
        () -> MathUtil.applyDeadband(driverXbox.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(driverXbox.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
        () -> driverXbox.getRawAxis(2));

    drivebase.setDefaultCommand( driveFieldOrientedDirectAngle );
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings() {
    driverXbox.x().onTrue(new InstantCommand(drivebase::zeroGyro));
    // waypoint buttons

    if(oneController.getSelected()) {
      opXbox.rightBumper().whileTrue(new IntakeControl(noteControl));
      opXbox.leftBumper().whileTrue(new OutakeControl(noteControl));
      opXbox.y().onTrue(new NoteTransfer(noteControl, false));
      opXbox.x().onTrue(new NoteTransfer(noteControl, true));
      opXbox.pov(180).onTrue(new InstantCommand(noteControl::removeAngleBrake));
      opXbox.pov(0).whileTrue(new IntakeControl(noteControl, false));
    } else {
      opXbox.rightBumper().whileTrue(new IntakeControl(noteControl));
      opXbox.leftBumper().whileTrue(new OutakeControl(noteControl));
      opXbox.y().onTrue(new NoteTransfer(noteControl, false));
      opXbox.x().onTrue(new NoteTransfer(noteControl, true));
      opXbox.pov(180).onTrue(new InstantCommand(noteControl::removeAngleBrake));
      opXbox.pov(0).whileTrue(new IntakeControl(noteControl, false));
    }
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return autoChooser.getSelected();
  }

  public void setDriveMode() {
    //drivebase.setDefaultCommand();
  }

  public void setMotorBrake(boolean brake) {
    drivebase.setMotorBrake(brake);
  }
}