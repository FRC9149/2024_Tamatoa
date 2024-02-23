package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.noteSubsystems.ServoMotor;

public class runServo extends Command{
  ServoMotor system;
  double angle;

  public runServo(ServoMotor ServoSubsystem, double desiredAngle) {
    system = ServoSubsystem;
    angle = desiredAngle;
    addRequirements(system);
  }
  @Override
  public void initialize() {
    system.setAngle(angle);
  }
  @Override
  public boolean isFinished() {
    return angle == system.getAngle();
  }
}
