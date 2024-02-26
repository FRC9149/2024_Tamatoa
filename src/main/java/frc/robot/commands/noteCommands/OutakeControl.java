package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LedStrip;
import frc.robot.subsystems.noteSubsystems.IntakeMotor;
import frc.robot.subsystems.noteSubsystems.LaunchingMotors;

public class OutakeControl extends Command{
  private LaunchingMotors launchSystem;
  private IntakeMotor intakeSystem;
  private double ticks, tick = 0.03;

  public OutakeControl(LaunchingMotors LauncherSubsystem, IntakeMotor IntakeSubsystem, LedStrip leds) {
    launchSystem = LauncherSubsystem;
    intakeSystem = IntakeSubsystem;
    addRequirements(launchSystem);
    addRequirements(IntakeSubsystem);
  }

  @Override
  public void initialize() {
    launchSystem.run();
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 1);
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
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 0);
  }
}
