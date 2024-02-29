package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.noteSubsystems.IntakeMotor;

public class IntakeControl extends Command {
  private IntakeMotor system;
  private boolean isIntake;

  public IntakeControl (IntakeMotor IntakeSubsystem, boolean isIntake) {
    system = IntakeSubsystem;
    addRequirements(system);
    this.isIntake = isIntake;
  }
  @Override
  public void initialize() {
    system.run(isIntake ? 1 : -1);
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 1);
  }
  @Override
  public boolean isFinished() {
    return false;
  }
  @Override
  public void end(boolean interuppted){
    system.stop();
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 0);
  }

}
    

