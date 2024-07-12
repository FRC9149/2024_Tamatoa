package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;

public class visionOutput extends Command {
  VisionSubsystem system;
  public visionOutput(VisionSubsystem subsystem) {
    system = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    SmartDashboard.putNumber("X: ", system.getTx());
    SmartDashboard.putNumber("Y: ", system.getTy());
  }

  @Override
  public boolean runsWhenDisabled() { return true;}
}
    

