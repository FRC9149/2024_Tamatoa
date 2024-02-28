package frc.robot.subsystems.noteSubsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class IntakeMotor extends SubsystemBase {
  private static final CANSparkMax IntakeMotor = new CANSparkMax(MotorConstants.IntakePort, MotorType.kBrushless);

  public IntakeMotor(boolean isReversed) {
    IntakeMotor.setInverted(isReversed);
  }

  /**Runs the motor to intake the notes
   * @param isIntake If set to true, the motor will intake a note
   */
  public void run(boolean isIntake) {
    IntakeMotor.set(isIntake ? 1 : -1);
  }
  public void run(double speed) {
    IntakeMotor.set(speed);
  }
  /**Stops the motor that intakes notes*/
  public void stop() {
    IntakeMotor.set(0);
  }
}
