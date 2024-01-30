package frc.robot.commands.noteintake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;


public class IntakeControl extends Command
{

static CANSparkMax GearControl = new CANSparkMax(0, MotorType.kBrushless);

 public static void jksdfh() {
    GearControl.set(1);
 }

}
    

