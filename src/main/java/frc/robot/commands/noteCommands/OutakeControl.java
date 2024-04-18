package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.noteSubsystems.IntakeMotor;
import frc.robot.subsystems.noteSubsystems.LaunchingMotors;

public class OutakeControl extends Command{
  private LaunchingMotors launchSystem;
  private IntakeMotor intakeSystem;
  //private LedStrip led;
  private double ticks, tick = 0.035;
  double Slow = 0.0;
  public OutakeControl(LaunchingMotors LauncherSubsystem, IntakeMotor IntakeSubsystem, double slow) {
    launchSystem = LauncherSubsystem;
    intakeSystem = IntakeSubsystem;
    addRequirements(IntakeSubsystem, launchSystem);
    Slow = slow;
  }

  @Override
  public void initialize() {
    launchSystem.run(1*Slow);
  }
  @Override
  public void execute() {
    ticks += tick;
    if(ticks >= 1) intakeSystem.run(false);
  }
  @Override
  public boolean isFinished() {
    return ticks >= 3;
  }
  @Override
  public void end(boolean interuppted) {
    ticks = 0;
  	launchSystem.stop();
    intakeSystem.stop();
  }
}
