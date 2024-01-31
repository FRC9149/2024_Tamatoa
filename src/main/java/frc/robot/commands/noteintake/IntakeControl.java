package frc.robot.commands.noteintake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.EndEffector;


public class IntakeControl extends Command {

   private static CANSparkMax GearControl = new CANSparkMax(0, MotorType.kBrushless);
   private boolean isReversed = false;

   public IntakeControl (boolean isReversed) {
      this.isReversed = isReversed;
   }

   @Override
   public void initialize() {
      GearControl.set(isReversed ? -1 : 1);
   }
   @Override
   public boolean isFinished() {
      return false;
   }
   @Override
   public void end(boolean interuppted){
      GearControl.set(0);
   }

}
    

