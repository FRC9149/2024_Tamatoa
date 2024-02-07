package frc.robot.subsystems;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.units.Angle;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class NoteSubsystem extends SubsystemBase {
    private static final CANSparkMax IntakeMotor = new CANSparkMax(9, MotorType.kBrushless);
    private static final CANSparkMax AngleMotor = new CANSparkMax(10, MotorType.kBrushless);
    private static final CANSparkMax[] launchMotors = new CANSparkMax[]{
        new CANSparkMax(20, MotorType.kBrushless),
        new CANSparkMax(30, MotorType.kBrushless)
    };

    private static final DutyCycleEncoder AngleEncoder = new DutyCycleEncoder(0);

    /** A subsystem used to control the position of notes via motors.
     * 
     * @param isIntakeReversed Is the motor that pulls in notes reversed?
     * @param isAngleReversed Is the motor that moves the intake arm reversed?
     * @param isLauncherReversed Are the motors that shoot the note reversed?
     * @param isEncoderReversed Is the Encoder that reads the rotation of the IntakeArm reversed?
     * @author El Campus
     */
    public NoteSubsystem(boolean isIntakeReversed, boolean isAngleReversed, boolean isLauncherReversed, boolean isEncoderReversed) {
        IntakeMotor.setInverted(isIntakeReversed);
        AngleMotor.setInverted(isAngleReversed);
        launchMotors[0].setInverted(isLauncherReversed);
        launchMotors[1].setInverted(!isLauncherReversed);
        AngleEncoder.setDistancePerRotation(360);//set distance to be the current angle in degrees
        zeroAngle();
    }
    /** Runs the motor to intake the notes
     * @param isIntake If set to true, the motor will intake a note
     */
    public void runIntake(boolean isIntake) {
        IntakeMotor.set(isIntake ? 1 : -1);
    }
    /** Stops the motor that intakes notes*/
    public void stopIntake() {
        IntakeMotor.set(0);
    }
    /** Runs the motor to move the intake from the ground to outake.
    *   @param speed A number betwwen 1 and -1.
    */
    public void runAngle(double speed) {
        if(speed <=1 && speed >= -1) AngleMotor.set(speed);
    }
    /** Stops the motor that moves the intake arm */
    public void stopAngle() {
        AngleMotor.set(0);
    }
    /**zero's the AngleEncoder so that 0 is the current angle */
    public void zeroAngle() {
        AngleEncoder.reset();
    }
    /** Shoot a note out of the launcher */
    public void runLaunch() {
        launchMotors[0].set(1);
        launchMotors[1].set(1);
    }
    /** Stop the motors that run the launcher */
    public void stopLaunch() {
        launchMotors[0].set(0);
        launchMotors[1].set(0);
    }
    /** Stops all the motors in the subsystem*/
    public void stopAll() {
        stopIntake();
        stopAngle();
    }
    /** set the Angle Motor braking to either true of false.
     * 
     * @param isBrake if true: the motor will enter braking
     */
    public void setAngleBrake(boolean isBrake) {
        AngleMotor.setIdleMode(isBrake ? IdleMode.kBrake : IdleMode.kCoast);
    }

    /** Returns the angle of the Intake arm since zero'd
     * @returns double (Angle in Radians)
     */
    public double getAngleRad() {
        return AngleEncoder.isConnected() ? AngleEncoder.getDistance() * (Math.PI / 180) : -1;
    }
    /** Returns the angle of the Intake arm since zero'd
     * @returns double (Angle in Degrees)
     */
    public double getAngleDeg() {
        return AngleEncoder.isConnected() ? AngleEncoder.getDistance() : -1;
    }
    /**
     * @return double (Position of encoder)
     */
    public double getAnglePosition() {
        return AngleEncoder.isConnected() ? AngleEncoder.getAbsolutePosition() : -1;
    }

    @Override
    public void periodic() {
        if(AngleEncoder.isConnected()){
            SmartDashboard.putNumber("AngleEncoder get", AngleEncoder.get());
            SmartDashboard.putNumber("AngleEncoder position", AngleEncoder.getAbsolutePosition() - AngleEncoder.getPositionOffset());
            SmartDashboard.putNumber("AngleEncoder distance", AngleEncoder.getDistance());
        }
    }
}