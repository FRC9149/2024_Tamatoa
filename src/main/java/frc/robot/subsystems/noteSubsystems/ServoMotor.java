package frc.robot.subsystems.noteSubsystems;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.units.Angle;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class ServoMotor extends SubsystemBase {
  private static final Servo servoMotor = new Servo(MotorConstants.servoPort);
  /**
   * @return The angle that the Servo is at
  */
  public double getAngle() {
    return servoMotor.getAngle();
  }
  /**Sets the angle that the servo should travel to 
   * @param angle A number between 0 & 180
  */
  public void setAngle(double angle) {
    servoMotor.setAngle(angle);
  }
}
