package frc.robot.commands.noteCommands;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.noteSubsystems.IntakeMotor;
import frc.robot.subsystems.noteSubsystems.LaunchingMotors;

public class OutakeControl extends Command{
  private LaunchingMotors launchSystem;
  private IntakeMotor intakeSystem;
  private double ticks, tick = 0.035;
  double speed = 1;

  public OutakeControl(LaunchingMotors LauncherSubsystem, IntakeMotor IntakeSubsystem, double speed) {
    launchSystem = LauncherSubsystem;
    intakeSystem = IntakeSubsystem;
    addRequirements(IntakeSubsystem, launchSystem);
    this.speed = speed;
  }

  @Override
  public void initialize() { launchSystem.run(speed); }
  @Override
  public void execute() {
    ticks += tick;
    if(ticks >= 1) intakeSystem.run(false);
  }
  @Override
  public boolean isFinished() { return false; }
  @Override
  public void end(boolean interuppted) {
    ticks = 0;
  	launchSystem.stop();
    intakeSystem.stop();
  }
}
