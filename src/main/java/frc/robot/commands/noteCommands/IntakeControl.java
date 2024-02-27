package frc.robot.commands.noteCommands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LedStrip;
import frc.robot.subsystems.noteSubsystems.IntakeMotor;

public class IntakeControl extends Command {
  private IntakeMotor system;
  private LedStrip led;
  private boolean isIntake = true;

  public IntakeControl (IntakeMotor IntakeSubsystem, boolean isIntake, LedStrip leds) {
    system = IntakeSubsystem;
    led = leds;
    addRequirements(system, led);
    this.isIntake = isIntake;
  }
  @Override
  public void initialize() {
    system.run(isIntake);
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 1);
  }
  @Override
  public void execute() {
    led.rainbow();
  }
  @Override
  public boolean isFinished() {
    return false;
  }
  @Override
  public void end(boolean interuppted){
    system.stop();
    led.off();
    RobotContainer.opXbox.setRumble(RumbleType.kBothRumble, 0);
  }

}
    

