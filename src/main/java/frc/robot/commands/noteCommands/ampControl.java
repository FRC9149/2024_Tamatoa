package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.noteSubsystems.AmpMotor;

public class ampControl extends Command {
  AmpMotor system;
  /**Run the motor to push a note into the amp
   * @param AmpMotorSubsystem The subsystem that runs the Amp motor
   * @param targetTime How long to run the motor for. If set to a negative number, the motor will reverse backwards.
   */
  public ampControl(AmpMotor AmpMotorSubsystem) {
    system = AmpMotorSubsystem;
    addRequirements(system);
  }

  @Override
  public void initialize() {
    system.run();
  }
  @Override
  public boolean isFinished() {
    return false;
  }
  @Override
  public void end(boolean interrupted) {
    system.stop();
  }
}
