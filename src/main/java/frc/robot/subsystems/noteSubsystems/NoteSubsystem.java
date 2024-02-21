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

public class NoteSubsystem extends SubsystemBase {
  private static final CANSparkMax IntakeMotor = new CANSparkMax(9, MotorType.kBrushless);
  private static final CANSparkMax[] launchMotors = new CANSparkMax[]{
    new CANSparkMax(11, MotorType.kBrushless),
    new CANSparkMax(12, MotorType.kBrushless)
  };
  private static final Servo servoMotor = new Servo(9);

  /**A subsystem used to control the position of notes via motors.
   * 
   * @param isIntakeReversed Is the motor that pulls in notes reversed?
   * @param isAngleReversed Is the motor that moves the intake arm reversed?
   * @param isLauncherReversed Are the motors that shoot the note reversed?
   * @author El Campus
   */
  public NoteSubsystem(boolean isIntakeReversed, boolean isLauncherReversed) {
    IntakeMotor.setInverted(isIntakeReversed);
    launchMotors[0].setInverted(isLauncherReversed);
    launchMotors[1].setInverted(!isLauncherReversed);
    
  }
  /**Runs the motor to intake the notes
   * @param isIntake If set to true, the motor will intake a note
   */
  public void runIntake(boolean isIntake) {
    IntakeMotor.set(isIntake ? 1 : -1);
  }
  /**Stops the motor that intakes notes*/
  public void stopIntake() {
    IntakeMotor.set(0);
  }
  /**Shoot a note out of the launcher */
  public void runLaunch() {
    launchMotors[0].set(1);
    launchMotors[1].set(1);
  }
  /**Stop the motors that run the launcher */
  public void stopLaunch() {
    launchMotors[0].set(0);
    launchMotors[1].set(0);
  }
  /**Stops all the motors in the subsystem*/
  public void stopAll() {
    stopIntake();
    stopLaunch();
  }
  /**
   * @return The angle that the Servo is at
  */
  public double getServoAngle() {
    return servoMotor.getAngle();
  }
  /**Sets the angle that the servo should travel to 
   * @param angle A number between 0 & 180
  */
  public void setServoAngle(double angle) {
    servoMotor.setAngle(angle);
  }
}
