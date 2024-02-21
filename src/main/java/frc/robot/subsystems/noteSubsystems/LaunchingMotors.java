package frc.robot.subsystems.noteSubsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class LaunchingMotors extends SubsystemBase {
  private static final CANSparkMax[] launchMotors = new CANSparkMax[]{
    new CANSparkMax(11, MotorType.kBrushless),
    new CANSparkMax(12, MotorType.kBrushless)
  };
}
