package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.noteSubsystems.NoteSubsystem;

public class runServo extends Command{
  NoteSubsystem system;
  double angle;

  public runServo(NoteSubsystem NoteSubsystemObj, double desiredAngle) {
    system = NoteSubsystemObj;
    angle = desiredAngle;
    addRequirements(system);
  }
  @Override
  public void initialize() {
    system.setServoAngle(angle);
  }
  @Override
  public boolean isFinished() {
    return angle == system.getServoAngle();
  }
}
