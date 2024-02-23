package frc.robot.subsystems.noteSubsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class ServoMotor extends SubsystemBase {
  private static final Servo motor = new Servo(MotorConstants.servoPort);
  /**
   * @return The angle that the Servo is at
  */
  public double getAngle() {
    return motor.getAngle();
  }
  /**Sets the angle that the servo should travel to 
   * @param angle A number between 0 & 180
  */
  public void setAngle(double angle) {
    motor.setAngle(angle);
  }
}
