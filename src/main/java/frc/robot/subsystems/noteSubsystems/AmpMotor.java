package frc.robot.subsystems.noteSubsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class AmpMotor extends SubsystemBase {
    private static final CANSparkMax motor = new CANSparkMax(MotorConstants.AmpPort, MotorType.kBrushed);
    private static final RelativeEncoder encoder = motor.getEncoder();

    public AmpMotor(boolean isReversed) {
        motor.setInverted(isReversed);
    }
    public void run() {
        motor.set(1);
    }
    public void stop() {
        motor.set(0);
    }
    /**
     * @return The number of rotations that the motor has done
     */
    public double getEncoder() {
        return encoder.getPosition();
    }
}
