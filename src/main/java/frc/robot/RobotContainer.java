// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.ControllerButtons;
import frc.robot.commands.noteCommands.IntakeControl;
import frc.robot.commands.noteCommands.NoteTransfer;
import frc.robot.commands.noteCommands.OutakeControl;
import frc.robot.commands.noteCommands.ampControl;
import frc.robot.commands.noteCommands.visionOutput;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.distanceSensor;
import frc.robot.subsystems.noteSubsystems.AmpMotor;
import frc.robot.subsystems.noteSubsystems.IntakeArm;
import frc.robot.subsystems.noteSubsystems.IntakeMotor;
import frc.robot.subsystems.noteSubsystems.LaunchingMotors;

import java.io.File;
import java.util.function.Consumer;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and controllers are defined here...
  private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));

  private final IntakeMotor intake = new IntakeMotor(false);
  private final LaunchingMotors launcher = new LaunchingMotors(false);
  private final IntakeArm intakeArm = new IntakeArm(false);

  private final VisionSubsystem vision = new VisionSubsystem();
  
  public static final XboxController driverXbox = new XboxController(0);
  
  private SendableChooser<Command> autoChooser, drivetype = new SendableChooser<>();
  private SendableChooser<Double> speed = new SendableChooser<>(), outakeSpeed = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    NamedCommands.registerCommand("IntakeDown", new NoteTransfer(intakeArm, false).withTimeout(.75));
    NamedCommands.registerCommand("IntakeUp", new NoteTransfer(intakeArm, true).withTimeout(.75));
    NamedCommands.registerCommand("RunIntake", new IntakeControl(intake, true));
    NamedCommands.registerCommand("LaunchNote", new OutakeControl(launcher, intake, 1).withTimeout(1));

    Command driveFieldOrientedDirectAngle = drivebase.driveCommand(
    () -> MathUtil.applyDeadband((-driverXbox.getLeftY()), OperatorConstants.LEFT_Y_DEADBAND),
    () -> MathUtil.applyDeadband((-driverXbox.getLeftX()), OperatorConstants.LEFT_X_DEADBAND),
    () -> MathUtil.applyDeadband(-driverXbox.getRightX(), OperatorConstants.RIGHT_X_DEADBAND),
    () -> MathUtil.applyDeadband(-driverXbox.getRightY(), OperatorConstants.RIGHT_Y_DEADBAND),
    () -> speed.getSelected()
  );
  
  Command driveFieldOrientedAnglularVelocity = drivebase.driveCommand(
    () -> MathUtil.applyDeadband((-driverXbox.getLeftY()), OperatorConstants.LEFT_Y_DEADBAND),
    () -> MathUtil.applyDeadband((-driverXbox.getLeftX()), OperatorConstants.LEFT_X_DEADBAND),
    () -> driverXbox.getRightX(),
    () -> speed.getSelected()
  );

    drivetype.setDefaultOption("Direct", driveFieldOrientedDirectAngle);
    drivetype.addOption("Angular", driveFieldOrientedAnglularVelocity);

    speed.setDefaultOption("speed: 1", 1.0);
    speed.setDefaultOption("speed: .5", 0.5);
    
    outakeSpeed.setDefaultOption("outakeSpeed: 1", 1.0);
    outakeSpeed.setDefaultOption("outakeSpeed: .5", 0.5);

    drivebase.setupPathPlanner();
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData(autoChooser);
    SmartDashboard.putData(speed);
    SmartDashboard.putData(outakeSpeed);
    SmartDashboard.putData(drivetype);
    configureBindings();

    drivetype.onChange(cmd -> drivebase.setDefaultCommand(cmd));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings() {
    new Trigger(()->{return driverXbox.getRightTriggerAxis()>0.1;}).whileTrue(new OutakeControl(launcher, intake, 1));
    new Trigger(()->{return driverXbox.getLeftTriggerAxis() >0.1;}).whileTrue(new IntakeControl(intake, true));

    new JoystickButton(driverXbox, ControllerButtons.xButton).onTrue(new NoteTransfer(intakeArm, false));
    new JoystickButton(driverXbox, ControllerButtons.yButton).onTrue(new NoteTransfer(intakeArm, true));

    new JoystickButton(driverXbox, ControllerButtons.capture).onTrue(new InstantCommand(intakeArm::removeBrake));
    new JoystickButton(driverXbox, ControllerButtons.menu).onTrue(new InstantCommand(drivebase::zeroGyro));   
    
    new Trigger(()->{return vision.getTx() > 0;}).whileTrue(new visionOutput(vision));
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


  public void setMotorBrake(boolean brake) {
    drivebase.setMotorBrake(brake);
  }
}
