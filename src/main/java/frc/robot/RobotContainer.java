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
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
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
import frc.robot.commands.noteCommands.IntakeControl;
import frc.robot.commands.noteCommands.NoteTransfer;
import frc.robot.commands.noteCommands.OutakeControl;
import frc.robot.commands.noteCommands.ampControl;
import frc.robot.commands.noteCommands.runServo;
import frc.robot.commands.swervedrive.drivebase.AbsoluteDriveAdv;
import frc.robot.commands.swervedrive.drivebase.AbsoluteFieldDrive;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.noteSubsystems.AmpMotor;
import frc.robot.subsystems.noteSubsystems.IntakeArm;
import frc.robot.subsystems.noteSubsystems.IntakeMotor;
import frc.robot.subsystems.noteSubsystems.LaunchingMotors;
import frc.robot.subsystems.noteSubsystems.ServoMotor;

import java.io.File;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Driver;
import java.sql.Driver;

import com.fasterxml.jackson.core.sym.Name;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPlannerTrajectory;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and controllers are defined here...
  private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
  private final VisionSubsystem vision = new VisionSubsystem();

  private final ServoMotor noteControl = new ServoMotor();
  private final IntakeMotor intake = new IntakeMotor(false);
  private final LaunchingMotors launcher = new LaunchingMotors(false);
  private final AmpMotor ampMotor = new AmpMotor(false);
  private final IntakeArm intakeArm = new IntakeArm(true);
  
  public static final XboxController driverXbox = new XboxController(0);
  public static final XboxController opXbox = new XboxController(1);
  
  private SendableChooser<Command> autoChooser;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    NamedCommands.registerCommand("IntakeDown", new NoteTransfer(intakeArm, true).withTimeout(1));
    NamedCommands.registerCommand("IntakeUp", new NoteTransfer(intakeArm, false).withTimeout(1));
    NamedCommands.registerCommand("RunIntake", new IntakeControl(intake, true));
    NamedCommands.registerCommand("LaunchNote", new OutakeControl(launcher, intake).withTimeout(1));

    drivebase.setupPathPlanner();
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData(autoChooser);
    configureBindings();

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
    new JoystickButton(driverXbox, ControllerButtons.menu).onTrue(new InstantCommand(drivebase::zeroGyro));
    new JoystickButton(driverXbox, ControllerButtons.lbButton).whileTrue(new OutakeControl(launcher, intake)); // Optional

    new JoystickButton(driverXbox, ControllerButtons.aButton).whileTrue(AutoBuilder.pathfindThenFollowPath(
      PathPlannerPath.fromPathFile(DriverStation.getAlliance().isPresent() ? DriverStation.getAlliance().get()==Alliance.Red ? "Home Red" : "Home Blue" : ""), 
      new PathConstraints(2, 2, 540, 720)
    ));

    /*new Trigger(()->{return driverXbox.getPOV()==9;}).whileTrue(Commands.deferredProxy(()->{
      driverXbox.setRumble(RumbleType.kBothRumble, 1);
      Pose2d movement = vision.PickUpNote();
      if(movement != null) drivebase.drive(movement.getTranslation(), movement.getRotation().getDegrees(), false);
      return new EmptyCommand();
    }).andThen(Commands.deferredProxy(()->{
      driverXbox.setRumble(RumbleType.kBothRumble, 0);
      return new EmptyCommand();
    })));*/

    new JoystickButton(opXbox, ControllerButtons.rbButton).whileTrue(new IntakeControl(intake, true));
    new JoystickButton(opXbox, ControllerButtons.lbButton).whileTrue(new OutakeControl(launcher, intake));

    new JoystickButton(opXbox, ControllerButtons.xButton).onTrue(new NoteTransfer(intakeArm, false));
    new JoystickButton(opXbox, ControllerButtons.yButton).onTrue(new NoteTransfer(intakeArm, true));

    new JoystickButton(opXbox, ControllerButtons.capture).onTrue(new InstantCommand(intakeArm::removeBrake));
    new JoystickButton(opXbox, ControllerButtons.bButton).whileTrue(new IntakeControl(intake, false));
    new JoystickButton(opXbox, ControllerButtons.aButton).whileTrue(new ampControl(ampMotor));

    new JoystickButton(driverXbox, ControllerButtons.xButton).onTrue(new runServo(noteControl, 180));
    new JoystickButton(driverXbox, ControllerButtons.yButton).onTrue(new runServo(noteControl, 0));
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