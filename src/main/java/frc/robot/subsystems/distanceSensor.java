package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class distanceSensor extends SubsystemBase {
  AnalogInput sensor;
  double tripVal = 4;

  public distanceSensor(int port) { sensor = new AnalogInput(port); }
  public distanceSensor(int port, double tripValue) {
    sensor = new AnalogInput(port);
    tripVal = tripValue;
  }

  public void writeVoltage() { SmartDashboard.putNumber("Distance sensor on port: " + sensor.getChannel() + " reads: ", sensor.getVoltage()); }
  public boolean isTripped() { return sensor.getVoltage() > tripVal; }
}