package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LedStrip extends SubsystemBase {
  private AddressableLED led;
  private AddressableLEDBuffer ledBuffer;
  private double rainbowPoint = 0;

  public LedStrip(int port, int length) {
    led = new AddressableLED(port);
    ledBuffer = new AddressableLEDBuffer(length);
    led.setLength(ledBuffer.getLength());
  }

  public void off() {
    for(int i = 0; i < ledBuffer.getLength(); i++) ledBuffer.setRGB(i, 0, 0, 0);
    start();
  }

  /**To be placed in periodic or execute */
  public void rainbow() {
    for(int i = 0; i < ledBuffer.getLength(); i++) {
      double hue = (rainbowPoint + (i * 180 / ledBuffer.getLength())) % 180;
      ledBuffer.setHSV(i, (int)hue, 255, 128);
    }
    rainbowPoint += 3;
    rainbowPoint %= 180;
    start();
  }

  public void solid(Color color) {
    for(int i = 0; i < ledBuffer.getLength(); i++) ledBuffer.setLED(i, color);
  }

  public void setRGB(int index, int r, int g, int b) {
    ledBuffer.setRGB(index, r, g, b);
    start();
  }

  void start() {
    led.setData(ledBuffer);
    led.start();
  }
}
