package frc.robot.subsystems.noteSubsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.MotorConstants;

public class IntakeArm extends SubsystemBase {
  private static final CANSparkMax motor = new CANSparkMax(MotorConstants.IntakeArmPort, MotorType.kBrushless);

  private static final DutyCycleEncoder AngleEncoder = new DutyCycleEncoder(0);
  private static final double AngleEncoderOffset = 289.287187;

  public IntakeArm(boolean isReversed) {
    motor.setInverted(isReversed);
    AngleEncoder.setDistancePerRotation(360);//set distance to be the current angle in degrees
  }

  /**Run the motor at a certain speed
  *   @param speed A double between -1 & 1 to set the speed in percentage; limited to 100%.
  */
  public void run(double speed) {
    motor.set(speed > 1 ? 1 : speed < -1 ? -1 : speed);
  }
  /**Stops the Arm from moving*/
  public void stop() {
    motor.set(0);
  }
  /**Apply motor braking to the intake arm*/
  public void addBrake() {
    motor.setIdleMode(IdleMode.kBrake);
  }
  /**Set braking mode to coast for the intake arm*/
  public void removeBrake(){
    motor.setIdleMode(IdleMode.kCoast);
  }
  /**Returns the angle of the Intake arm
   * @returns double (Angle in Degrees)
   */
  public double getAngleDeg() {
    return AngleEncoder.isConnected() ? AngleEncoder.getDistance() - AngleEncoderOffset: -1;
  }

}