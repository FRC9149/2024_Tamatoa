package frc.robot.subsystems.noteSubsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class LaunchingMotors extends SubsystemBase {
  private static final CANSparkMax[] launchMotors = new CANSparkMax[]{
    new CANSparkMax(MotorConstants.LauncherPorts[0], MotorType.kBrushless),
    new CANSparkMax(MotorConstants.LauncherPorts[1], MotorType.kBrushless)
  };

  public LaunchingMotors(boolean isReversed) {
    launchMotors[0].setInverted(isReversed);
    launchMotors[1].setInverted(!isReversed);
  }

  /**Shoot a note out of the launcher */
  public void run() {
    launchMotors[0].set(1);
    launchMotors[1].set(1);
  }
  /**Stop the motors that run the launcher */
  public void stop() {
    launchMotors[0].set(0);
    launchMotors[1].set(0);
  }
}
