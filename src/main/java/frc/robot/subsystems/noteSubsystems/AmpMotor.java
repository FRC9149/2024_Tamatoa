package frc.robot.subsystems.noteSubsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class AmpMotor extends SubsystemBase {
    private static final CANSparkMax motor = new CANSparkMax(MotorConstants.AmpPort, MotorType.kBrushless);

    public AmpMotor(boolean isReversed) {
        motor.setInverted(isReversed);
    }
    public void run() {
        motor.set(1);
    }
    public void stop() {
        motor.set(0);
    }
}
