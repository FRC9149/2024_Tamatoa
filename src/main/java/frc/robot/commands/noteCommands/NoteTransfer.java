package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.noteSubsystems.IntakeArm;

public class NoteTransfer extends Command {
  private IntakeArm system;
  private double desiredAngle;

  /** Command to move the Intake Arm to the desired position for launching and picking up notes
   * 
   * @param corgi The IntakeArm object (corgi is an inside joke)
   * @param flip If true: the arm will travel to the ground, else the launcher for shooting
   */
  public NoteTransfer(IntakeArm corgi, boolean flip) {
    system = corgi;
    addRequirements(system);
    desiredAngle = flip ? -190 : 0;
    if(flip) system.addBrake(); else system.removeBrake();
  }
  @Override
  public void initialize() {
    system.run(desiredAngle == 0 ? -0.5 : 0.5);
    RobotContainer.opXbox.setRumble(desiredAngle == 0 ? RumbleType.kRightRumble : RumbleType.kLeftRumble , 1);
  }
  @Override
  public boolean isFinished() {
    return desiredAngle == 0 ? system.getAngleDeg() >= -2 : system.getAngleDeg() <= -Math.abs((desiredAngle) - 2);
  }
  @Override
  public void end(boolean interuppted) {
    system.stop();
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 0);
  }
}
