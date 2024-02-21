package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.noteSubsystems.NoteSubsystem;

public class OutakeControl extends Command{
  private NoteSubsystem system;
  private double ticks, tick = 0.03;

  public OutakeControl(NoteSubsystem NoteSubsystemObj) {
    system = NoteSubsystemObj;
    addRequirements(system);
  }

  @Override
  public void initialize() {
    system.runLaunch();
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 1);
  }
  @Override
  public void execute() {
    ticks += tick;
    if(ticks >= 1) system.runIntake(false);
  }
  @Override
  public boolean isFinished() {
    return false;
  }
  @Override
  public void end(boolean interuppted) {
    ticks = 0;
  	system.stopAll();
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 0);
  }
}
