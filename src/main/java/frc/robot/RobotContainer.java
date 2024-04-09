// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
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
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
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
  
  public static final XboxController driverXbox = new XboxController(0);
  public static final XboxController opXbox = new XboxController(1);
  
  private SendableChooser<Command> autoChooser, drivetype;
  private SendableChooser<Double> speed, outakeSpeed;
  private SendableChooser<Boolean> twoControllers;

  Command driveFieldOrientedDirectAngle = drivebase.driveCommand(
    () -> MathUtil.applyDeadband((-driverXbox.getLeftY() * speed.getSelected()) * (1 - opXbox.getLeftTriggerAxis()) , OperatorConstants.LEFT_Y_DEADBAND),
    () -> MathUtil.applyDeadband((-driverXbox.getLeftX() * speed.getSelected()) * (1 - opXbox.getLeftTriggerAxis()) , OperatorConstants.LEFT_X_DEADBAND),
    () -> MathUtil.applyDeadband(-driverXbox.getRightX(), OperatorConstants.RIGHT_X_DEADBAND),
    () -> MathUtil.applyDeadband(-driverXbox.getRightY(), OperatorConstants.RIGHT_Y_DEADBAND)
  );
  Command driveFieldOrientedAnglularVelocity = drivebase.driveCommand(
    () -> MathUtil.applyDeadband((-driverXbox.getLeftY() * speed.getSelected()) * (1 - opXbox.getLeftTriggerAxis()), OperatorConstants.LEFT_Y_DEADBAND),
    () -> MathUtil.applyDeadband((-driverXbox.getLeftX() * speed.getSelected()) * (1 - opXbox.getLeftTriggerAxis()), OperatorConstants.LEFT_X_DEADBAND),
    () -> driverXbox.getRightX() * 0.5
  );

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    NamedCommands.registerCommand("IntakeDown", new NoteTransfer(intakeArm, false).withTimeout(.75));
    NamedCommands.registerCommand("IntakeUp", new NoteTransfer(intakeArm, true).withTimeout(.75));
    NamedCommands.registerCommand("RunIntake", new IntakeControl(intake, true));
    NamedCommands.registerCommand("LaunchNote", new OutakeControl(launcher, intake, outakeSpeed.getSelected()).withTimeout(1));

    drivetype.setDefaultOption("direct", driveFieldOrientedDirectAngle);
    drivetype.addOption("angular", driveFieldOrientedAnglularVelocity);

    twoControllers.setDefaultOption("One Controller", true);
    twoControllers.addOption("Two Controller", false);

    speed.setDefaultOption("1", 1.0);
    outakeSpeed.setDefaultOption("1", 1.0);

    drivebase.setupPathPlanner();
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData(autoChooser);
    SmartDashboard.putData(speed);
    SmartDashboard.putData(outakeSpeed);
    SmartDashboard.putData(twoControllers);
    SmartDashboard.putData(drivetype);
    configureBindings();

    drivetype.onChange(cmd -> updateDriveCommand(cmd));
  }

  void updateDriveCommand(Command cmd) {
    drivebase.setDefaultCommand(cmd);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings() {
    /*Driver Controller:
     *    Driving sticks
     *    Shooting: right trigger
     * 
     *Operator Controller:
     *    intake: right bumper (switches)
     *    arm up/down: x/y (switches)
     *    remove brake: both sticks
     *    spin forever: 4 letter buttons
     *    slow down: left Trigger
     *    release note: b
     */
    new Trigger(()->{return driverXbox.getRightTriggerAxis()>0.1;}).whileTrue(new OutakeControl(launcher, intake, outakeSpeed.getSelected()));
    new JoystickButton(driverXbox, ControllerButtons.rbButton).and(new Trigger(()->twoControllers.getSelected())).whileTrue(new IntakeControl(intake, true));
    new JoystickButton(opXbox, ControllerButtons.rbButton).and(new Trigger(()->!twoControllers.getSelected())).whileTrue(new IntakeControl(intake, true));

    new JoystickButton(driverXbox, ControllerButtons.xButton).and(new Trigger(()->twoControllers.getSelected())).onTrue(new NoteTransfer(intakeArm, false));
    new JoystickButton(driverXbox, ControllerButtons.yButton).and(new Trigger(()->twoControllers.getSelected())).onTrue(new NoteTransfer(intakeArm, true));
    new JoystickButton(opXbox, ControllerButtons.xButton).and(new Trigger(()->!opXbox.getAButton())).and(new Trigger(()->!twoControllers.getSelected())).onTrue(new NoteTransfer(intakeArm, false));
    new JoystickButton(opXbox, ControllerButtons.yButton).and(new Trigger(()->!opXbox.getAButton())).and(new Trigger(()->!twoControllers.getSelected())).onTrue(new NoteTransfer(intakeArm, true));

    Trigger leftInTrigger = new JoystickButton(opXbox, ControllerButtons.leftIn);
    new JoystickButton(opXbox, ControllerButtons.rightIn).and(leftInTrigger).onTrue(new InstantCommand(intakeArm::removeBrake));
    
    new JoystickButton(opXbox, ControllerButtons.menu).onTrue(new InstantCommand(drivebase::zeroGyro));

    new JoystickButton(opXbox, ControllerButtons.bButton).and(new Trigger(()->!opXbox.getAButton())).whileTrue(new IntakeControl(intake, false));

    new JoystickButton(opXbox, ControllerButtons.aButton ).and(
    new JoystickButton(opXbox, ControllerButtons.bButton)).and(
    new JoystickButton(opXbox, ControllerButtons.xButton)).and(
    new JoystickButton(opXbox, ControllerButtons.yButton)).toggleOnTrue(Commands.deferredProxy(()->{
      updateDriveCommand(drivebase.driveCommand(
        () -> {return 0;},
        () -> {return 0;},
        () -> {return 1 - opXbox.getLeftTriggerAxis();}
      ));
      return null;
    }));
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
