package frc.robot.subsystems;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.units.Angle;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    private CANSparkMax IntakeMotor = new CANSparkMax(0, MotorType.kBrushless);
    private CANSparkMax AngleMotor = new CANSparkMax(0, MotorType.kBrushless);

    private Encoder AngleEncoder;

    public IntakeSubsystem(boolean isIntakeReversed, boolean isAngleReversed, boolean isEncoderReversed) {
        IntakeMotor.setInverted(isIntakeReversed);
        AngleMotor.setInverted(isAngleReversed);
        AngleEncoder = new Encoder(0, 1, isEncoderReversed, EncodingType.k2X);
    }
    public void runIntake() {
        IntakeMotor.set(1);
    }
    public void stopIntake() {
        IntakeMotor.set(0);
    }
    public void stopAngle() {
        AngleMotor.set(0);
    }
    public void stopAll() {
        stopIntake();
        stopAngle();
    }
}